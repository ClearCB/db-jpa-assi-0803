package edu.craptocraft.assibdjpamariadb;

import com.zaxxer.hikari.HikariDataSource;
import edu.craptocraft.assibdjpamariadb.jdbc.PoolHikari;
import edu.craptocraft.assibdjpamariadb.jpa.JpaService;

import java.sql.SQLException;

/**
 * Hello world!
 *
 */
public class App 
{
    private static HikariDataSource poolHikari;
    public static void main( String[] args ) throws SQLException {
        System.out.println( "Programando con JDBC y JPA, que emoción!" );

        System.out.println("\tJDBC en acción: ");
        //Parte JDBC
        //Inicamos conexión Pool
        PoolHikari.getInstance();

        //CRUD
        PoolHikari.readData();
        PoolHikari.createData(20, "Spider-Man: Revolutions", "Un juego de spiderman revolucionado", "2023-07-25", "PC");
        PoolHikari.readData();
        PoolHikari.updateData(20, "PlayStation 4");
        PoolHikari.readData();
        PoolHikari.deleteData(20);
        PoolHikari.readData();

        //Cerramos conexión Pool
        PoolHikari.closeDatabaseConnectionPool();


        System.out.println("\tJPA en acción: ");


    }
}
