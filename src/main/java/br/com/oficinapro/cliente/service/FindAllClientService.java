package br.com.oficinapro.cliente.service;

import br.com.oficinapro.cliente.domain.Client;
import br.com.oficinapro.cliente.dto.response.ClientResponse;
import br.com.oficinapro.cliente.repository.ClientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindAllClientService {

    private final ClientRepository clientRepository;

    public FindAllClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional(readOnly = true)
    public Page<ClientResponse> findAll(boolean enabled, Pageable pageable){
        Page<Client> clients = clientRepository.findAll(enabled, pageable);
        return clients.map(x -> new ClientResponse(x));
    }
}
