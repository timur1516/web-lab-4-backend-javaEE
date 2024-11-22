package ru.timur.web4_backend.dao;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.timur.web4_backend.entity.PointEntity;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class PointDAOImpl implements PointDAO {
    @Inject
    SessionFactory sessionFactory;

    @Override
    public void addPoint(PointEntity point) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try (session) {
            session.persist(point);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    @Override
    public List<PointEntity> getPointsByUserId(Long userId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<PointEntity> points = new ArrayList<>();
        try (session) {
            points = session.createQuery("from PointEntity ", PointEntity.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return points;
    }
}
