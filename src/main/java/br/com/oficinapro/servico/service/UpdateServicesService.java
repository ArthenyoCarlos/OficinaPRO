package br.com.oficinapro.servico.service;

import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.servico.domain.Services;
import br.com.oficinapro.servico.dto.request.ServicesRequest;
import br.com.oficinapro.servico.dto.response.ServicesResponse;
import br.com.oficinapro.servico.mapper.ServicesMapper;
import br.com.oficinapro.servico.repository.ServicesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateServicesService {

    private final ServicesRepository servicesRepository;
    private final ServicesMapper servicesMapper;

    public UpdateServicesService(ServicesRepository servicesRepository, ServicesMapper servicesMapper) {
        this.servicesRepository = servicesRepository;
        this.servicesMapper = servicesMapper;
    }

    @Transactional
    public ServicesResponse update(String code, ServicesRequest servicesRequest) {
        Services services = servicesRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with code: " + code));

        servicesMapper.updateEntity(servicesRequest, services);
        normalizeFields(services);

        Services updatedServices = servicesRepository.save(services);
        return servicesMapper.toResponse(updatedServices);
    }

    private void normalizeFields(Services services) {
        services.setName(services.getName().trim());
        services.setCategory(services.getCategory().trim());
        services.setDescription(services.getDescription() == null ? null : services.getDescription().trim());
    }
}
