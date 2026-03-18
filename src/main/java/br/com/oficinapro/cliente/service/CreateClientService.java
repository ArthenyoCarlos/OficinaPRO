package br.com.oficinapro.cliente.service;

import br.com.oficinapro.cliente.domain.Client;
import br.com.oficinapro.cliente.dto.request.ClientRequest;
import br.com.oficinapro.cliente.dto.response.ClientResponse;
import br.com.oficinapro.cliente.mapper.ClientMapper;
import br.com.oficinapro.cliente.repository.ClientRepository;
import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.common.exception.ResourceConflictException;
import br.com.oficinapro.common.util.ValidationCpfCnpj;
import org.springframework.stereotype.Service;

@Service
public class CreateClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public CreateClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    public ClientResponse create(ClientRequest clientRequest){
        validateEmailUniqueness(clientRequest.email());
        validateCpfCnpjUniqueness(clientRequest.cpfCnpj());
        Client client = mapToClient(clientRequest);
        client.setEnabled(true);

        Client savedClient = clientRepository.save(client);
        return mapToResponse(savedClient);
    }

    private void validateEmailUniqueness(String email) {
        if (clientRepository.findByEmail(email) != null) {
            throw new ResourceConflictException("Email already exists");
        }
    }

    private void validateCpfCnpjUniqueness(String cpfCnpj) {
        if (!ValidationCpfCnpj.validationCpfCnpj(cpfCnpj)) {
            throw new BusinessException("Invalid CPF/CNPJ");
        }

        cpfCnpj = ValidationCpfCnpj.onlyDigits(cpfCnpj);

        if (clientRepository.findByCpfCnpj(cpfCnpj) != null) {
            throw new ResourceConflictException("CPF/CNPJ already exists");
        }
    }

    private Client mapToClient(ClientRequest clientRequest) {
        return clientMapper.toEntity(clientRequest);
    }

    private ClientResponse mapToResponse(Client client) {
        return clientMapper.toResponse(client);
    }
}
