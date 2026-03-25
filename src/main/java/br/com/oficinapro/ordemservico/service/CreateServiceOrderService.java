package br.com.oficinapro.ordemservico.service;

import br.com.oficinapro.auth.domain.User;
import br.com.oficinapro.auth.reposirory.UserRepository;
import br.com.oficinapro.cliente.domain.Client;
import br.com.oficinapro.cliente.repository.ClientRepository;
import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
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
public class CreateServiceOrderService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final ServiceOrderMapper serviceOrderMapper;
    private final ClientRepository clientRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final ServicesRepository servicesRepository;
    private final ProductRepository productRepository;
    private final ServiceOrderFinancialStatusService serviceOrderFinancialStatusService;
    private final ServiceOrderReceiptValidationService serviceOrderReceiptValidationService;

    public CreateServiceOrderService(ServiceOrderRepository serviceOrderRepository,
                                     ServiceOrderMapper serviceOrderMapper,
                                     ClientRepository clientRepository,
                                     VehicleRepository vehicleRepository,
                                     UserRepository userRepository,
                                     ServicesRepository servicesRepository,
                                     ProductRepository productRepository,
                                     ServiceOrderFinancialStatusService serviceOrderFinancialStatusService,
                                     ServiceOrderReceiptValidationService serviceOrderReceiptValidationService) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.serviceOrderMapper = serviceOrderMapper;
        this.clientRepository = clientRepository;
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.servicesRepository = servicesRepository;
        this.productRepository = productRepository;
        this.serviceOrderFinancialStatusService = serviceOrderFinancialStatusService;
        this.serviceOrderReceiptValidationService = serviceOrderReceiptValidationService;
    }

    @Transactional
    public ServiceOrderResponse create(ServiceOrderRequest request) {
        validateInitialStatus(request);

        ServiceOrder serviceOrder = serviceOrderMapper.toEntity(request);
        serviceOrder.setNumber(generateNumber());
        serviceOrder.setOpenedAt(request.openedAt() != null ? request.openedAt() : LocalDateTime.now());

        assignMainReferences(serviceOrder, request);
        serviceOrder.setServiceItems(mapServiceItems(request.serviceItems(), serviceOrder));
        serviceOrder.setProductItems(mapProductItems(request.productItems(), serviceOrder));
        serviceOrder.setReceipts(mapReceipts(request.receipts(), serviceOrder));
        calculateTotals(serviceOrder, request);
        serviceOrderReceiptValidationService.validateServiceOrderReceipts(serviceOrder);
        serviceOrderFinancialStatusService.recalculate(serviceOrder);
        serviceOrder.setStatusHistory(buildInitialStatusHistory(serviceOrder));

        ServiceOrder saved = serviceOrderRepository.save(serviceOrder);
        return new ServiceOrderResponse(saved);
    }

    private void validateInitialStatus(ServiceOrderRequest request) {
        if (request.status() != br.com.oficinapro.ordemservico.domain.enums.ServiceOrderStatus.OPEN) {
            throw new BusinessException("Service order must be created with status OPEN");
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

    private List<ServiceOrderServiceItem> mapServiceItems(List<ServiceOrderServiceItemRequest> requests, ServiceOrder serviceOrder) {
        List<ServiceOrderServiceItem> items = new ArrayList<>();
        if (requests == null) {
            return items;
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
            items.add(item);
        }

        return items;
    }

    private List<ServiceOrderProductItem> mapProductItems(List<ServiceOrderProductItemRequest> requests, ServiceOrder serviceOrder) {
        List<ServiceOrderProductItem> items = new ArrayList<>();
        if (requests == null) {
            return items;
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
            items.add(item);
        }

        return items;
    }

    private List<Receipt> mapReceipts(List<ServiceOrderReceiptRequest> requests, ServiceOrder serviceOrder) {
        List<Receipt> receipts = new ArrayList<>();
        if (requests == null) {
            return receipts;
        }

        for (ServiceOrderReceiptRequest request : requests) {
            Receipt receipt = new Receipt();
            receipt.setServiceOrder(serviceOrder);
            receipt.setAmount(request.amount());
            receipt.setPaymentMethod(request.paymentMethod());
            receipt.setReceiptDate(request.receiptDate() != null ? request.receiptDate() : LocalDateTime.now());
            receipt.setNotes(request.notes());
            receipt.setReceivedBy(findUserByCode(request.receivedByCode(), "Received by"));
            receipts.add(receipt);
        }

        return receipts;
    }

    private List<ServiceOrderStatusHistory> buildInitialStatusHistory(ServiceOrder serviceOrder) {
        ServiceOrderStatusHistory history = new ServiceOrderStatusHistory();
        history.setServiceOrder(serviceOrder);
        history.setPreviousStatus(null);
        history.setNewStatus(serviceOrder.getStatus());
        history.setChangedBy(serviceOrder.getResponsibleTechnician());
        history.setNotes("Initial status");
        history.setChangedAt(LocalDateTime.now());
        return List.of(history);
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

    private String generateNumber() {
        return "OS-" + System.currentTimeMillis();
    }
}
