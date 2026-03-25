package br.com.oficinapro.ordemservico.repository;

import br.com.oficinapro.ordemservico.domain.ServiceOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, UUID> {

    Optional<ServiceOrder> findByCode(String code);

    Optional<ServiceOrder> findByNumber(String number);

    @EntityGraph(attributePaths = {"client", "vehicle", "responsibleTechnician"})
    @Query("SELECT so FROM ServiceOrder so WHERE so.status = :status")
    Page<ServiceOrder> findAllByStatus(@Param("status") br.com.oficinapro.ordemservico.domain.enums.ServiceOrderStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"client", "vehicle", "responsibleTechnician"})
    Page<ServiceOrder> findAll(Pageable pageable);
}
