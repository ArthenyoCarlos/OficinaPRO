package br.com.oficinapro.fornecedor.repository;

import br.com.oficinapro.fornecedor.domain.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SupplierRepository extends JpaRepository<Supplier, UUID> {

    Supplier findByCnpj(String cnpj);

    Supplier findByEmail(String email);

    Optional<Supplier> findByCode(String code);

    @Query("SELECT s FROM Supplier s WHERE s.enabled = :enabled")
    Page<Supplier> findAll(@Param("enabled") boolean enabled, Pageable pageable);
}
