package org.example.project_management.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private List<Long> projectIds;

    public ClientDto() {
    }

    public ClientDto(Long id, String name, String email, String phone, List<Long> projectIds) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.projectIds = projectIds;
    }
}