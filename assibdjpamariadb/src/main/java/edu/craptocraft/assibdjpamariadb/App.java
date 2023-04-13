package edu.craptocraft.assibdjpamariadb;

import com.zaxxer.hikari.HikariDataSource;
import edu.craptocraft.assibdjpamariadb.jdbc.PoolHikari;
import edu.craptocraft.assibdjpamariadb.jpa.JpaService;
import edu.craptocraft.assibdjpamariadb.jpa.Users;

import java.sql.SQLException;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App {

    private static JpaService jpaService;

    public static void main(String[] args) throws SQLException {
        System.out.println("Programando con JDBC y JPA, que emoción!");

        System.out.println("\tJDBC en acción: ");
        // Parte JDBC
        // Inicamos conexión Pool
        PoolHikari.getInstance();

        // CRUD
        PoolHikari.readData();
        PoolHikari.createData(20, "Spider-Man: Revolutions", "Un juego de spiderman revolucionado", "2023-07-25", "PC");
        PoolHikari.readData();
        PoolHikari.updateData(20, "PlayStation 4");
        PoolHikari.readData();
        PoolHikari.deleteData(20);
        PoolHikari.readData();

        // Cerramos conexión Pool
        PoolHikari.closeDatabaseConnectionPool();

        System.out.println("\tJPA en acción: ");

        jpaService = JpaService.getInstance();

        printAllUsers(jpaService);

    }

    private static void printAllUsers(JpaService jpaService) {

        List<Users> usersList = jpaService.runInTransaction(entityManager -> entityManager.createQuery(
                "select p from Users p", Users.class).getResultList());

        usersList.stream()
                .map(user -> user.getUsername() + ":" + user.getEmail())
                .forEach(System.out::println);

    }

}
