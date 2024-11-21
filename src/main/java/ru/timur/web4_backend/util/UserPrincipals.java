package ru.timur.web4_backend.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.security.Principal;

@Getter
@AllArgsConstructor
public class UserPrincipals implements Principal {
    private final String username;
    private final Long userId;

    @Override
    public String getName() {
        return username;
    }
}
