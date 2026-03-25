package br.com.oficinapro.estoque.repository;

import br.com.oficinapro.estoque.domain.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StockMovementRepository extends JpaRepository<StockMovement, UUID> {
}
