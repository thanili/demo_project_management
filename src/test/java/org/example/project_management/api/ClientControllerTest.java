package org.example.project_management.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.project_management.dto.ClientDto;
import org.example.project_management.entity.Client;
import org.example.project_management.helper.EntityDtoConverter;
import org.example.project_management.security.JwtUtils;
import org.example.project_management.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * The ClientControllerTest class is a test class that tests the ClientController class.
 * Tests the entire flow from controller to service and repository.
 * Mocks the ClientService and focus on testing the RESTful endpoints of the ClientController.
 * Uses MockMvc to simulate HTTP requests and responses.
 */
@WebMvcTest(controllers = ClientController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;
    @MockBean
    private EntityDtoConverter entityDtoConverter;

    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    UserDetailsService userDetailsService;

    private Client client;
    private ClientDto clientDto;

    @BeforeEach
    public void setUp() {
        client = new Client();
        client.setId(1L);
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");
        client.setPhone("1234567890");

        clientDto =
                new ClientDto(1L,
                        "John Doe",
                        "john.doe@example.com",
                        "1234567890",
                        new ArrayList<>());
    }

    @Test
    public void testCreateClient() throws Exception {
        when(clientService.saveClient(any(Client.class))).thenReturn(client);
        when(entityDtoConverter.convertClientToClientDto(any(Client.class))).thenReturn(clientDto);
        when(entityDtoConverter.convertClientDtoToClient(any(ClientDto.class))).thenReturn(client);

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(clientDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    public void testGetAllClients() throws Exception {
        when(clientService.getAllClients()).thenReturn(Arrays.asList(client));
        when(entityDtoConverter.convertClientsToClientDtos(anyList())).thenReturn(Arrays.asList(clientDto));

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    public void testGetClientById() throws Exception {
        when(clientService.getClientById(1L)).thenReturn(client);
        when(entityDtoConverter.convertClientToClientDto(client)).thenReturn(clientDto);

        mockMvc.perform(get("/api/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    public void testUpdateClient() throws Exception {
        when(clientService.updateClient(anyLong(), any(Client.class))).thenReturn(client);
        when(entityDtoConverter.convertClientToClientDto(client)).thenReturn(clientDto);
        when(entityDtoConverter.convertClientDtoToClient(clientDto)).thenReturn(client);

        mockMvc.perform(put("/api/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(clientDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    public void testDeleteClient() throws Exception {
        doNothing().when(clientService).deleteClient(1L);

        mockMvc.perform(delete("/api/clients/1"))
                .andExpect(status().isOk());
    }
}
