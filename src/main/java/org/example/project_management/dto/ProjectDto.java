package org.example.project_management.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectDto {
    private Long id;
    private String title;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate deadline;
    private String status;
    private Long clientId;
    private List<Long > taskIds;
    private List<Long > invoiceIds;

    public ProjectDto() {
    }

    public ProjectDto(Long id, String title, String description, LocalDate startDate, LocalDate deadline, String status, Long clientId, List<Long> taskIds, List<Long> invoiceIds) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.deadline = deadline;
        this.status = status;
        this.clientId = clientId;
        this.taskIds = taskIds;
        this.invoiceIds = invoiceIds;
    }
}
