package ru.timur.web4_backend.service;

import ru.timur.web4_backend.dto.ImageDTO;
import ru.timur.web4_backend.dto.PointRequestDTO;
import ru.timur.web4_backend.dto.PointResponseDTO;
import ru.timur.web4_backend.dto.UserProfileDataDTO;
import ru.timur.web4_backend.exception.UserNotFoundException;

import java.util.List;

public interface UserService {
    List<PointResponseDTO> getAllPoints(Long userId);
    PointResponseDTO addPoint(PointRequestDTO point, Long userId) throws UserNotFoundException;
    void updateAvatar(ImageDTO avatar, Long userId) throws UserNotFoundException;
    void updateUsername(String username, Long userId) throws UserNotFoundException;
    void updatePassword(String password, Long userId) throws UserNotFoundException;
    UserProfileDataDTO getUserProfileData(Long userId) throws UserNotFoundException;
}
