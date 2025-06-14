package com.pluralsight;

import com.pluralsight.data.FDao;
import com.pluralsight.model.Film;
import org.apache.commons.dbcp2.BasicDataSource;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String username = args[0];
        String password = args[1];

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/sakila");
        dataSource.setUsername(username);
        dataSource.setPassword(password);


        displayAllFilmsScreen(dataSource);
        displayFilmSearchScreen(dataSource);
        displayCreateFilmScreen(dataSource);
        displayUpdateFilmScreen(dataSource);
        displayDeleteFilmScreen(dataSource);

    }


    private static void displayCreateFilmScreen(BasicDataSource dataSource) {
        Film film = new Film(0, "SPIDER-MAN: NO WAY HOME", 2021);

        FDao filmDAO = new FDao(dataSource);
        film = filmDAO.create(film);

        System.out.printf("%-4d %-40s %10d%n", film.getFilmId(), film.getTitle(), film.getReleaseYear());

    }

    private static void displayUpdateFilmScreen(BasicDataSource dataSource) {
        Film film = new Film(1004, "SUPER/MAN", 2024);

        FDao filmDAO = new FDao(dataSource);
        film = filmDAO.update(film.getFilmId(), film);

        System.out.printf("%-4d %-40s %10d%n", film.getFilmId(), film.getTitle(), film.getReleaseYear());

    }

    private static void displayDeleteFilmScreen(BasicDataSource dataSource) {

        FDao filmDAO = new FDao(dataSource);
        filmDAO.delete(1005);

        System.out.print("Successfully deleted film.");

    }

    private static void displayAllFilmsScreen(BasicDataSource dataSource) {
        FDao filmDAO = new FDao(dataSource);
        List<Film> films = filmDAO.getAll();

        //print header row
        System.out.printf("%-4s %-40s %10s%n", "Id", "Title", "Release Year");
        System.out.println("_________________________________________________________________________________");

        for (Film film:films){
            System.out.printf("%-4d %-40s %10d%n", film.getFilmId(), film.getTitle(), film.getReleaseYear());
        }
    }

    private static void displayFilmSearchScreen(BasicDataSource dataSource) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Search for films that start with: ");
        String searchTerm = scanner.nextLine() + "%";

        FDao filmDAO = new FDao(dataSource);
        List<Film> films = filmDAO.search(searchTerm);

        //print header row
        System.out.printf("%-4s %-40s %10s%n", "Id", "Title", "Release Year");
        System.out.println("_________________________________________________________________________________");

        for (Film film:films){
            System.out.printf("%-4d %-40s %10d%n", film.getFilmId(), film.getTitle(), film.getReleaseYear());
        }
    }
}
