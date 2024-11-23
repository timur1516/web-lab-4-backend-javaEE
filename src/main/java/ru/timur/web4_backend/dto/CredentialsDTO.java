package ru.timur.web4_backend.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CredentialsDTO {
    private String accessToken;
    private String refreshToken;
}
