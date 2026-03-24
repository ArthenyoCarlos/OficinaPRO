package br.com.oficinapro.cliente.repository;

import br.com.oficinapro.cliente.domain.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    Client findByEmail(String email);

    Client findByCpfCnpj(String cpfCnpj);

    Optional<Client> findByCode(String code);

    @Query("SELECT c FROM Client c WHERE c.enabled = :enabled")
    Page<Client> findAll(@Param("enabled") boolean enabled, Pageable pageable);
}
