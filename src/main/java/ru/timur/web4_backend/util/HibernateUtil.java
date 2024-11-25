package ru.timur.web4_backend.util;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

@Slf4j
@NoArgsConstructor
@ApplicationScoped
public class HibernateUtil implements Serializable {
    private SessionFactory sessionFactory;

    @PostConstruct
    private void init() {
        Configuration configuration = new Configuration().configure("/META-INF/hibernate.cfg.xml");
        Properties properties = new Properties();
        String propertiesPath = System.getenv("DB_CONFIG_PATH");
        if (propertiesPath == null) {
            log.error("Unable to find properties path. Create DB_CONFIG_PATH system variable");
            return;
        }
        try {
            properties.load(new FileInputStream(propertiesPath));
        } catch (IOException e) {
            log.error("Error loading properties file for database", e);
            return;
        }
        configuration.addProperties(properties);
        sessionFactory = configuration.buildSessionFactory();
        log.info("SessionFactory created");
    }

    @Produces
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @PreDestroy
    public void closeSessionFactory() {
        sessionFactory.close();
    }
}

