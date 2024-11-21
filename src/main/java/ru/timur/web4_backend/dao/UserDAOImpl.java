package ru.timur.web4_backend.dao;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.timur.web4_backend.entity.UserEntity;

import java.util.Optional;

@Stateless
public class UserDAOImpl implements UserDAO {
    @Inject
    SessionFactory sessionFactory;

    @Override
    public void addUser(UserEntity user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try (session) {
            session.persist(user);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    @Override
    public Optional<UserEntity> getUserById(Long userId) {
        UserEntity user = null;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try (session) {
            user = session.createQuery(
                            "FROM UserEntity WHERE id = :id", UserEntity.class)
                    .setParameter("id", userId)
                    .uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<UserEntity> getUserByUsername(String username) {
        UserEntity user = null;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try (session) {
            user = session.createQuery(
                            "FROM UserEntity WHERE username = :username", UserEntity.class)
                    .setParameter("username", username)
                    .uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
        return Optional.ofNullable(user);
    }
}
