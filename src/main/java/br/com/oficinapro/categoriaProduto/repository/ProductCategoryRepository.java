package br.com.oficinapro.categoriaProduto.repository;

import br.com.oficinapro.categoriaProduto.domain.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID> {

    ProductCategory findByName(String name);

    Optional<ProductCategory> findByCode(String code);

    @Query("SELECT p FROM ProductCategory p WHERE p.enabled = :enabled")
    Page<ProductCategory> findAll(@Param("enabled") boolean enabled, Pageable pageable);
}
