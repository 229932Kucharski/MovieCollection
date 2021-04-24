package controller;

import app.App;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.account.Account;
import model.account.password.PasswordHashing;
import model.account.user.User;
import model.dao.JdbcUserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Arrays;


public class LoginController {

    public TextField loginTextField;
    public AnchorPane loginAnchorPane;
    public PasswordField passwordField;
    public Label loginInfoLabel;
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    public void login() {
        String login = loginTextField.getText();
        String password = passwordField.getText();


        if(login.equals("") || password.equals("")) {
            setLoginWarning("Login or password incorrect");
            return;
        }
        User user = null;
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

        UserController.setLoggedUser(user);
        logger.info("User " + user.getName() + " has logged in");
        App.changeScene(loginAnchorPane, "mainWindow");
    }

    private void setLoginWarning(String mess) {
        logger.warn(mess);
        loginInfoLabel.setText(mess);
        loginInfoLabel.setStyle("-fx-text-fill: red");
    }

    public void signUp() {
        App.changeScene(loginAnchorPane, "signUpWindow");
    }
}
