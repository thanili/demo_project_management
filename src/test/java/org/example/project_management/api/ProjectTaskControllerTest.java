package org.example.project_management.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.project_management.dto.ProjectTaskDto;
import org.example.project_management.entity.ProjectTask;
import org.example.project_management.helper.EntityDtoConverter;
import org.example.project_management.security.JwtUtils;
import org.example.project_management.service.ProjectTaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProjectTaskController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class ProjectTaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectTaskService projectTaskService;

    @MockBean
    private EntityDtoConverter entityDtoConverter;

    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    UserDetailsService userDetailsService;

    @Autowired
    ObjectMapper objectMapper;

    private ProjectTask projectTask;
    private ProjectTaskDto projectTaskDto;

    @BeforeEach
    public void setUp() {
        projectTask = new ProjectTask();
        projectTask.setId(1L);
        projectTask.setTitle("Task 1");
        projectTask.setDescription("Description 1");
        projectTask.setDueDate(LocalDate.now().plusDays(10));
        projectTask.setStatus("In Progress");

        projectTaskDto =
                new ProjectTaskDto(1L,
                        "Task 1",
                        "Description 1",
                        LocalDate.now().plusDays(10),
                        "In Progress",
                        1L);
    }

    @Test
    public void testCreateProjectTask() throws Exception {
        when(projectTaskService.saveProjectTask(any(ProjectTask.class))).thenReturn(projectTask);
        when(entityDtoConverter.convertProjectTaskToProjectTaskDto(any(ProjectTask.class))).thenReturn(projectTaskDto);
        when(entityDtoConverter.convertProjectTaskDtoToProjectTask(any(ProjectTaskDto.class))).thenReturn(projectTask);

        mockMvc.perform(post("/api/project-tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectTaskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task 1"));
    }

    @Test
    public void testGetAllProjectTasks() throws Exception {
        when(projectTaskService.getAllProjectTasks()).thenReturn(Arrays.asList(projectTask));
        when(entityDtoConverter.convertProjectTasksToProjectTaskDtos(anyList())).thenReturn(Arrays.asList(projectTaskDto));

        mockMvc.perform(get("/api/project-tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Task 1"));
    }

    @Test
    public void testGetProjectTaskById() throws Exception {
        when(projectTaskService.getProjectTaskById(1L)).thenReturn(projectTask);
        when(entityDtoConverter.convertProjectTaskToProjectTaskDto(projectTask)).thenReturn(projectTaskDto);

        mockMvc.perform(get("/api/project-tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task 1"));
    }

    @Test
    public void testUpdateProjectTask() throws Exception {
        when(projectTaskService.updateProjectTask(anyLong(), any(ProjectTask.class))).thenReturn(projectTask);
        when(entityDtoConverter.convertProjectTaskToProjectTaskDto(projectTask)).thenReturn(projectTaskDto);
        when(entityDtoConverter.convertProjectTaskDtoToProjectTask(projectTaskDto)).thenReturn(projectTask);

        mockMvc.perform(put("/api/project-tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(projectTaskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task 1"));
    }

    @Test
    public void testDeleteProjectTask() throws Exception {
        doNothing().when(projectTaskService).deleteProjectTask(1L);

        mockMvc.perform(delete("/api/project-tasks/1"))
                .andExpect(status().isOk());
    }
}
