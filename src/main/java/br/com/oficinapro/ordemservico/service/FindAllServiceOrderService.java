package br.com.oficinapro.ordemservico.service;

import br.com.oficinapro.ordemservico.domain.ServiceOrder;
import br.com.oficinapro.ordemservico.dto.response.ServiceOrderResponse;
import br.com.oficinapro.ordemservico.repository.ServiceOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindAllServiceOrderService {

    private final ServiceOrderRepository serviceOrderRepository;

    public FindAllServiceOrderService(ServiceOrderRepository serviceOrderRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
    }

    @Transactional(readOnly = true)
    public Page<ServiceOrderResponse> findAll(Pageable pageable) {
        Page<ServiceOrder> orders = serviceOrderRepository.findAll(pageable);
        return orders.map(ServiceOrderResponse::new);
    }
}
