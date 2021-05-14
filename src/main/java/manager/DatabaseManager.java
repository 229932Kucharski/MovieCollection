package manager;

import java.sql.*;

public class DatabaseManager implements AutoCloseable{

    private final String url = "jdbc:sqlserver://localhost";
    private final String username = "sa";
    private final String password = "qwerty";
    private final Connection connection;

    public DatabaseManager() throws SQLException {
        connection = prepareConnection();
    }

    /**
     * Method prepare connection to database
     */
    private Connection prepareConnection() throws SQLException{
        Connection connection = null;
        connection = DriverManager.getConnection(url, username, password);
        return connection;
    }

    /**
     * Create database movieCollection
     */
    public void createDatabase() throws SQLException {
        Statement s = connection.createStatement();
        int Result = s.executeUpdate("CREATE DATABASE movieCollection");
    }

    /**
     * Create table account
     */
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

    /**
     * Create table user favourite videos
     */
    public void createUserFav() throws SQLException {
        String createFav = "USE movieCollection;" +
                "CREATE TABLE favouriteVideo(" +
                "userId int FOREIGN KEY REFERENCES account(userId)," +
                "videoId int FOREIGN KEY REFERENCES movie(movieId)" +
                ");";
        Statement s = connection.createStatement();
        int Result = s.executeUpdate(createFav);
    }

    /**
     * Create table movie
     */
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

    /**
     * Create table comment
     */
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

    /**
     * Close connection
     */
    @Override
    public void close() throws Exception {
        connection.close();
    }
}
