package br.com.oficinapro.servico.service;

import br.com.oficinapro.servico.domain.Services;
import br.com.oficinapro.servico.dto.response.ServicesResponse;
import br.com.oficinapro.servico.repository.ServicesRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindAllServicesService {

    private final ServicesRepository servicesRepository;

    public FindAllServicesService(ServicesRepository servicesRepository) {
        this.servicesRepository = servicesRepository;
    }

    @Transactional(readOnly = true)
    public Page<ServicesResponse> findAll(boolean enabled, Pageable pageable) {
        Page<Services> services = servicesRepository.findAll(enabled, pageable);
        return services.map(ServicesResponse::new);
    }
}
