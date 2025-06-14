package com.pluralsight.data;

import com.pluralsight.model.Film;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.SQLExceptionList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FDao {
    BasicDataSource basicDataSource;

    public FDao(BasicDataSource basicDataSource) {
        this.basicDataSource = basicDataSource;
    }
    public List<Film> getAll(){
        String Sql = " select * from film";
        List<Film> films = new ArrayList<>();
        try(Connection connection = this.basicDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(Sql)){

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("film_id");
                    String title = resultSet.getString("title");
                    int releaseYear = resultSet.getInt("release_year");
                    Film film = new Film(id, title, releaseYear);
                    films.add(film);
                }
            }
        }catch(SQLException e){
            //display user friendly error message
            System.out.println("There was an error retrieving the films. Please try again or contact support.");
            //display error message for the developer
            e.printStackTrace();
        }
        return films;
    }

    public List<Film> search(String searchTerm) {
        String sql = """
                 select *\s
                 from film
                 where title like ?;
               \s""";
        List<Film> films = new ArrayList<>();

        try (
                //create a connection
                Connection connection = this.basicDataSource.getConnection();
                //create a SQL statement
                PreparedStatement preparedStatement = connection.prepareStatement(sql)

        ) {

            //execute the statement/query
            preparedStatement.setString(1, searchTerm);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                //loop through the results and put them into model objects
                while (resultSet.next()) {
                    int id = resultSet.getInt("film_id");
                    String title = resultSet.getString("title");
                    int releaseYear = resultSet.getInt("release_year");
                    Film film = new Film(id, title, releaseYear);
                    films.add(film);
                }
            }

        } catch (SQLException e) {
            //display user friendly error message
            System.out.println("There was an error retrieving the films. Please try again or contact support.");
            //display error message for the developer
            e.printStackTrace();
        }
        return films;
    }

    public Film create(Film film) {
        String sql = """
                 INSERT INTO film
                 (
                 `title`,
                 `release_year`,
                 `language_id`
                 )
                 VALUES
                 (
                  ?,
                  ?,
                  ?
                 );
                """;


        // Create the connection and prepared statement
        try (Connection connection = basicDataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Set parameters in the preparedStatement
            preparedStatement.setString(1, film.getTitle());
            preparedStatement.setInt(2, film.getReleaseYear());
            preparedStatement.setInt(3, film.getLanguageId());
            // Execute the preparedStatement
            int rows = preparedStatement.executeUpdate();
            // Display the number of rows that were updated
            System.out.printf("Rows updated %d\n", rows);
            // Get the result containing primary key(s)
            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                // Iterate through the primary keys that were generated
                while (keys.next()) {
                    int id = keys.getInt(1);
                    System.out.printf("%d key was added\n", id);
                    film.setFilmId(id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return film;
    }

    public Film update(int filmId, Film film) {
        film.setFilmId(filmId);
        String sql = """
                update film
                set
                	title = ?,
                    release_year = ?,
                    language_id = ?
                where film_id = ?;
                """;

        // Create the connection and prepared statement
        try (Connection connection = basicDataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set two parameters in the preparedStatement
            preparedStatement.setString(1, film.getTitle());
            preparedStatement.setLong(2, film.getReleaseYear());
            preparedStatement.setLong(3, film.getLanguageId());
            preparedStatement.setLong(4, filmId);

            // Execute the preparedStatement
            int rows = preparedStatement.executeUpdate();
            // Display the number of rows that were updated
            System.out.printf("Rows updated %d\n", rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return film;
    }

    public void delete(int filmId) {
        String sql = """
                delete from film
                where film_id = ?;
                """;

        // Create the connection and prepared statement
        try (Connection connection = basicDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Set parameters in the preparedStatement
            preparedStatement.setInt(1, filmId);
            // Execute the preparedStatement
            int rows = preparedStatement.executeUpdate();
            // Display the number of rows that were updated
            System.out.printf("Rows deleted %d\n", rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
