package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/campusdb",
                "root",
                "surya12@"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}