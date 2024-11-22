package ru.timur.web4_backend.dao;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.timur.web4_backend.entity.UserSessionEntity;

import java.util.Optional;

@Stateless
public class UserSessionDAOImpl implements UserSessionDAO {
    @Inject
    private SessionFactory sessionFactory;

    @Override
    public void saveSession(UserSessionEntity userSessionEntity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try (session) {
            session.persist(userSessionEntity);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    @Override
    public void updateSession(UserSessionEntity userSessionEntity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try (session) {
            session.merge(userSessionEntity);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    @Override
    public void deleteSessionByToken(String token) {
        UserSessionEntity userSessionEntity = getSessionByToken(token).orElse(null);
        if(userSessionEntity == null) return;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try (session) {
            session.remove(userSessionEntity);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    @Override
    public Optional<UserSessionEntity> getSessionByToken(String token) {
        UserSessionEntity userSessionEntity = null;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try (session) {
            userSessionEntity = session.createQuery(
                    "FROM UserSessionEntity WHERE token = :token", UserSessionEntity.class)
                    .setParameter("token", token)
                    .uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return Optional.ofNullable(userSessionEntity);
    }
}
