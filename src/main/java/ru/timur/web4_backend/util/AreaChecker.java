package ru.timur.web4_backend.util;

import jakarta.ejb.Stateless;
import ru.timur.web4_backend.dto.PointRequestDTO;
import ru.timur.web4_backend.dto.PointResponseDTO;

import java.util.Date;

@Stateless
public class AreaChecker {
    public PointResponseDTO checkPoint(PointRequestDTO request) {
        PointResponseDTO response = PointResponseDTO
                .builder()
                .x(request.getX())
                .y(request.getY())
                .r(request.getR())
                .reqTime(new Date())
                .build();

        long startTime = System.nanoTime() / 1000;
        response.setHit(isHit(request.getX(), request.getY(), request.getR()));
        long endTime = System.nanoTime() / 1000;
        response.setProcTime(endTime - startTime);
        return response;
    }

    private boolean isHit(double x, double y, double r) {
        return x * x + y * y <= r * r && y >= 0 && x >= 0 ||
                y <= 2 * x + r && y >= 0 && x <= 0 ||
                -r <= x && x <= 0 && -r / 2 <= y && y <= 0;
    }
}
