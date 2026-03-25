package br.com.oficinapro.financeiro.repository;

import br.com.oficinapro.financeiro.domain.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReceiptRepository extends JpaRepository<Receipt, UUID> {
}
