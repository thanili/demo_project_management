package org.example.project_management.dto;

import lombok.Getter;

@Getter
public class RefreshTokenResponse {
    private final String accessToken;

    public RefreshTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
