package org.example.project_management.repository;

import org.example.project_management.entity.Client;
import org.example.project_management.entity.Project;
import org.example.project_management.entity.ProjectStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProjectRepositoryTest {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ClientRepository clientRepository;

    //@Mock
    private Client client;

    @BeforeEach
    public void setUp() {
        // Set up the client object as needed
        client = new Client();
        client.setId(1L);
        client.setName("Test Client");
        client.setEmail("testclient@example.com");
        client.setPhone("1234567890");
        clientRepository.save(client);
    }

    @Test
    public void testSaveProjectWithClient() {
        Client client = new Client();
        client.setName("XYZ Corp");
        client.setEmail("xyz@example.com");
        client.setPhone("123456789");

        Client savedClient = clientRepository.save(client);

        Project project = new Project();
        project.setTitle("Website Redesign");
        project.setDescription("Redesign the corporate website");
        project.setStartDate(LocalDate.now());
        project.setDeadline(LocalDate.now().plusMonths(1));
        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setClient(savedClient);

        Project savedProject = projectRepository.save(project);
        assertNotNull(savedProject.getId());
        assertEquals(savedClient.getId(), savedProject.getClient().getId());
    }

    @Test
    public void testFindProjectById() {
        Client client = new Client();
        client.setName("ABC Corp");
        client.setEmail("abc@example.com");
        client.setPhone("987654321");

        Client savedClient = clientRepository.save(client);

        Project project = new Project();
        project.setTitle("Mobile App");
        project.setDescription("Build a mobile app");
        project.setStartDate(LocalDate.now());
        project.setDeadline(LocalDate.now().plusMonths(2));
        project.setStatus(ProjectStatus.IDLE);
        project.setClient(savedClient);

        Project savedProject = projectRepository.save(project);

        Optional<Project> foundProject = projectRepository.findById(savedProject.getId());
        assertTrue(foundProject.isPresent());
        assertEquals("Mobile App", foundProject.get().getTitle());
    }

    @Test
    public void testFindAllByClientId() {
        // Save the client before assigning it to projects
        //Client savedClient = clientRepository.save(client);

        // Given: Create test projects associated with the test client
        Project project1 = new Project();
        project1.setId(1L);
        project1.setTitle("Project 1");
        project1.setClient(client);
        project1.setStatus(ProjectStatus.IN_PROGRESS);
        project1.setStartDate(LocalDate.now().minusMonths(1));
        project1.setDeadline(LocalDate.now().plusMonths(1));
        project1.setDescription("Description 1");

        Project project2 = new Project();
        project2.setId(2L);
        project2.setTitle("Project 2");
        project2.setClient(client);
        project2.setStatus(ProjectStatus.COMPLETE);
        project2.setStartDate(LocalDate.now().minusMonths(1));
        project2.setDeadline(LocalDate.now().plusMonths(1));
        project2.setDescription("Description 1");

        // Save the projects
        projectRepository.save(project1);
        projectRepository.save(project2);

        // When: Call the method to test
        List<Project> projects = projectRepository.findAllByClientId(client.getId()).get();

        // Then: Validate the results
        assertThat(projects).hasSize(2);
        //assertThat(projects).containsExactlyInAnyOrder(project1, project2);
    }
}
