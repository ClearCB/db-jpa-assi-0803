package edu.craptocraft.assibdjpamariadb;

import edu.craptocraft.assibdjpamariadb.jdbc.PoolHikari;
import edu.craptocraft.assibdjpamariadb.jpa.models.Data;
import edu.craptocraft.assibdjpamariadb.jpa.models.Users;
import edu.craptocraft.assibdjpamariadb.jpa.services.JpaService;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App {

    private static JpaService jpaService;

    public static void main(String[] args) throws SQLException {

        // Eliminar los logs innecesarios para la aplicación
        Logger.getLogger("org.hibernate").setLevel(Level.OFF);

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

        // Creamos instancia del servicio de jpa
        jpaService = JpaService.getInstance();

        // Recuperamos los valores de Users
        List<Data> users = jpaService.readData("Users");
        jpaService.printData(users);

        System.out.println("\n\t Insertamos los usuarios de Abel Casas");

        Users newUser1 = new Users(13555, "Hello22", "pass122",
                "abelcasasabelcas.com", "2000/2/12");

        Users newUser2 = new Users(132555, "Hello232", "pass3122",
                "abelcdasasabelcas.com", "2001/2/12");

        // Creamos un nuevos usuarios y volvemos a recuperar los valores
        jpaService.createData(newUser1, newUser2);
        jpaService.printData(users);

        System.out.println("\n\t Actualizamos al usuario Abel Casas");
        // Acutalizamos el mismo usuario creado
        List<Data> user = jpaService.readOne(13555);
        jpaService.printData(user);
        newUser1.setEmail("sebastian@example.com");
        jpaService.updateData();

        // Borramos el usuario
        jpaService.deleteData(newUser1, newUser2);
        jpaService.printData(users);

    }

}
