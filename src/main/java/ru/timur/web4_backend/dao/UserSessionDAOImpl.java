package ru.timur.web4_backend.dao;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
    public void deleteSessionByAccessToken(String token) {
        UserSessionEntity userSessionEntity = getSessionByAccessToken(token).orElse(null);
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
    public void deleteSessionByRefreshToken(String token) {
        UserSessionEntity userSessionEntity = getSessionByRefreshToken(token).orElse(null);
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
    public Optional<UserSessionEntity> getSessionByAccessToken(String token) {
        UserSessionEntity userSessionEntity = null;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try (session) {
            userSessionEntity = session.createQuery(
                    "FROM UserSessionEntity WHERE accessToken = :token", UserSessionEntity.class)
                    .setParameter("token", token)
                    .uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return Optional.ofNullable(userSessionEntity);
    }

    @Override
    public Optional<UserSessionEntity> getSessionByRefreshToken(String token) {
        UserSessionEntity userSessionEntity = null;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try (session) {
            userSessionEntity = session.createQuery(
                            "FROM UserSessionEntity WHERE refreshToken = :token", UserSessionEntity.class)
                    .setParameter("token", token)
                    .uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return Optional.ofNullable(userSessionEntity);
    }
}
