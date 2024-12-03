package ru.timur.web4_backend.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileDataDTO {
    private String username;
    private String avatar;
    private String avatarType;
}
