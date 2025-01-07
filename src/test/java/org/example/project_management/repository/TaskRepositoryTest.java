package org.example.project_management.repository;

import org.example.project_management.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class TaskRepositoryTest {
    @Autowired
    private ProjectTaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void testSaveTaskWithProject() {
        Client client = new Client();
        client.setName("Test Client");
        client.setEmail("test@example.com");
        client.setPhone("987654321");
        Client savedClient = clientRepository.save(client);

        Project project = new Project();
        project.setTitle("Backend Development");
        project.setDescription("Build the backend of the system");
        project.setStartDate(LocalDate.now());
        project.setDeadline(LocalDate.now().plusMonths(1));
        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setClient(savedClient);

        Project savedProject = projectRepository.save(project);

        ProjectTask task = new ProjectTask();
        task.setTitle("Setup Database");
        task.setDescription("Configure database for the project");
        task.setDueDate(LocalDate.now().plusWeeks(1));
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setProject(savedProject);

        ProjectTask savedTask = taskRepository.save(task);
        assertNotNull(savedTask.getId());
        assertEquals(savedProject.getId(), savedTask.getProject().getId());
    }

    @Test
    public void testFindTasksByProject() {
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

        ProjectTask task1 = new ProjectTask();
        task1.setTitle("Setup API");
        task1.setDescription("Set up the API backend");
        task1.setDueDate(LocalDate.now().plusWeeks(2));
        task1.setStatus(TaskStatus.IN_PROGRESS);
        task1.setProject(savedProject);

        ProjectTask task2 = new ProjectTask();
        task2.setTitle("Create UI Design");
        task2.setDescription("Design the user interface");
        task2.setDueDate(LocalDate.now().plusWeeks(3));
        task2.setStatus(TaskStatus.IDLE);
        task2.setProject(savedProject);

        taskRepository.saveAll(List.of(task1, task2));

        List<ProjectTask> tasks = taskRepository.findAll();
        assertEquals(2, tasks.size());
    }
}
