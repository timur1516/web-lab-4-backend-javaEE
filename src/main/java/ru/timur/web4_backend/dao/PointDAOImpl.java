package ru.timur.web4_backend.dao;

import jakarta.ejb.Stateless;
import ru.timur.web4_backend.entity.PointEntity;

import java.util.List;

@Stateless
public class PointDAOImpl implements PointDAO {
    @Override
    public void addPointByUserId(PointEntity point, Long userId) {
    }

    @Override
    public List<PointEntity> getPointsByUserId(Long userId) {
        return List.of();
    }
}
