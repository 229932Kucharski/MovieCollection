package model.dao;

import model.movie.Comment;
import model.movie.Movie;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserRates implements AutoCloseable{

    private final String url = "jdbc:sqlserver://localhost;databaseName=movieCollection";
    private final String username = "sa";
    private final String password = "qwerty";
    private final Connection connection;

    public JdbcUserRates() throws SQLException {
        connection = prepareConnection();
    }

    private Connection prepareConnection() throws SQLException{
        Connection connection = null;
        connection = DriverManager.getConnection(url, username, password);
        return connection;
    }

    public void add(int userId, int movieId, int rate) throws SQLException {
        String addRate ="insert into userRates(userId, videoId, rate) VALUES "
                + "(?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(addRate)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, movieId);
            preparedStatement.setInt(3, rate);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public void delete(int userId, int movieId) throws SQLException {
        String deleteRate ="DELETE FROM userRates WHERE userid=? AND videoId=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteRate)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, movieId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public void deleteOfUser(int userId) throws SQLException {
        String deleteRates ="DELETE FROM userRates WHERE userid=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteRates)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public void deleteForMovie(int movieId) throws SQLException {
        String deleteRates ="DELETE FROM userRates WHERE videoId=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteRates)) {
            preparedStatement.setInt(1, movieId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public int findRateForMovie(int userId, int movieId) throws SQLException {
        String getRate ="select * from userRates WHERE userid=? AND videoId=?";
        int rate = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(getRate)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, movieId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                rate = resultSet.getInt(3);
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return rate;
    }

    public List<Integer> getRatesForMovie(int movieId) throws SQLException {
        List<Integer> rates = new ArrayList<>();
        String getRates ="select * from userRates WHERE videoId=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(getRates)) {
            preparedStatement.setInt(1, movieId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int rate = resultSet.getInt(3);
                rates.add(rate);
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return rates;
    }



    @Override
    public void close() throws Exception {
        connection.close();
    }
}
