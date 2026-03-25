package br.com.oficinapro.servico.service;

import br.com.oficinapro.servico.domain.Services;
import br.com.oficinapro.servico.dto.request.ServicesRequest;
import br.com.oficinapro.servico.dto.response.ServicesResponse;
import br.com.oficinapro.servico.mapper.ServicesMapper;
import br.com.oficinapro.servico.repository.ServicesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateServicesService {

    private final ServicesRepository servicesRepository;
    private final ServicesMapper servicesMapper;

    public CreateServicesService(ServicesRepository servicesRepository, ServicesMapper servicesMapper) {
        this.servicesRepository = servicesRepository;
        this.servicesMapper = servicesMapper;
    }

    @Transactional
    public ServicesResponse create(ServicesRequest servicesRequest) {
        Services services = servicesMapper.toEntity(servicesRequest);
        normalizeFields(services);
        services.setEnabled(true);

        Services savedServices = servicesRepository.save(services);
        return servicesMapper.toResponse(savedServices);
    }

    private void normalizeFields(Services services) {
        services.setName(services.getName().trim());
        services.setCategory(services.getCategory().trim());
        services.setDescription(services.getDescription() == null ? null : services.getDescription().trim());
    }
}
