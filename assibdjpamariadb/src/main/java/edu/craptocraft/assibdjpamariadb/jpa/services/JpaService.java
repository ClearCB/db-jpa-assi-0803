package edu.craptocraft.assibdjpamariadb.jpa.services;

import jakarta.persistence.*;

import java.util.function.Function;

import edu.craptocraft.assibdjpamariadb.jpa.models.Data;
import edu.craptocraft.assibdjpamariadb.jpa.models.Users;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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

    public static void initDB() {

        EntityManager entityManager = JpaService.getInstance().entityManagerFactory.createEntityManager();

        // Cargar el contenido del archivo en una cadena de caracteres
        String ddlScript;
        String dmlScript;
        try {
            ddlScript = new String(Files.readAllBytes(Paths.get("../../../../../../../../../scripts/DDL.sql")));
            dmlScript = new String(Files.readAllBytes(Paths.get("../../../../../../../../../scripts/DML.sql")));
        } catch (IOException e) {

            e.printStackTrace();
        } finally {

            // consulta
            Query queryDdl = entityManager.createNativeQuery(ddlScript);
            Query queryDml = entityManager.createNativeQuery(dmlScript);
            queryDdl.executeUpdate();
            queryDml.executeUpdate();
        }

        // Crear una instancia de Query y establecer el contenido del archivo en la
    }

    public <T> T runInTransaction(Function<EntityManager, T> function) {

        EntityManager entityManager = JpaService.getInstance().entityManagerFactory.createEntityManager();
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

    public List<Data> readOne(int id) {

        EntityManager entityManager = JpaService.getInstance().entityManagerFactory.createEntityManager();
        List<Data> rows = new ArrayList<Data>();

        try {
            Data row = entityManager.find(Users.class, id);
            rows.add(row);
        } catch (Exception e) {
            System.err.println("Se ha producido el siguiente error " + e.getMessage());
        } finally {
            entityManager.close();
        }

        return rows;
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

    public void updateData() {

        EntityManager entityManager = JpaService.getInstance().entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.flush();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            System.err.println("Se ha producido el siguiente error " + e.getMessage());
        } finally {
            entityManager.close();
        }

    }

    public void deleteData(Data... data) {

        int rowsDeleted = 0;
        EntityManager entityManager = JpaService.getInstance().entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            for (Data element : data) {

                System.out.println("\n\t > Elemento: ");
                element.print();
                if (!entityManager.contains(element)) {
                    element = entityManager.merge(element);
                }
                entityManager.remove(element);
                System.out.println(" eliminado");
                rowsDeleted += 1;
            }

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
