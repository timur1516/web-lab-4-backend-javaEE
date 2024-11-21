package ru.timur.web4_backend.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PointResponseDTO implements Serializable {
    private double x;
    private double y;
    private double r;
    private boolean hit;
    private Date reqTime;
    private long procTime;
}
