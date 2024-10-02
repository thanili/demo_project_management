package org.example.project_management.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.project_management.dto.ProjectDto;
import org.example.project_management.entity.Project;
import org.example.project_management.helper.EntityDtoConverter;
import org.example.project_management.service.data.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProjectController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class ProjectControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private EntityDtoConverter entityDtoConverter;

    @Autowired
    ObjectMapper objectMapper;

    private Project project;
    private ProjectDto projectDto;

    @BeforeEach
    public void setUp() {
        project = new Project();
        project.setId(1L);
        project.setTitle("Project 1");
        project.setDescription("Description 1");
        project.setStartDate(LocalDate.now().minusMonths(1));
        project.setDeadline(LocalDate.now().plusMonths(1));
        project.setStatus("In Progress");

        projectDto =
                new ProjectDto(1L,
                        "Project 1",
                        "Description 1",
                        LocalDate.now().minusMonths(1),
                        LocalDate.now().plusMonths(1),
                        "In Progress",
                        1L,
                        new ArrayList<>(),
                        new ArrayList<>());
    }

    @Test
    public void testCreateProject() throws Exception {
        when(projectService.saveProject(any(Project.class))).thenReturn(project);
        when(entityDtoConverter.convertProjectToProjectDto(any(Project.class))).thenReturn(projectDto);
        when(entityDtoConverter.convertProjectDtoToProject(any(ProjectDto.class))).thenReturn(project);

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Project 1"));
    }

    @Test
    public void testGetAllProjects() throws Exception {
        when(projectService.getAllProjects()).thenReturn(Arrays.asList(project));
        when(entityDtoConverter.convertProjectsToProjectDtos(anyList())).thenReturn(Arrays.asList(projectDto));

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Project 1"));
    }

    @Test
    public void testGetProjectById() throws Exception {
        when(projectService.getProjectById(1L)).thenReturn(project);
        when(entityDtoConverter.convertProjectToProjectDto(project)).thenReturn(projectDto);

        mockMvc.perform(get("/api/projects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Project 1"));
    }

    @Test
    public void testUpdateProject() throws Exception {
        when(projectService.updateProject(anyLong(), any(Project.class))).thenReturn(project);
        when(entityDtoConverter.convertProjectToProjectDto(project)).thenReturn(projectDto);
        when(entityDtoConverter.convertProjectDtoToProject(projectDto)).thenReturn(project);

        mockMvc.perform(put("/api/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Project 1"));
    }

    @Test
    public void testDeleteProject() throws Exception {
        doNothing().when(projectService).deleteProject(1L);

        mockMvc.perform(delete("/api/projects/1"))
                .andExpect(status().isNoContent());
    }
}
