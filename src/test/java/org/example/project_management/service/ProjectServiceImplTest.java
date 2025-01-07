package org.example.project_management.service;

import org.example.project_management.entity.Client;
import org.example.project_management.entity.Project;
import org.example.project_management.entity.ProjectStatus;
import org.example.project_management.exception.ProjectNotFoundException;
import org.example.project_management.repository.ProjectRepository;
import org.example.project_management.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProjectServiceImplTest {
    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private Project project;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Client client = new Client();
        client.setId(1L);
        client.setName("Client 1");
        client.setEmail("client1@test.com");
        client.setPhone("1234567890");

        project = new Project();
        project.setId(1L);
        project.setTitle("Project 1");
        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setDeadline(java.time.LocalDate.now().plusMonths(1));
        project.setStartDate(java.time.LocalDate.now().minusMonths(1));
        project.setDescription("Description 1");
        project.setClient(client);
    }

    @Test
    public void testSaveProject() {
        when(projectRepository.save(project)).thenReturn(project);

        Project savedProject = projectService.saveProject(project);

        assertNotNull(savedProject);
        assertEquals("Project 1", savedProject.getTitle());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    public void testGetAllProjects() {
        when(projectRepository.findAll()).thenReturn(Arrays.asList(project));

        var projects = projectService.getAllProjects();

        assertEquals(1, projects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    public void testGetProjectById_Success() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Project foundProject = projectService.getProjectById(1L);

        assertNotNull(foundProject);
        assertEquals(1L, foundProject.getId());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetProjectById_NotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.getProjectById(1L));
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.save(project)).thenReturn(project);

        Project updatedProject = projectService.updateProject(1L, project);

        assertNotNull(updatedProject);
        assertEquals("Project 1", updatedProject.getTitle());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    public void testDeleteProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        projectService.deleteProject(1L);

        verify(projectRepository, times(1)).delete(project);
    }
}
