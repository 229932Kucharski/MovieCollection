package model.dao;

import model.movie.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcCommentDao implements Dao<Comment> {

    private final String url = "jdbc:sqlserver://localhost;databaseName=movieCollection";
    private final String username = "sa";
    private final String password = "qwerty";
    private final Connection connection;

    public JdbcCommentDao() throws SQLException {
        connection = prepareConnection();
    }

    private Connection prepareConnection() throws SQLException{
        Connection connection = null;
        connection = DriverManager.getConnection(url, username, password);
        return connection;
    }


    @Override
    public void add(Comment obj) throws SQLException {
        String addComment ="insert into comment(userId, videoId, content, commentDate) VALUES "
                + "(?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(addComment)) {
            preparedStatement.setInt(1, obj.getUserId());
            preparedStatement.setInt(2, obj.getVideoId());
            preparedStatement.setString(3, obj.getContent());
            preparedStatement.setDate(4, Date.valueOf(obj.getCommentDate()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void update(Comment obj) {

    }

    @Override
    public void delete(Comment obj) throws SQLException {
        String deleteComment ="DELETE FROM comment WHERE commentId=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteComment)) {
            preparedStatement.setInt(1, obj.getCommentId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public void deleteCommentsFromMovie(int movieId) throws SQLException {
        String deleteComments ="DELETE FROM comment WHERE videoId=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteComments)) {
            preparedStatement.setInt(1, movieId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public void deleteCommentsOfUser(int userId) throws SQLException {
        String deleteComments ="DELETE FROM comment WHERE userId=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteComments)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<Comment> findAll() throws SQLException {
        return null;
    }

    @Override
    public Comment findById(int id) {
        return null;
    }

    @Override
    public Comment findByName(String name) throws SQLException {
        return null;
    }


    public List<Comment> findAllForMovie(int movieId) throws SQLException {
        String getComments ="select * from comment where videoId=?";
        List<Comment> comments = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(getComments)) {
            preparedStatement.setInt(1, movieId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                int userId = resultSet.getInt(2);
                int videoId = resultSet.getInt(3);
                String content = resultSet.getString(4);
                LocalDate commentDate = resultSet.getDate(5).toLocalDate();

                Comment comment = new Comment(id, userId, videoId, content, commentDate);
                comments.add(0, comment);
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return comments;
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
