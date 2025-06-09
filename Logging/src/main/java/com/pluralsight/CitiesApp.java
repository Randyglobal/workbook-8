package com.pluralsight;

import java.sql.*;
public class CitiesApp {
    public static void main(String[] args) throws SQLException {
        // load the MySQL Driver, next line no longer needed in newer versions of Java
//         Class.forName("com.mysql.cj.jdbc.Driver");

        // 1. open a connection to the database
        // use the database URL to point to the correct database
        Connection connection;
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind", "root", "JesusisKing07@");

        // create statement
        // the statement is tied to the open connection
        Statement statement = connection.createStatement();

        // define your query
        String query = "SELECT CategoryName FROM categories ";
        System.out.println(query);
        // 2. Execute your query
        ResultSet results = statement.executeQuery(query);
        // process the results
        while (results.next()) {
            String city = results.getString("CategoryName");
            System.out.println(city);
        }

        // 3. Close resources
        results.close();
        statement.close();
        connection.close();
    }
}
