package controller;

import model.movie.Comment;

import java.sql.*;
import java.time.LocalDate;

public class DatabaseController {

    private final String url = "jdbc:sqlserver://localhost";
    private final String username = "sa";
    private final String password = "qwerty";
    private final Connection connection;

    public DatabaseController() throws SQLException {
        connection = prepareConnection();
    }

    private Connection prepareConnection() throws SQLException{
        Connection connection = null;
        connection = DriverManager.getConnection(url, username, password);
        return connection;
    }

    public void createDatabase() throws SQLException {
        Statement s = connection.createStatement();
        int Result = s.executeUpdate("CREATE DATABASE movieCollection");
    }

    public void createAccount() throws SQLException {
        String createAccount = "USE movieCollection;" +
                "CREATE TABLE account(" +
                "name varchar(30) NOT NULL," +
                "password binary(100) NOT NULL," +
                "registerDate date NOT NULL," +
                "email varchar(30) NOT NULL," +
                "gender char(1) NOT NULL," +
                "birthDate date NOT NULL," +
                "phoneNumber varchar(12)," +
                "userId int IDENTITY(1,1) PRIMARY KEY," +
                "premiumUser bit NOT NULL" +
                ");";
        Statement s = connection.createStatement();
        int Result = s.executeUpdate(createAccount);
    }

    public void createMovie() throws SQLException {
        String createAccount = "USE movieCollection;" +
                "CREATE TABLE movie(" +
                "movieId int IDENTITY(1,1) PRIMARY KEY," +
                "title varchar(50), " +
                "country varchar(50), " +
                "genre varchar(50), " +
                "director varchar(50), " +
                "cover binary(200), " +
                "premiereDate date, " +
                "description varchar(500), " +
                "avgRate float, " +
                "ageRestriction int, " +
                "timeDuration int" +
                ");";
        Statement s = connection.createStatement();
        int Result = s.executeUpdate(createAccount);
    }

    public void createComment() throws SQLException {
        String createAccount = "USE movieCollection;" +
                "CREATE TABLE comment(" +
                "commentId int IDENTITY(1,1) PRIMARY KEY," +
                "userId int FOREIGN KEY REFERENCES account(userId)," +
                "videoId int FOREIGN KEY REFERENCES movie(movieId)," +
                "content varchar(250)," +
                "commentDate date" +
                ");";
        Statement s = connection.createStatement();
        int Result = s.executeUpdate(createAccount);
    }
}
