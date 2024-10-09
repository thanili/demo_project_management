package org.example.project_management.service;

import org.example.project_management.entity.Client;
import org.example.project_management.exception.ClientNotFoundException;
import org.example.project_management.repository.ClientRepository;
import org.example.project_management.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClientServiceImplTest {
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client client;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        client = new Client();
        client.setId(1L);
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");
        client.setPhone("1234567890");
    }

    @Test
    public void testSaveClient() {
        when(clientRepository.save(client)).thenReturn(client);

        Client savedClient = clientService.saveClient(client);

        assertNotNull(savedClient);
        assertEquals("John Doe", savedClient.getName());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    public void testGetAllClients() {
        when(clientRepository.findAll()).thenReturn(Arrays.asList(client));

        var clients = clientService.getAllClients();

        assertEquals(1, clients.size());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    public void testGetClientById_Success() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        Client foundClient = clientService.getClientById(1L);

        assertNotNull(foundClient);
        assertEquals(1L, foundClient.getId());
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetClientById_NotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.getClientById(1L));
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateClient() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(client)).thenReturn(client);

        Client updatedClient = clientService.updateClient(1L, client);

        assertNotNull(updatedClient);
        assertEquals("John Doe", updatedClient.getName());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    public void testDeleteClient() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        clientService.deleteClient(1L);

        verify(clientRepository, times(1)).delete(client);
    }
}
