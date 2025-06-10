package com.pluralsight;

import java.sql.*;
public class Main {
    public static void main(String[] args) throws SQLException,
            ClassNotFoundException {
        if (args.length != 2) {
            System.out.println(
                    "Application needs two arguments to run: " +
                            "java com.pluralsight.UsingDriverManager <username> <password>");
            System.exit(1);
        }
// get the user name and password from the command line args
        String username = args[0];
        String password = args[1];
// load the driver
        Class.forName("com.mysql.cj.jdbc.Driver");
// create the connection and prepared statement
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/car_dealership", username, password);
        PreparedStatement preparedStatement =
                connection.prepareStatement(
                        "SELECT FirstName, LastName FROM customers " +
                                "WHERE LastName LIKE ? ORDER BY FirstName");
// set the parameters for the prepared statement
        preparedStatement.setString(1, "%S%");
// execute the query
        ResultSet resultSet = preparedStatement.executeQuery();
// loop thru the results
        while (resultSet.next()) {
// process the data
            System.out.printf("first_name = %s, last_name = %s;\n",
                    resultSet.getString(1), resultSet.getString(2));
        }
// close the resources
        resultSet.close();
        preparedStatement.close();
        connection.close();
    }
}
