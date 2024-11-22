package ru.timur.web4_backend.dao;

import ru.timur.web4_backend.entity.PointEntity;

import java.util.List;

public interface PointDAO {
    void addPoint(PointEntity point);
    List<PointEntity> getPointsByUserId(Long userId);
}
