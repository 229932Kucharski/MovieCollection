package model.dao;

import model.movie.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserFav implements AutoCloseable{

    private final String url = "jdbc:sqlserver://localhost;databaseName=movieCollection";
    private final String username = "sa";
    private final String password = "qwerty";
    private final Connection connection;

    public JdbcUserFav() throws SQLException {
        connection = prepareConnection();
    }

    private Connection prepareConnection() throws SQLException{
        Connection connection = null;
        connection = DriverManager.getConnection(url, username, password);
        return connection;
    }

    public void add(int userId, int movieId) throws SQLException {
        String addComment ="insert into favouriteVideo(userId, videoId) VALUES "
                + "(?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(addComment)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, movieId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public void delete(int userId, int movieId) throws SQLException {
        String deleteFav ="DELETE FROM favouriteVideo WHERE userid=? AND videoId=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteFav)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, movieId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public void deleteOfUser(int userId) throws SQLException {
        String deleteFav ="DELETE FROM favouriteVideo WHERE userid=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteFav)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public void deleteForMovie(int movieId) throws SQLException {
        String deleteFav ="DELETE FROM favouriteVideo WHERE videoId=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteFav)) {
            preparedStatement.setInt(1, movieId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public boolean isFavVideo(int userId, int movieId) throws SQLException {
        String isFav ="SELECT * FROM favouriteVideo WHERE userid=? AND videoId=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(isFav)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, movieId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return false;
    }

    public List<Movie> getAllFavVideo(int userId) throws SQLException {
        List<Movie> movies = new ArrayList<>();
        String getFav ="SELECT * FROM favouriteVideo WHERE userid=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(getFav)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int movieId = resultSet.getInt(2);
                JdbcMovieDao movieDao = new JdbcMovieDao();
                Movie movie = movieDao.findById(movieId);
                movies.add(movie);
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return movies;
    }



    @Override
    public void close() throws Exception {
        connection.close();
    }
}
