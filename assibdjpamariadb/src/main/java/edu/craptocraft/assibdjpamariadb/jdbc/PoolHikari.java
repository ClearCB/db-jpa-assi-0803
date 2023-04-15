package edu.craptocraft.assibdjpamariadb.jdbc;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PoolHikari {

    private static HikariDataSource dataSource = null;

    private PoolHikari() {
        // Avoid problems
    };

    public static HikariDataSource getInstance() {

        if (dataSource == null) {
            initDatabaseConnectionPool();
        }
        return dataSource;

    }

    private static void initDatabaseConnectionPool() {

        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mariadb://localhost:3306/epicgames");
        dataSource.setUsername("root");
        dataSource.setPassword("0808");
    }

    public static void closeDatabaseConnectionPool() throws SQLException {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    public static void createData(int id, String name, String description, String release_date, String platform)
            throws SQLException {
        System.out.println("\n\t>Añadiendo datos");

        Connection connection = dataSource.getConnection();

        int rowsInserted;
        try (PreparedStatement statement = connection.prepareStatement(
                """
                        INSERT INTO Games(id, name, description, release_date, platform)
                        values (?,?,?,?,?)
                        """)) {
            statement.setInt(1, id);
            statement.setString(2, name);
            statement.setString(3, description);
            statement.setString(4, release_date);
            statement.setString(5, platform);

            rowsInserted = statement.executeUpdate();
        }
        System.out.println("\n\t>Filas insertadas: " + rowsInserted);

        connection.close();
    }

    public static void readData() throws SQLException {
        System.out.println("\t>Mostrando datos\n");

        Connection connection = dataSource.getConnection();

        try (PreparedStatement statement = connection.prepareStatement("""
                 select name, description, release_date, platform
                 from Games
                 order by name DESC
                """)) {

            ResultSet resultSet = statement.executeQuery();

            boolean empty = true;
            while (resultSet.next()) {
                String name = resultSet.getString(1);
                String description = resultSet.getString(2);
                String release_date = resultSet.getString(2);
                String platform = resultSet.getString(2);

                System.out.println("\t> título : " + name + ", descripción: " + description + ", fecha de salida: "
                        + release_date + " , plataforma " + platform);
                empty = false;
            }
            if (empty) {
                System.out.println(">(no data)");
            }

        }
        connection.close();
    }

    public static void updateData(int id, String platform) throws SQLException {

        System.out.println("\n\t>Actualizando dato");

        Connection connection = dataSource.getConnection();

        try (PreparedStatement statement = connection.prepareStatement("""
                    UPDATE Games
                    set platform = ?
                    where id = ?
                """)) {

            statement.setString(1, platform);
            statement.setInt(2, id);

            int rowsUpdated = statement.executeUpdate();
            System.out.println("\t>Filas actualizadas: " + rowsUpdated);
        } finally {

            connection.close();

        }
    }

    public static void deleteData(int id) throws SQLException {

        Connection connection = dataSource.getConnection();

        try (PreparedStatement statement = connection.prepareStatement("""
                        DELETE FROM Games
                        WHERE id LIKE ?
                """)) {

            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            System.out.println("\n\t>Filas eliminadas: " + rowsDeleted);
        } finally {

            connection.close();

        }
    }

}