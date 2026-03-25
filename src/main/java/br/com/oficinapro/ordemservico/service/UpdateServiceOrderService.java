package br.com.oficinapro.ordemservico.service;

import br.com.oficinapro.auth.domain.User;
import br.com.oficinapro.auth.reposirory.UserRepository;
import br.com.oficinapro.cliente.domain.Client;
import br.com.oficinapro.cliente.repository.ClientRepository;
import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.estoque.service.ServiceOrderStockService;
import br.com.oficinapro.financeiro.domain.Receipt;
import br.com.oficinapro.financeiro.service.ServiceOrderFinancialStatusService;
import br.com.oficinapro.financeiro.service.ServiceOrderReceiptValidationService;
import br.com.oficinapro.ordemservico.domain.ServiceOrder;
import br.com.oficinapro.ordemservico.domain.ServiceOrderProductItem;
import br.com.oficinapro.ordemservico.domain.ServiceOrderServiceItem;
import br.com.oficinapro.ordemservico.domain.ServiceOrderStatusHistory;
import br.com.oficinapro.ordemservico.dto.request.ServiceOrderProductItemRequest;
import br.com.oficinapro.ordemservico.dto.request.ServiceOrderReceiptRequest;
import br.com.oficinapro.ordemservico.dto.request.ServiceOrderRequest;
import br.com.oficinapro.ordemservico.dto.request.ServiceOrderServiceItemRequest;
import br.com.oficinapro.ordemservico.dto.response.ServiceOrderResponse;
import br.com.oficinapro.ordemservico.mapper.ServiceOrderMapper;
import br.com.oficinapro.ordemservico.repository.ServiceOrderRepository;
import br.com.oficinapro.produto.domain.Product;
import br.com.oficinapro.produto.repository.ProductRepository;
import br.com.oficinapro.servico.domain.Services;
import br.com.oficinapro.servico.repository.ServicesRepository;
import br.com.oficinapro.veiculo.domain.Vehicle;
import br.com.oficinapro.veiculo.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UpdateServiceOrderService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final ServiceOrderMapper serviceOrderMapper;
    private final ClientRepository clientRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final ServicesRepository servicesRepository;
    private final ProductRepository productRepository;
    private final ServiceOrderStockService serviceOrderStockService;
    private final ServiceOrderFinancialStatusService serviceOrderFinancialStatusService;
    private final ServiceOrderReceiptValidationService serviceOrderReceiptValidationService;

    public UpdateServiceOrderService(ServiceOrderRepository serviceOrderRepository,
                                     ServiceOrderMapper serviceOrderMapper,
                                     ClientRepository clientRepository,
                                     VehicleRepository vehicleRepository,
                                     UserRepository userRepository,
                                     ServicesRepository servicesRepository,
                                     ProductRepository productRepository,
                                     ServiceOrderStockService serviceOrderStockService,
                                     ServiceOrderFinancialStatusService serviceOrderFinancialStatusService,
                                     ServiceOrderReceiptValidationService serviceOrderReceiptValidationService) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.serviceOrderMapper = serviceOrderMapper;
        this.clientRepository = clientRepository;
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.servicesRepository = servicesRepository;
        this.productRepository = productRepository;
        this.serviceOrderStockService = serviceOrderStockService;
        this.serviceOrderFinancialStatusService = serviceOrderFinancialStatusService;
        this.serviceOrderReceiptValidationService = serviceOrderReceiptValidationService;
    }

    @Transactional
    public ServiceOrderResponse update(String code, ServiceOrderRequest request) {
        ServiceOrder serviceOrder = serviceOrderRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Service order not found with code: " + code));

        var previousStatus = serviceOrder.getStatus();
        validateStatusTransition(previousStatus, request.status());
        validateFinishedOrderRestrictions(previousStatus, request);

        serviceOrderMapper.updateEntity(request, serviceOrder);
        serviceOrder.setOpenedAt(request.openedAt() != null ? request.openedAt() : serviceOrder.getOpenedAt());
        assignMainReferences(serviceOrder, request);
        replaceServiceItems(serviceOrder, request.serviceItems());
        replaceProductItems(serviceOrder, request.productItems());
        if (request.receipts() != null) {
            replaceReceipts(serviceOrder, request.receipts());
        }
        calculateTotals(serviceOrder, request);
        serviceOrderReceiptValidationService.validateServiceOrderReceipts(serviceOrder);
        serviceOrderFinancialStatusService.recalculate(serviceOrder);
        normalizeFields(serviceOrder);

        if (previousStatus != br.com.oficinapro.ordemservico.domain.enums.ServiceOrderStatus.FINISHED
                && serviceOrder.getStatus() == br.com.oficinapro.ordemservico.domain.enums.ServiceOrderStatus.FINISHED) {
            serviceOrderStockService.outputProducts(serviceOrder, serviceOrder.getResponsibleTechnician());
        }

        if (previousStatus != serviceOrder.getStatus()) {
            serviceOrder.getStatusHistory().add(buildStatusHistory(serviceOrder, previousStatus));
        }

        ServiceOrder updated = serviceOrderRepository.save(serviceOrder);
        return new ServiceOrderResponse(updated);
    }

    private void validateStatusTransition(br.com.oficinapro.ordemservico.domain.enums.ServiceOrderStatus currentStatus,
                                          br.com.oficinapro.ordemservico.domain.enums.ServiceOrderStatus newStatus) {
        if (!currentStatus.canTransitionTo(newStatus)) {
            throw new BusinessException("Invalid status transition from " + currentStatus + " to " + newStatus);
        }
    }

    private void validateFinishedOrderRestrictions(br.com.oficinapro.ordemservico.domain.enums.ServiceOrderStatus currentStatus,
                                                   ServiceOrderRequest request) {
        if (currentStatus == br.com.oficinapro.ordemservico.domain.enums.ServiceOrderStatus.FINISHED
                && request.productItems() != null) {
            throw new BusinessException("Cannot change product items of a finished service order");
        }
    }

    private void assignMainReferences(ServiceOrder serviceOrder, ServiceOrderRequest request) {
        Client client = clientRepository.findByCode(request.clientCode().trim())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with code: " + request.clientCode()));
        Vehicle vehicle = vehicleRepository.findByCode(request.vehicleCode().trim())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with code: " + request.vehicleCode()));

        serviceOrder.setClient(client);
        serviceOrder.setVehicle(vehicle);
        serviceOrder.setResponsibleTechnician(findUserByCode(request.responsibleTechnicianCode(), "Responsible technician"));
        serviceOrder.setApprovedBy(findUserByCode(request.approvedByCode(), "Approved by"));
    }

    private void replaceServiceItems(ServiceOrder serviceOrder, List<ServiceOrderServiceItemRequest> requests) {
        serviceOrder.getServiceItems().clear();
        if (requests == null) {
            return;
        }

        for (ServiceOrderServiceItemRequest request : requests) {
            Services service = servicesRepository.findByCode(request.serviceCode().trim())
                    .orElseThrow(() -> new ResourceNotFoundException("Service not found with code: " + request.serviceCode()));

            ServiceOrderServiceItem item = new ServiceOrderServiceItem();
            item.setServiceOrder(serviceOrder);
            item.setService(service);
            item.setComplementaryDescription(request.complementaryDescription());
            item.setQuantity(request.quantity());
            item.setUnitPrice(resolveServiceUnitPrice(request, service));
            item.setDiscount(defaultZero(request.discount()));
            item.setTotalAmount(calculateItemTotal(item.getUnitPrice(), item.getQuantity(), item.getDiscount()));
            item.setResponsibleTechnician(findUserByCode(request.responsibleTechnicianCode(), "Responsible technician"));
            serviceOrder.getServiceItems().add(item);
        }
    }

    private void replaceProductItems(ServiceOrder serviceOrder, List<ServiceOrderProductItemRequest> requests) {
        serviceOrder.getProductItems().clear();
        if (requests == null) {
            return;
        }

        for (ServiceOrderProductItemRequest request : requests) {
            Product product = productRepository.findByCode(request.productCode().trim())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with code: " + request.productCode()));

            ServiceOrderProductItem item = new ServiceOrderProductItem();
            item.setServiceOrder(serviceOrder);
            item.setProduct(product);
            item.setQuantity(request.quantity());
            item.setUnitPrice(resolveProductUnitPrice(request, product));
            item.setDiscount(defaultZero(request.discount()));
            item.setTotalAmount(calculateItemTotal(item.getUnitPrice(), item.getQuantity(), item.getDiscount()));
            serviceOrder.getProductItems().add(item);
        }
    }

    private void replaceReceipts(ServiceOrder serviceOrder, List<ServiceOrderReceiptRequest> requests) {
        serviceOrder.getReceipts().clear();
        if (requests == null) {
            return;
        }

        for (ServiceOrderReceiptRequest request : requests) {
            Receipt receipt = new Receipt();
            receipt.setServiceOrder(serviceOrder);
            receipt.setAmount(request.amount());
            receipt.setPaymentMethod(request.paymentMethod());
            receipt.setReceiptDate(request.receiptDate() != null ? request.receiptDate() : LocalDateTime.now());
            receipt.setNotes(request.notes());
            receipt.setReceivedBy(findUserByCode(request.receivedByCode(), "Received by"));
            serviceOrder.getReceipts().add(receipt);
        }
    }

    private ServiceOrderStatusHistory buildStatusHistory(ServiceOrder serviceOrder, br.com.oficinapro.ordemservico.domain.enums.ServiceOrderStatus previousStatus) {
        ServiceOrderStatusHistory history = new ServiceOrderStatusHistory();
        history.setServiceOrder(serviceOrder);
        history.setPreviousStatus(previousStatus);
        history.setNewStatus(serviceOrder.getStatus());
        history.setChangedBy(serviceOrder.getResponsibleTechnician());
        history.setNotes("Status updated");
        history.setChangedAt(LocalDateTime.now());
        return history;
    }

    private User findUserByCode(String code, String fieldName) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }

        return userRepository.findByCode(code.trim())
                .orElseThrow(() -> new ResourceNotFoundException(fieldName + " user not found with code: " + code));
    }

    private void calculateTotals(ServiceOrder serviceOrder, ServiceOrderRequest request) {
        BigDecimal totalServices = serviceOrder.getServiceItems().stream()
                .map(ServiceOrderServiceItem::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalParts = serviceOrder.getProductItems().stream()
                .map(ServiceOrderProductItem::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discount = defaultZero(request.discount());

        serviceOrder.setDiscount(discount);
        serviceOrder.setTotalServices(totalServices);
        serviceOrder.setTotalParts(totalParts);
        serviceOrder.setTotalAmount(totalServices.add(totalParts).subtract(discount).max(BigDecimal.ZERO));
    }

    private BigDecimal resolveServiceUnitPrice(ServiceOrderServiceItemRequest request, Services service) {
        return request.unitPrice() != null ? request.unitPrice() : defaultZero(service.getDefaultPrice());
    }

    private BigDecimal resolveProductUnitPrice(ServiceOrderProductItemRequest request, Product product) {
        return request.unitPrice() != null ? request.unitPrice() : defaultZero(product.getSalePrice());
    }

    private BigDecimal calculateItemTotal(BigDecimal unitPrice, Integer quantity, BigDecimal discount) {
        return unitPrice.multiply(BigDecimal.valueOf(quantity.longValue()))
                .subtract(defaultZero(discount))
                .max(BigDecimal.ZERO);
    }

    private BigDecimal defaultZero(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    private void normalizeFields(ServiceOrder serviceOrder) {
        serviceOrder.setReportedProblem(trim(serviceOrder.getReportedProblem()));
        serviceOrder.setDiagnosis(trim(serviceOrder.getDiagnosis()));
        serviceOrder.setInternalNotes(trim(serviceOrder.getInternalNotes()));
        serviceOrder.setCustomerNotes(trim(serviceOrder.getCustomerNotes()));
        serviceOrder.setFuelLevel(trim(serviceOrder.getFuelLevel()));
        serviceOrder.setAccessories(trim(serviceOrder.getAccessories()));
        serviceOrder.setApprovalNotes(trim(serviceOrder.getApprovalNotes()));
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }
}
