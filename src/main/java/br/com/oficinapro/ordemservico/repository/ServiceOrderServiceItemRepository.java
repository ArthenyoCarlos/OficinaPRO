package br.com.oficinapro.ordemservico.repository;

import br.com.oficinapro.ordemservico.domain.ServiceOrderServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ServiceOrderServiceItemRepository extends JpaRepository<ServiceOrderServiceItem, UUID> {
}
