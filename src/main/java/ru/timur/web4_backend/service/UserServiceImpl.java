package ru.timur.web4_backend.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import ru.timur.web4_backend.dao.PointDAO;
import ru.timur.web4_backend.dao.UserDAO;
import ru.timur.web4_backend.dto.PointRequestDTO;
import ru.timur.web4_backend.dto.PointResponseDTO;
import ru.timur.web4_backend.entity.PointEntity;
import ru.timur.web4_backend.entity.UserEntity;
import ru.timur.web4_backend.exception.UserNotFoundException;
import ru.timur.web4_backend.util.AreaChecker;

import java.util.List;

@Stateless
public class UserServiceImpl implements UserService {
    @EJB
    private PointDAO pointDAO;
    @EJB
    private AreaChecker areaChecker;
    @EJB
    private UserDAO userDAO;

    @Override
    public List<PointResponseDTO> getAllPoints(Long userId) {
        return pointDAO.getPointsByUserId(userId).stream().map(PointResponseDTO::new).toList();
    }

    @Override
    public PointResponseDTO addPoint(PointRequestDTO point, Long userId) throws UserNotFoundException {
        UserEntity user = userDAO
                .getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + "does not exists"));
        PointResponseDTO response = areaChecker.checkPoint(point);
        PointEntity pointEntity = PointEntity
                .builder()
                .x(response.getX())
                .y(response.getY())
                .r(response.getR())
                .hit(response.isHit())
                .reqTime(response.getReqTime())
                .procTime(response.getProcTime())
                .user(user)
                .build();
        pointDAO.addPoint(pointEntity);
        return response;
    }
}
