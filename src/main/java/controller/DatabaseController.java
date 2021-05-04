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

    public void createUserFav() throws SQLException {
        String createFav = "USE movieCollection;" +
                "CREATE TABLE favouriteVideo(" +
                "userId int FOREIGN KEY REFERENCES account(userId)," +
                "videoId int FOREIGN KEY REFERENCES movie(movieId)" +
                ");";
        Statement s = connection.createStatement();
        int Result = s.executeUpdate(createFav);
    }

    public void createMovie() throws SQLException {
        String createMovie = "USE movieCollection;" +
                "CREATE TABLE movie(" +
                "movieId int IDENTITY(1,1) PRIMARY KEY," +
                "title varchar(50), " +
                "country varchar(50), " +
                "genre varchar(50), " +
                "director varchar(50), " +
                "cover varbinary(MAX), " +
                "premiereDate date, " +
                "description varchar(500), " +
                "avgRate float, " +
                "ageRestriction int, " +
                "timeDuration int" +
                ");";
        Statement s = connection.createStatement();
        int Result = s.executeUpdate(createMovie);
    }

    public void createSeries() throws SQLException {
        String createSeries = "USE movieCollection;" +
                "CREATE TABLE series(" +
                "seriesId int IDENTITY(1,1) PRIMARY KEY," +
                "title varchar(50), " +
                "country varchar(50), " +
                "genre varchar(50), " +
                "director varchar(50), " +
                "cover varbinary(MAX), " +
                "premiereDate date, " +
                "description varchar(500), " +
                "avgRate float, " +
                "ageRestriction int, " +
                "numberOfSeasons int" +
                ");";
        Statement s = connection.createStatement();
        int Result = s.executeUpdate(createSeries);
        createSeason();
        createEpisode();
    }

    public void createSeason() throws SQLException {
        String createSeason = "USE movieCollection;" +
                "CREATE TABLE season(" +
                "seasonId int IDENTITY(1,1) PRIMARY KEY," +
                "number int," +
                "numberOfEpisodes int," +
                "seriesId int FOREIGN KEY REFERENCES series(seriesId)" +
                ");";
        Statement s = connection.createStatement();
        int Result = s.executeUpdate(createSeason);
    }

    public void createEpisode() throws SQLException {
        String createEpisode = "USE movieCollection;" +
                "CREATE TABLE episode(" +
                "episodeId int IDENTITY(1,1) PRIMARY KEY," +
                "title varchar(70)," +
                "seasonId int FOREIGN KEY REFERENCES season(seasonId)," +
                "number int," +
                "timeDuration int" +
                ");";
        Statement s = connection.createStatement();
        int Result = s.executeUpdate(createEpisode);
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
