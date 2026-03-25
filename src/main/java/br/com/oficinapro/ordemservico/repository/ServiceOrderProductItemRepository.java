package br.com.oficinapro.ordemservico.repository;

import br.com.oficinapro.ordemservico.domain.ServiceOrderProductItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ServiceOrderProductItemRepository extends JpaRepository<ServiceOrderProductItem, UUID> {
}
