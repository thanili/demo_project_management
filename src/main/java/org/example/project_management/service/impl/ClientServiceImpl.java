package org.example.project_management.service.impl;

import jakarta.transaction.Transactional;
import org.example.project_management.entity.Client;
import org.example.project_management.exception.ClientNotFoundException;
import org.example.project_management.repository.ClientRepository;
import org.example.project_management.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(final ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public Client saveClient(Client client) {
        logger.info("Creating a new client: {}", client.getName());
        return clientRepository.save(client);
    }

    @Override
    public List<Client> getAllClients() {
        logger.info("Getting all clients");
        return clientRepository.findAll();
    }

    @Override
    public Client getClientById(Long id) {
        logger.info("Getting a client by Id: {}", id);
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));
    }

    @Override
    @Transactional
    public Client updateClient(Long id, Client clientDetails) {
        logger.info("Updating existing client");
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        if(clientDetails.getName() != null)
            client.setName(clientDetails.getName());

        if(clientDetails.getEmail() != null)
            client.setEmail(clientDetails.getEmail());

        if(clientDetails.getPhone() != null)
            client.setPhone(clientDetails.getPhone());

        if(clientDetails.getProjects() != null) {
            client.getProjects().clear();
            client.getProjects().addAll(clientDetails.getProjects());
        } else {
            // If no projects are provided, keep the existing ones
            // client.setProjects(client.getProjects());
        }

        return clientRepository.save(client);
    }

    @Override
    @Transactional
    public void deleteClient(Long id) {
        logger.info("Deleting a client with Id: {}", id);
        Client client = getClientById(id);
        clientRepository.delete(client);
    }

    public void handleSubmitClient(Client client) {
        if(client.getId() != null) {
            Client existingClient = getClientById(client.getId());
            if (existingClient != null)
                client.setProjects(existingClient.getProjects());
        }
        saveClient(client);
    }
}
