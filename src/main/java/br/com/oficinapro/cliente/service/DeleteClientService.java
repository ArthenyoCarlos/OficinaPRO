package br.com.oficinapro.cliente.service;

import br.com.oficinapro.cliente.domain.Client;
import br.com.oficinapro.cliente.repository.ClientRepository;
import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteClientService {

    private final ClientRepository clientRepository;

    public DeleteClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(String code){
        Client client = clientRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        if(!client.isEnabled()){
            throw new BusinessException("Client already disabled");
        }

        client.setEnabled(false);
        clientRepository.save(client);
    }
}
