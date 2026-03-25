package br.com.oficinapro.ordemservico.repository;

import br.com.oficinapro.ordemservico.domain.ServiceOrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ServiceOrderStatusHistoryRepository extends JpaRepository<ServiceOrderStatusHistory, UUID> {
}
