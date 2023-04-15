package edu.craptocraft.assibdjpamariadb.jpa;

import jakarta.persistence.*;

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

    public List<Data> readOne(String table, int id) {

        return JpaService.getInstance().runInTransaction(entityManager -> entityManager.createQuery(
                "select p from " + table + " p where id = " + id, Data.class).getResultList());
    }

    public void printData(List<Data> data) {

        data.stream()
                .forEach(Data::print);

    }

    public void createData(Data... data) {

        EntityManager entityManager = JpaService.getInstance().entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            for (Data element : data) {

                entityManager.persist(element);
                System.out.println("\n\t > Elemento: ");
                element.print();
                System.out.println(" insertado");
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            System.err.println("Se ha producido el siguiente error " + e.getMessage());
        } finally {
            entityManager.close();
        }

    }

    public void updateData(Data data) {
        data.updateData();
    }

    public void deleteData(String table, String id) {

        int rowsDeleted = 0;
        EntityManager entityManager = JpaService.getInstance().entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            int idValue = Integer.parseInt(id);
            transaction.begin();
            Query query = entityManager.createQuery(" DELETE FROM " + table + " WHERE id = :idParam");
            query.setParameter("idParam", idValue);
            rowsDeleted = query.executeUpdate();
            transaction.commit();
            System.out.println("\n\t > Eliminadas " + rowsDeleted + " filas");
        } catch (Exception e) {
            transaction.rollback();
            System.err.println("Se ha producido el siguiente error " + e.getMessage());
        } finally {
            entityManager.close();
        }

    }

}
