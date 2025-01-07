package org.example.project_management.service;

import org.example.project_management.entity.Project;
import org.example.project_management.entity.ProjectStatus;
import org.example.project_management.entity.ProjectTask;
import org.example.project_management.entity.TaskStatus;
import org.example.project_management.exception.ProjectTaskNotFoundException;
import org.example.project_management.repository.ProjectTaskRepository;
import org.example.project_management.service.impl.ProjectTaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProjectTaskServiceImplTest {
    @Mock
    private ProjectTaskRepository projectTaskRepository;

    @InjectMocks
    private ProjectTaskServiceImpl projectTaskService;

    private ProjectTask projectTask;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Project project = new Project();
        project.setId(1L);
        project.setStartDate(java.time.LocalDate.now().minusMonths(1));
        project.setDeadline(java.time.LocalDate.now().plusMonths(1));
        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setTitle("Project 1");

        projectTask = new ProjectTask();
        projectTask.setId(1L);
        projectTask.setTitle("Project Task 1");
        projectTask.setStatus(TaskStatus.COMPLETE);
        projectTask.setDescription("Description 1");
        projectTask.setDueDate(java.time.LocalDate.now().plusDays(10));
        projectTask.setProject(project);
    }

    @Test
    public void testSaveProjectTask() {
        when(projectTaskRepository.save(projectTask)).thenReturn(projectTask);

        ProjectTask savedProjectTask = projectTaskService.saveProjectTask(projectTask);

        assertNotNull(savedProjectTask);
        assertEquals("Project Task 1", savedProjectTask.getTitle());
        verify(projectTaskRepository, times(1)).save(projectTask);
    }

    @Test
    public void testGetAllProjectTasks() {
        when(projectTaskRepository.findAll()).thenReturn(Arrays.asList(projectTask));

        var projectTasks = projectTaskService.getAllProjectTasks();

        assertEquals(1, projectTasks.size());
        verify(projectTaskRepository, times(1)).findAll();
    }

    @Test
    public void testGetProjectTaskById_Success() {
        when(projectTaskRepository.findById(1L)).thenReturn(Optional.of(projectTask));

        ProjectTask foundProjectTask = projectTaskService.getProjectTaskById(1L);

        assertNotNull(foundProjectTask);
        assertEquals(1L, foundProjectTask.getId());
        verify(projectTaskRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetProjectTaskById_NotFound() {
        when(projectTaskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProjectTaskNotFoundException.class, () -> projectTaskService.getProjectTaskById(1L));
        verify(projectTaskRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateProjectTask() {
        when(projectTaskRepository.findById(1L)).thenReturn(Optional.of(projectTask));
        when(projectTaskRepository.save(projectTask)).thenReturn(projectTask);

        ProjectTask updatedProjectTask = projectTaskService.updateProjectTask(1L, projectTask);

        assertNotNull(updatedProjectTask);
        assertEquals("Project Task 1", updatedProjectTask.getTitle());
        verify(projectTaskRepository, times(1)).save(projectTask);
    }

    @Test
    public void testDeleteProjectTask() {
        when(projectTaskRepository.findById(1L)).thenReturn(Optional.of(projectTask));

        projectTaskService.deleteProjectTask(1L);

        verify(projectTaskRepository, times(1)).delete(projectTask);
    }
}
