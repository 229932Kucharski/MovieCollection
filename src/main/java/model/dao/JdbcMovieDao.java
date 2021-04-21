package model.dao;

import model.account.user.Adult;
import model.account.user.Kid;
import model.movie.*;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JdbcMovieDao implements Dao<Movie>{

    private final String url = "jdbc:sqlserver://localhost;databaseName=movieCollection";
    private final String username = "sa";
    private final String password = "qwerty";
    private final Connection connection;

    public JdbcMovieDao() throws SQLException {
        connection = prepareConnection();
    }

    private Connection prepareConnection() throws SQLException{
        Connection connection = null;
        connection = DriverManager.getConnection(url, username, password);
        return connection;
    }



    @Override
    public void add(Movie obj) throws SQLException {

    }

    @Override
    public void update(Movie obj) throws SQLException {
        String deleteUser ="UPDATE movie SET cover = ? WHERE movieId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteUser)) {
            preparedStatement.setBytes(1, ImageConverter.imageToByteArray(obj.getId()));
            preparedStatement.setInt(2, obj.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Movie obj) throws SQLException {

    }

    @Override
    public List<Movie> findAll() throws SQLException {
        String getMovies ="select * from movie";
        List<Movie> movies = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(getMovies)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String title = resultSet.getString(2);
                String country = resultSet.getString(3);
                String genre = resultSet.getString(4);
                String director = resultSet.getString(5);
                byte[] cover = resultSet.getBytes(6);
                LocalDate premiereDate = resultSet.getDate(7).toLocalDate();
                String description = resultSet.getString(8);
                double avgRate = (double) resultSet.getFloat(9);
                int ageRestriction = resultSet.getInt(10);
                int timeDuration = resultSet.getInt(11);

                List<Comment> comments = null;
                try(JdbcCommentDao commentDao = new JdbcCommentDao()) {
                    comments = commentDao.findAllForMovie(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(timeDuration != 0) {
                    FullLengthFilm film = new FullLengthFilm(id, title, country, Genres.valueOf(genre), director,
                            cover, premiereDate, description, avgRate, ageRestriction, timeDuration);
                    film.setComments(comments);
                    movies.add(film);
                } else {
                    ShortFilm film = new ShortFilm(id, title, country, Genres.valueOf(genre), director,
                            cover, premiereDate, description, avgRate, ageRestriction, timeDuration);
                    film.setComments(comments);
                    movies.add(film);
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return movies;
    }

    @Override
    public Movie findById(int id) {
        return null;
    }

    @Override
    public Movie findByName(String name) throws SQLException {
        return null;
    }

    @Override
    public void close() throws Exception {

    }
}
