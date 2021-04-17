package controller;

import app.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.account.Account;
import model.account.password.PasswordHashing;
import model.account.user.User;
import model.dao.Dao;
import model.dao.JdbcUserDao;

import java.sql.SQLException;
import java.util.Arrays;


public class LoginController {

    public TextField loginTextField;
    public AnchorPane loginAnchorPane;
    public PasswordField passwordField;
    public Label loginInfoLabel;

    public void login() {
        String login = loginTextField.getText();
        String password = passwordField.getText();

        // TODO
        if(login.equals("admin")) {
            App.changeScene(loginAnchorPane, "mainWindow");
            return;
        }

        if(login.equals("") || password.equals("")) {
            setLoginWarning("Login or password incorrect");
            return;
        }
        Account user = null;
        try(JdbcUserDao userDao = new JdbcUserDao()) {
            user = userDao.findByName(login);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            setLoginWarning("Login or password incorrect");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            setLoginWarning("Some error occured, please restart application");
            return;
        }
        if(user == null || !Arrays.equals(user.getPassword(), PasswordHashing.hashPassword(password))) {
            setLoginWarning("Login or password incorrect");
            return;
        }

        UserController.setUser((User) user);
        App.changeScene(loginAnchorPane, "mainWindow");
    }

    private void setLoginWarning(String mess) {
        loginInfoLabel.setText(mess);
        loginInfoLabel.setStyle("-fx-text-fill: red");
    }

    public void signUp() {
        App.changeScene(loginAnchorPane, "signUpWindow");
    }
}
