package org.example.project_management.repository;

import org.example.project_management.entity.Client;
import org.example.project_management.exception.ClientNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ClientRepositoryTest {
    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void testSaveClient() {
        Client client = new Client();
        client.setName("XYZ Corp");
        client.setEmail("contact@xyzcorp.com");
        client.setPhone("987-654-3210");

        Client savedClient = clientRepository.save(client);
        assertNotNull(savedClient.getId());
        assertEquals("XYZ Corp", savedClient.getName());
    }

    @Test
    public void testFindClientById() {
        Client client = new Client();
        client.setName("ABC Corp");
        client.setEmail("contact@abccorp.com");
        client.setPhone("123-456-7890");

        Client savedClient = clientRepository.save(client);
        Optional<Client> foundClient = clientRepository.findById(savedClient.getId());
        assertTrue(foundClient.isPresent());
        assertEquals("ABC Corp", foundClient.get().getName());
    }

    @Test
    public void testDeleteClient() {
        Client client = new Client();
        client.setName("Delete Corp");
        client.setEmail("contact@deletecorp.com");
        client.setPhone("555-555-5555");

        Client savedClient = clientRepository.save(client);
        clientRepository.deleteById(savedClient.getId());

        Optional<Client> deletedClient = clientRepository.findById(savedClient.getId());
        assertFalse(deletedClient.isPresent());
    }

    @Test
    public void testDeleteNonExistentClient() {
        assertThrows(ClientNotFoundException.class, () -> clientRepository.deleteById(999L));
    }
}
