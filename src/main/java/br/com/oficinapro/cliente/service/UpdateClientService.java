package br.com.oficinapro.cliente.service;

import br.com.oficinapro.cliente.domain.Client;
import br.com.oficinapro.cliente.dto.request.ClientRequest;
import br.com.oficinapro.cliente.dto.response.ClientResponse;
import br.com.oficinapro.cliente.mapper.ClientMapper;
import br.com.oficinapro.cliente.repository.ClientRepository;
import br.com.oficinapro.common.exception.ResourceConflictException;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.common.util.ValidationCpfCnpj;
import org.springframework.stereotype.Service;

@Service
public class UpdateClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public UpdateClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    public ClientResponse update(String code, ClientRequest clientRequest) {
        Client client = clientRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with code: " + code));

        updateCpfCnpj(client, clientRequest.cpfCnpj());
        updateEmail(client, clientRequest.email());
        clientMapper.updateEntity(clientRequest, client);

        Client updateClient = clientRepository.save(client);
        return clientMapper.toResponse(updateClient);
    }

    private void updateCpfCnpj(Client client, String s) {
        if (s == null || s.equals(client.getCpfCnpj())) {
            return;
        }

        Client existingClient = clientRepository.findByCpfCnpj(s);
        if (existingClient != null && !existingClient.getId().equals(client.getId())) {
            throw new ResourceConflictException("CPF/CNPJ already exists");
        }

        if(!ValidationCpfCnpj.validationCpfCnpj(s)) {
            throw new ResourceConflictException("Invalid CPF/CNPJ");
        }
        s = ValidationCpfCnpj.onlyDigits(s);

        client.setCpfCnpj(s);
    }

    private void updateEmail(Client client, String email) {
        if (email == null || email.equals(client.getEmail())) {
            return;
        }

        Client existingClient = clientRepository.findByEmail(email);
        if (existingClient != null && !existingClient.getId().equals(client.getId())) {
            throw new ResourceConflictException("Email already exists");
        }

        client.setEmail(email);
    }
}
