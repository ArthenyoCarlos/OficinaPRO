package br.com.oficinapro.servico.repository;

import br.com.oficinapro.servico.domain.Services;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ServicesRepository extends JpaRepository<Services, UUID> {

    Optional<Services> findByCode(String code);

    @Query("SELECT s FROM Services s WHERE s.enabled = :enabled")
    Page<Services> findAll(@Param("enabled") boolean enabled, Pageable pageable);
}
