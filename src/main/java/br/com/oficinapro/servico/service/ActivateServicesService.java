package br.com.oficinapro.servico.service;

import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.servico.domain.Services;
import br.com.oficinapro.servico.repository.ServicesRepository;
import org.springframework.stereotype.Service;

@Service
public class ActivateServicesService {

    private final ServicesRepository servicesRepository;

    public ActivateServicesService(ServicesRepository servicesRepository) {
        this.servicesRepository = servicesRepository;
    }

    public void activate(String code) {
        Services services = servicesRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with code: " + code));

        if (services.isEnabled()) {
            throw new BusinessException("Service already enabled");
        }

        services.setEnabled(true);
        servicesRepository.save(services);
    }
}
