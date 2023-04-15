package edu.craptocraft.assibdjpamariadb.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.function.Function;
import java.util.List;

public class JpaService {

    private static JpaService instance;

    private EntityManagerFactory entityManagerFactory;

    private JpaService() {
        entityManagerFactory = Persistence.createEntityManagerFactory("mariadb-connection");
    }

    public static JpaService getInstance() {

        instance = (instance == null) ? new JpaService() : instance;
        return instance;
    }

    public void shutdown() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    public <T> T runInTransaction(Function<EntityManager, T> function) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        boolean success = false;

        try {
            T returnValue = function.apply(entityManager);
            success = true;
            return returnValue;

        } finally {

            if (success) {
                transaction.commit();
            } else {
                transaction.rollback();
            }

        }
    }

    public List<Data> readData(String table) {

        return JpaService.getInstance().runInTransaction(entityManager -> entityManager.createQuery(
                "select p from " + table + " p", Data.class).getResultList());
    }

    public void printData(List<Data> data) {

        data.stream()
                .forEach(Data::print);

    }

    public void createData(Data data) {

        data.createData();

    }

    public void updateData() {

    }

    public void deleteData() {

    }

}
