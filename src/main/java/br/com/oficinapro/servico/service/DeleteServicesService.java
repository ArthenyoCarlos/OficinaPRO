package br.com.oficinapro.servico.service;

import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.servico.domain.Services;
import br.com.oficinapro.servico.repository.ServicesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteServicesService {

    private final ServicesRepository servicesRepository;

    public DeleteServicesService(ServicesRepository servicesRepository) {
        this.servicesRepository = servicesRepository;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(String code) {
        Services services = servicesRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        if (!services.isEnabled()) {
            throw new BusinessException("Service already disabled");
        }

        services.setEnabled(false);
        servicesRepository.save(services);
    }
}
