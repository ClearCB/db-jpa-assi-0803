package edu.craptocraft.assibdjpamariadb;

import com.zaxxer.hikari.HikariDataSource;
import edu.craptocraft.assibdjpamariadb.jdbc.PoolHikari;

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

        //Parte JDBC
        //Inicamos conexión Pool
        PoolHikari.getInstance();

        //CRUD
        PoolHikari.readData();
        PoolHikari.createData(20, "Spider-Man: Revolutions", "Aventura", 69.0);
        PoolHikari.readData();
        PoolHikari.updateData(20, 100.0);
        PoolHikari.readData();
        PoolHikari.deleteData("Spider-Man: Revolutions");
        PoolHikari.readData();

        //Cerramos conexión Pool
        PoolHikari.closeDatabaseConnectionPool();


    }
}
