package br.com.oficinapro.produto.repository;

import br.com.oficinapro.produto.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    Product findBySku(String sku);

    Optional<Product> findByCode(String code);

    @Query("SELECT p FROM Product p WHERE p.enabled = :enabled")
    Page<Product> findAll(@Param("enabled") boolean enabled, Pageable pageable);
}
