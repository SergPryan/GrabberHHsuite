

import java.sql.*;
import java.util.List;

public class DatabaseManager {
    private static final String insertSQL = "insert into vacancy (id, name, dateOfPublication, organization, salary) values (?,?,?,?,?);";
    private static final String URL = "jdbc:mysql://localhost:3306/mydbtest?autoReconnect=true&useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private static Connection getConnection() {
        Connection connection = null;
        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void createRecord(List<Vacancy> list) {
        Connection connection = getConnection();
        try {

            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            for (Vacancy vacancy : list) {
                int slash = vacancy.getUrl().lastIndexOf("/") + 1;
                preparedStatement.setInt(1, Integer.valueOf(vacancy.getUrl().substring(slash)));
                preparedStatement.setString(2, vacancy.getName());
                preparedStatement.setString(3, vacancy.getDateOfPublication());
                preparedStatement.setString(4, vacancy.getOrganization());
                preparedStatement.setString(5, vacancy.getSalary());
                try {
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    preparedStatement.close();
                    return;
                }
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
