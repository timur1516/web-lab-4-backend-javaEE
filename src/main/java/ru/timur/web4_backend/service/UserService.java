package ru.timur.web4_backend.service;

import ru.timur.web4_backend.dto.PointRequestDTO;
import ru.timur.web4_backend.dto.PointResponseDTO;
import ru.timur.web4_backend.entity.PointEntity;
import ru.timur.web4_backend.exception.UserNotFoundException;

import java.util.List;

public interface UserService {
    List<PointResponseDTO> getAllPoints(Long userId);
    PointResponseDTO addPoint(PointRequestDTO point, Long userId) throws UserNotFoundException;
}
