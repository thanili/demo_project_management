package org.example.project_management.api;

import jakarta.validation.Valid;
import org.example.project_management.dto.ClientDto;
import org.example.project_management.entity.Client;
import org.example.project_management.helper.EntityDtoConverter;
import org.example.project_management.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    private final ClientService clientService;
    private final EntityDtoConverter entityDtoConverter;

    @Autowired
    public ClientController(final ClientService clientService,
                            final EntityDtoConverter entityDtoConverter
    ) {
        this.clientService = clientService;
        this.entityDtoConverter = entityDtoConverter;
    }

    /**
     * Creates a new client.
     *
     * @param clientDto the client DTO
     * @return the created client DTO
     */
    @PostMapping
    public ResponseEntity<ClientDto> createClient(@Valid @RequestBody ClientDto clientDto) {
        logger.info("Request to create a new client received: {}", clientDto.getName());
        Client savedClient = clientService.saveClient(entityDtoConverter.convertClientDtoToClient(clientDto));
        return ResponseEntity.ok(entityDtoConverter.convertClientToClientDto(savedClient));
    }

    /**
     * Retrieves all clients.
     *
     * @return the list of client DTOs
     */
    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllClients() {
        logger.info("Request to get all clients received");
        List<Client> clients = clientService.getAllClients();
        return ResponseEntity.ok(entityDtoConverter.convertClientsToClientDtos(clients));
    }

    /**
     * Retrieves a client by ID.
     *
     * @param id the client ID
     * @return the client DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long id) {
        logger.info("Request to get client by Id received: {}", id);
        Client client = clientService.getClientById(id);
        return ResponseEntity.ok(entityDtoConverter.convertClientToClientDto(client));
    }

    /**
     * Updates an existing client.
     *
     * @param id the client ID
     * @param clientDtoDetails the client DTO with updated details
     * @return the updated client DTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> updateClient(@PathVariable Long id, @Valid @RequestBody ClientDto clientDtoDetails) {
        logger.info("Request to update an existing client received: {}", clientDtoDetails.getName());
        Client clientDetails = entityDtoConverter.convertClientDtoToClient(clientDtoDetails);
        Client updatedClient = clientService.updateClient(id, clientDetails);
        return ResponseEntity.ok(entityDtoConverter.convertClientToClientDto(updatedClient));
    }

    /**
     * Deletes a client by ID.
     *
     * @param id the client ID
     * @return a response entity with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        logger.info("Request to delete client by Id received: {}", id);
        clientService.deleteClient(id);
        return ResponseEntity.ok("Client deleted successfully");
    }
}
