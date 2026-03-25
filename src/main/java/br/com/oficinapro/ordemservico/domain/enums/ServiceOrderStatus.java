package br.com.oficinapro.ordemservico.domain.enums;

import java.util.EnumSet;
import java.util.Set;

public enum ServiceOrderStatus {
    OPEN,
    IN_PROGRESS,
    WAITING_APPROVAL,
    APPROVED,
    WAITING_PARTS,
    FINISHED,
    DELIVERED,
    CANCELED;

    public Set<ServiceOrderStatus> allowedNextStatuses() {
        return switch (this) {
            case OPEN -> EnumSet.of(IN_PROGRESS, WAITING_APPROVAL, CANCELED);
            case IN_PROGRESS -> EnumSet.of(WAITING_APPROVAL, WAITING_PARTS, FINISHED, CANCELED);
            case WAITING_APPROVAL -> EnumSet.of(APPROVED, CANCELED);
            case APPROVED -> EnumSet.of(IN_PROGRESS, WAITING_PARTS, FINISHED, CANCELED);
            case WAITING_PARTS -> EnumSet.of(IN_PROGRESS, CANCELED);
            case FINISHED -> EnumSet.of(DELIVERED, CANCELED);
            case DELIVERED, CANCELED -> EnumSet.noneOf(ServiceOrderStatus.class);
        };
    }

    public boolean canTransitionTo(ServiceOrderStatus targetStatus) {
        return this == targetStatus || allowedNextStatuses().contains(targetStatus);
    }
}
