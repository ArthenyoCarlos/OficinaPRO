package br.com.oficinapro.ordemservico.service;

import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.ordemservico.domain.ServiceOrder;
import br.com.oficinapro.ordemservico.dto.response.ServiceOrderResponse;
import br.com.oficinapro.ordemservico.repository.ServiceOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindByServiceOrderCodeService {

    private final ServiceOrderRepository serviceOrderRepository;

    public FindByServiceOrderCodeService(ServiceOrderRepository serviceOrderRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
    }

    @Transactional(readOnly = true)
    public ServiceOrderResponse findByCode(String code) {
        ServiceOrder serviceOrder = serviceOrderRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Service order not found with code: " + code));

        return new ServiceOrderResponse(serviceOrder);
    }
}
