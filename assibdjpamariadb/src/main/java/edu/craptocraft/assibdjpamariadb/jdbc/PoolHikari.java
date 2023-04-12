package edu.craptocraft.assibdjpamariadb.jdbc;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PoolHikari {

    private static HikariDataSource dataSource = null;

    public static HikariDataSource getInstance() {

        if (dataSource == null){
            initDatabaseConnectionPool();
        }
        return dataSource;

    }

    private  static void initDatabaseConnectionPool() {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mariadb://localhost:3306/game");
        dataSource.setUsername("root");
        dataSource.setPassword("0909");
    }

    public static void closeDatabaseConnectionPool() throws SQLException {
        if (dataSource != null){
            dataSource.close();
        }
    }

    public static void createData(int id, String titulo, String genero, double precio) throws SQLException {
        System.out.println("\n\t>Añadiendo datos");

        Connection connection = dataSource.getConnection();

        int rowsInserted;
        try (PreparedStatement statement = connection.prepareStatement(
                """
            INSERT INTO juego(id, titulo, genero, precio)
            values (?,?,?,?)
            """
        )) {
            statement.setInt(1, id);
            statement.setString(2, titulo);
            statement.setString(3, genero);
            statement.setDouble(4, precio);

            rowsInserted = statement.executeUpdate();
        }
        System.out.println("\n\t>Filas insertadas: "+ rowsInserted);

        connection.close();
    }


    public static void readData() throws SQLException {
        System.out.println("\t>Mostrando datos\n");

        Connection connection = dataSource.getConnection();

        try(PreparedStatement statement =  connection.prepareStatement("""
            select titulo, genero, precio
            from juego
            order by precio DESC 
           """)) {

            ResultSet resultSet = statement.executeQuery();

            boolean empty = true;
            while (resultSet.next()){
                String titulo = resultSet.getString(1);
                String genero = resultSet.getString(2);
                double precio = resultSet.getInt(3);
                System.out.println("\t> título : "+ titulo + ", genero: "+ genero+ ", precio: "+ precio );
                empty = false;
            }
            if (empty){
                System.out.println(">(no data)");
            }


        }
        connection.close();
    }

    public static void updateData(int id, double precio) throws  SQLException {

        System.out.println("\n\t>Actualizando dato");

        Connection connection = dataSource.getConnection();

        try(PreparedStatement statement = connection.prepareStatement("""
            UPDATE juego 
            set precio = ?
            where id = ?
        """)) {

            statement.setDouble(1, precio);
            statement.setInt(2, id);

            int rowsUpdated = statement.executeUpdate();
            System.out.println("\t>Filas actualizadas: " + rowsUpdated);
        } finally {

            connection.close();

        }
    }

    public static void deleteData(String titulo) throws SQLException{

        Connection connection = dataSource.getConnection();

        try(PreparedStatement statement = connection.prepareStatement("""
                DELETE FROM juego
                WHERE titulo LIKE ?
        """)) {

            statement.setString(1, titulo);
            int rowsDeleted = statement.executeUpdate();
            System.out.println("\n\t>Filas eliminadas: " + rowsDeleted);
        } finally {

            connection.close();

        }
    }


}