package ru.timur.web4_backend.dao;

import ru.timur.web4_backend.entity.PointEntity;

import java.util.List;

public interface PointDAO {
    void addPointByUserId(PointEntity point, Long userId);
    List<PointEntity> getPointsByUserId(Long userId);
}
