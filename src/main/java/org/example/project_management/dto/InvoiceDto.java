package org.example.project_management.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceDto {
    private Long id;
    private Double amount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    private String status;
    private Long projectId;

    public InvoiceDto() {
    }

    public InvoiceDto(Long id, Double amount, LocalDate dueDate, String status, Long projectId) {
        this.id = id;
        this.amount = amount;
        this.dueDate = dueDate;
        this.status = status;
        this.projectId = projectId;
    }
}
