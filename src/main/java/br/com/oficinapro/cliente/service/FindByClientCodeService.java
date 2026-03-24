package br.com.oficinapro.cliente.service;

import br.com.oficinapro.cliente.domain.Client;
import br.com.oficinapro.cliente.dto.response.ClientResponse;
import br.com.oficinapro.cliente.repository.ClientRepository;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindByClientCodeService {

    private final ClientRepository clientRepository;

    public FindByClientCodeService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional(readOnly = true)
    public ClientResponse findByCode(String code){
        Client client = clientRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with code: " + code));

        return new ClientResponse(client);
    }
}
