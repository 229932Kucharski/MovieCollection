package model.dao;

import model.account.Account;
import model.account.supervisor.Administrator;
import model.account.user.Adult;
import model.account.user.Kid;
import model.account.user.User;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserDao implements Dao<User> {

    private final String url = "jdbc:sqlserver://localhost;databaseName=movieCollection";
    private final String username = "sa";
    private final String password = "qwerty";
    private final Connection connection;

    public JdbcUserDao() throws SQLException {
        connection = prepareConnection();
    }

    private Connection prepareConnection() throws SQLException{
        Connection connection = null;
        connection = DriverManager.getConnection(url, username, password);
        return connection;
    }


    @Override
    public void add(User obj) throws SQLException {

        String insertUser ="insert into account(name, password, registerDate, email, gender, birthDate, phoneNumber, premiumUser) VALUES "
                + "(?, ?, ?, ?, ?, ?, ?, ?)";

        if(obj instanceof Adult) {
            Adult account = (Adult) obj;
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertUser)) {
                preparedStatement.setString(1, account.getName());
                preparedStatement.setBytes(2, account.getPassword());
                preparedStatement.setDate(3, Date.valueOf(account.getRegisterDate()));
                preparedStatement.setString(4, account.getEmail());
                preparedStatement.setString(5, String.valueOf(account.getGender()));
                preparedStatement.setDate(6, Date.valueOf(account.getBirthDate()));
                preparedStatement.setString(7, account.getPhoneNumber());
                preparedStatement.setBoolean(8, false);
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                throw new SQLException(e);
            }
        } else {
            Kid account = (Kid) obj;
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertUser)) {
                preparedStatement.setString(1, account.getName());
                preparedStatement.setBytes(2, account.getPassword());
                preparedStatement.setDate(3, Date.valueOf(account.getRegisterDate()));
                preparedStatement.setString(4, account.getEmail());
                preparedStatement.setString(5, String.valueOf(account.getGender()));
                preparedStatement.setDate(6, Date.valueOf(account.getBirthDate()));
                preparedStatement.setDate(7, null);
                preparedStatement.setBoolean(8, false);
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                throw new SQLException(e);
            }
        }
    }

    @Override
    public void update(User obj) throws SQLException {
        String deleteUser ="UPDATE account SET password = ? WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteUser)) {
            preparedStatement.setBytes(1, obj.getPassword());
            preparedStatement.setString(2, obj.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(User obj) throws SQLException {
        String deleteUser ="DELETE FROM account WHERE name=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteUser)) {
            preparedStatement.setString(1, obj.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<User> findAll() throws SQLException {
        String getUsers = "select * from account";
        User user = null;
        List<User> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(getUsers)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                String login = resultSet.getString(1);
                byte[]pass = resultSet.getBytes(2);
                Date registerDate = resultSet.getDate(3);
                String email = resultSet.getString(4);
                char gender = resultSet.getString(5).charAt(0);
                Date birthDate = resultSet.getDate(6);
                String phoneNumber = resultSet.getString(7);
                int userId = resultSet.getInt(8);
                boolean isPremium = resultSet.getBoolean(9);
                if (login == null) {
                    return null;
                }
                LocalDate birthDateLoc = birthDate.toLocalDate();
                Period period = Period.between(birthDateLoc, LocalDate.now());
                if(login.equals("admin")) {
                    user = new Administrator(userId, login, pass, email, gender, birthDateLoc);
                }
                else if(period.getYears() >= 18) {
                    user = new Adult(userId, login, pass, email, gender, birthDateLoc, phoneNumber);
                    users.add(user);
                } else {
                    user = new Kid(userId, login, pass, email, gender, birthDateLoc);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return users;
    }

    @Override
    public User findById(int id) throws SQLException {
        String getUser = "select * from account where userId=?";
        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(getUser)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String login = resultSet.getString(1);
                byte[]pass = resultSet.getBytes(2);
                Date registerDate = resultSet.getDate(3);
                String email = resultSet.getString(4);
                char gender = resultSet.getString(5).charAt(0);
                Date birthDate = resultSet.getDate(6);
                String phoneNumber = resultSet.getString(7);
                int userId = resultSet.getInt(8);
                boolean isPremium = resultSet.getBoolean(9);
                if (login == null) {
                    return null;
                }
                LocalDate birthDateLoc = birthDate.toLocalDate();
                Period period = Period.between(birthDateLoc, LocalDate.now());
                if(login.equals("admin")) {
                    user = new Administrator(userId, login, pass, email, gender, birthDateLoc);
                }
                else if(period.getYears() >= 18) {
                    user = new Adult(userId, login, pass, email, gender, birthDateLoc, phoneNumber);
                } else {
                    user = new Kid(userId, login, pass, email, gender, birthDateLoc);
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return user;
    }

    @Override
    public User findByName(String name) throws SQLException {
        String getUser = "select * from account where name=?";
        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(getUser)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                String login = resultSet.getString(1);
                byte[]pass = resultSet.getBytes(2);
                Date registerDate = resultSet.getDate(3);
                String email = resultSet.getString(4);
                char gender = resultSet.getString(5).charAt(0);
                Date birthDate = resultSet.getDate(6);
                String phoneNumber = resultSet.getString(7);
                int userId = resultSet.getInt(8);
                boolean isPremium = resultSet.getBoolean(9);
                if (login == null) {
                    return null;
                }
                LocalDate birthDateLoc = birthDate.toLocalDate();
                Period period = Period.between(birthDateLoc, LocalDate.now());
                if(login.equals("admin")) {
                    user = new Administrator(userId, login, pass, email, gender, birthDateLoc);
                }
                else if(period.getYears() >= 18) {
                    user = new Adult(userId, login, pass, email, gender, birthDateLoc, phoneNumber);
                } else {
                    user = new Kid(userId, login, pass, email, gender, birthDateLoc);
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        return user;
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
