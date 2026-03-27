package br.com.oficinapro.ordemservico.service;

import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.ordemservico.domain.ServiceOrder;
import br.com.oficinapro.ordemservico.domain.ServiceOrderProductItem;
import br.com.oficinapro.ordemservico.domain.ServiceOrderServiceItem;
import br.com.oficinapro.ordemservico.domain.enums.ChargeType;
import br.com.oficinapro.ordemservico.dto.request.ServiceOrderRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ServiceOrderPricingService {

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    public void calculate(ServiceOrder serviceOrder, ServiceOrderRequest request) {
        validateDiscountInputs(request);

        BigDecimal subtotalServices = sumServiceItems(serviceOrder);
        BigDecimal subtotalParts = sumProductItems(serviceOrder);
        BigDecimal subtotalAmount = subtotalServices.add(subtotalParts);

        ChargeType chargeType = request.chargeType() != null ? request.chargeType() : ChargeType.FULL;
        BigDecimal chargeableServices = chargeType == ChargeType.ONLY_PRODUCTS ? BigDecimal.ZERO : subtotalServices;
        BigDecimal chargeableParts = chargeType == ChargeType.ONLY_SERVICES ? BigDecimal.ZERO : subtotalParts;

        DiscountResult servicesDiscount = resolveDiscount(
                chargeableServices,
                request.servicesDiscountAmount(),
                request.servicesDiscountPercent(),
                "services"
        );
        DiscountResult productsDiscount = resolveDiscount(
                chargeableParts,
                request.productsDiscountAmount(),
                request.productsDiscountPercent(),
                "products"
        );

        BigDecimal totalServices = chargeableServices.subtract(servicesDiscount.amount()).max(BigDecimal.ZERO);
        BigDecimal totalParts = chargeableParts.subtract(productsDiscount.amount()).max(BigDecimal.ZERO);
        BigDecimal amountBeforeTotalDiscount = totalServices.add(totalParts);

        BigDecimal requestedTotalDiscountAmount = request.totalDiscountAmount() != null
                ? request.totalDiscountAmount()
                : request.discount();

        DiscountResult totalDiscount = resolveDiscount(
                amountBeforeTotalDiscount,
                requestedTotalDiscountAmount,
                request.totalDiscountPercent(),
                "total"
        );

        serviceOrder.setChargeType(chargeType);
        serviceOrder.setSubtotalServices(subtotalServices);
        serviceOrder.setSubtotalParts(subtotalParts);
        serviceOrder.setSubtotalAmount(subtotalAmount);
        serviceOrder.setServicesDiscountAmount(servicesDiscount.amount());
        serviceOrder.setServicesDiscountPercent(servicesDiscount.percent());
        serviceOrder.setProductsDiscountAmount(productsDiscount.amount());
        serviceOrder.setProductsDiscountPercent(productsDiscount.percent());
        serviceOrder.setDiscount(totalDiscount.amount());
        serviceOrder.setTotalDiscountPercent(totalDiscount.percent());
        serviceOrder.setTotalServices(totalServices);
        serviceOrder.setTotalParts(totalParts);
        serviceOrder.setTotalAmount(amountBeforeTotalDiscount.subtract(totalDiscount.amount()).max(BigDecimal.ZERO));
    }

    private void validateDiscountInputs(ServiceOrderRequest request) {
        validateDiscountPair(request.productsDiscountAmount(), request.productsDiscountPercent(), "products");
        validateDiscountPair(request.servicesDiscountAmount(), request.servicesDiscountPercent(), "services");
        validateDiscountPair(resolveRequestedTotalDiscountAmount(request), request.totalDiscountPercent(), "total");
    }

    private BigDecimal resolveRequestedTotalDiscountAmount(ServiceOrderRequest request) {
        if (request.totalDiscountAmount() != null && request.discount() != null) {
            throw new BusinessException("Use either totalDiscountAmount or discount for total discount amount");
        }
        return request.totalDiscountAmount() != null ? request.totalDiscountAmount() : request.discount();
    }

    private void validateDiscountPair(BigDecimal amount, BigDecimal percent, String scope) {
        if (amount != null && percent != null) {
            throw new BusinessException("Inform either " + scope + " discount amount or percent, not both");
        }
    }

    private BigDecimal sumServiceItems(ServiceOrder serviceOrder) {
        return serviceOrder.getServiceItems().stream()
                .map(ServiceOrderServiceItem::getTotalAmount)
                .map(this::defaultZero)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal sumProductItems(ServiceOrder serviceOrder) {
        return serviceOrder.getProductItems().stream()
                .map(ServiceOrderProductItem::getTotalAmount)
                .map(this::defaultZero)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private DiscountResult resolveDiscount(BigDecimal baseAmount, BigDecimal requestedAmount, BigDecimal requestedPercent, String scope) {
        BigDecimal normalizedBaseAmount = defaultZero(baseAmount);

        if (normalizedBaseAmount.compareTo(BigDecimal.ZERO) == 0) {
            if ((requestedAmount != null && requestedAmount.compareTo(BigDecimal.ZERO) > 0)
                    || (requestedPercent != null && requestedPercent.compareTo(BigDecimal.ZERO) > 0)) {
                throw new BusinessException("Cannot apply " + scope + " discount when base amount is zero");
            }
            return new DiscountResult(BigDecimal.ZERO, BigDecimal.ZERO);
        }

        if (requestedPercent != null) {
            BigDecimal amount = normalizedBaseAmount.multiply(requestedPercent)
                    .divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);
            return new DiscountResult(amount, scalePercent(requestedPercent));
        }

        BigDecimal amount = defaultZero(requestedAmount);
        if (amount.compareTo(normalizedBaseAmount) > 0) {
            throw new BusinessException("The " + scope + " discount amount cannot exceed the base amount");
        }

        BigDecimal percent = amount.compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : amount.multiply(ONE_HUNDRED).divide(normalizedBaseAmount, 4, RoundingMode.HALF_UP);

        return new DiscountResult(amount, scalePercent(percent));
    }

    private BigDecimal scalePercent(BigDecimal value) {
        return defaultZero(value).setScale(4, RoundingMode.HALF_UP);
    }

    private BigDecimal defaultZero(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    private record DiscountResult(BigDecimal amount, BigDecimal percent) {
    }
}
