package org.example.project_management.service;

import org.example.project_management.entity.Client;

import java.util.List;

public interface ClientService {
    Client saveClient(Client client);
    List<Client> getAllClients();
    Client getClientById(Long id);
    Client updateClient(Long id, Client clientDetails);
    void deleteClient(Long id);
    void handleSubmitClient(Client client);
}
