package ru.timur.web4_backend.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PointRequestDTO implements Serializable {
    private double x;
    private double y;
    private double r;
}
