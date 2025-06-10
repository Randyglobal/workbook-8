package com.pluralsight;

import java.sql.*;
public class App {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
//        Saving the username and password, Needs to put an event handler to check on that
        if (args.length != 2) {
            System.out.println("Invalid connection, Arguments must need 2 requirements."
                    + "Please review email and password");
            System.exit(1);

        }
//        get username and password
        String username = args[0];
        String password = args[1];

        Class.forName("com.mysql.cj.jdbc.Driver");

//        Create database connection
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind",
                    username, password);
            System.out.println("Connection Successful");
//            Create sql query
            preparedStatement = connection.prepareStatement("Select ProductId," +
                    "ProductName, " +
                    "UnitPrice, " +
                    "UnitsInStock from products order by ProductID");

//            execute statement
            resultSet = preparedStatement.executeQuery();

//            looping through results and displaying results
            while (resultSet.next()) {
                System.out.println("-------------------------------------------------------------");
                System.out.printf("%-4d %-40s %7.2f %6d%n", resultSet.getInt(1), resultSet.getString(2), resultSet.getDouble(3), resultSet.getInt(4));
            }
        } catch (SQLException e) {
            System.out.println("Invalid Connection" + e);
        } finally {
            // close the resources
            if (resultSet != null){
                try {
                    resultSet.close();
                }catch (SQLException e){
                    System.out.println("Invalid" + e);
                }

            }
            if (preparedStatement != null){
                try {
                    preparedStatement.close();

                }catch (SQLException e){
                    System.out.println("Invalid" + e);

                }
            }
            if (connection != null){
                try{
                connection.close();
            }catch (SQLException e){
                System.out.println("Invalid" + e);
            }
            }
        }

    }
}
