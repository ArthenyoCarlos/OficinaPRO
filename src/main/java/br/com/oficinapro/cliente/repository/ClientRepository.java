package br.com.oficinapro.cliente.repository;

import br.com.oficinapro.cliente.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    Client findByEmail(String email);

    Client findByCpfCnpj(String cpfCnpj);

    Optional<Client> findByCode(String code);
}
