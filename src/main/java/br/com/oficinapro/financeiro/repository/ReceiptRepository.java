package br.com.oficinapro.financeiro.repository;

import br.com.oficinapro.financeiro.domain.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.UUID;

public interface ReceiptRepository extends JpaRepository<Receipt, UUID> {

    @Query("""
            SELECT COALESCE(SUM(r.amount), 0)
            FROM Receipt r
            WHERE r.serviceOrder.id = :serviceOrderId
            """)
    BigDecimal sumAmountByServiceOrderId(@Param("serviceOrderId") UUID serviceOrderId);

    @Query("""
            SELECT COALESCE(SUM(r.amount), 0)
            FROM Receipt r
            WHERE r.serviceOrder.id = :serviceOrderId
              AND (:receiptIdToIgnore IS NULL OR r.id <> :receiptIdToIgnore)
            """)
    BigDecimal sumAmountByServiceOrderIdIgnoringReceiptId(@Param("serviceOrderId") UUID serviceOrderId,
                                                          @Param("receiptIdToIgnore") UUID receiptIdToIgnore);
}
