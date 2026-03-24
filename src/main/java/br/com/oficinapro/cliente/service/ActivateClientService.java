package br.com.oficinapro.cliente.service;

import br.com.oficinapro.cliente.domain.Client;
import br.com.oficinapro.cliente.repository.ClientRepository;
import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ActivateClientService {

    private final ClientRepository clientRepository;

    public ActivateClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void activateUser(String code) {
        Client client = clientRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with code: " + code));

        if(client.isEnabled()) {
            throw new BusinessException("Client already enabled");
        }

        client.setEnabled(true);
        clientRepository.save(client);
    }
}
