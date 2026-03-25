package br.com.oficinapro.servico.service;

import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.servico.domain.Services;
import br.com.oficinapro.servico.dto.response.ServicesResponse;
import br.com.oficinapro.servico.repository.ServicesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindByServicesCodeService {

    private final ServicesRepository servicesRepository;

    public FindByServicesCodeService(ServicesRepository servicesRepository) {
        this.servicesRepository = servicesRepository;
    }

    @Transactional(readOnly = true)
    public ServicesResponse findByCode(String code) {
        Services services = servicesRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with code: " + code));

        return new ServicesResponse(services);
    }
}
