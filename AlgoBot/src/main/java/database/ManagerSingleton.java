package database;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ManagerSingleton {
    private static final ManagerSingleton instance = new ManagerSingleton();
    protected EntityManagerFactory entityManagerFactory;

    private ManagerSingleton() {
    }

    public static ManagerSingleton getInstance() {
        return instance;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null)
            createEntityManagerFactory();
        return entityManagerFactory;
    }

    public void closeEntityManagerFactory() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
            entityManagerFactory = null;
        }
    }

    protected void createEntityManagerFactory() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("testProject");
    }
}
