package controller;

import app.App;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import manager.UserManager;
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

    /**
     * Login user and set loggeduser in UserManager
     */
    public void login() {
        String login = loginTextField.getText();
        String password = passwordField.getText();

        //Check if fields are not empty
        if(login.equals("") || password.equals("")) {
            setLoginWarning("Login or password incorrect");
            return;
        }
        User user;
        //Check if user login exist in database
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
        //Check if password is correct
        if(user == null || !Arrays.equals(user.getPassword(), PasswordHashing.hashPassword(password))) {
            setLoginWarning("Login or password incorrect");
            return;
        }

        //Set logged user and open mainWindow
        UserManager.setLoggedUser(user);
        logger.info("User " + user.getName() + " has logged in");

        App.changeScene(loginAnchorPane, "mainWindow");
        loginAnchorPane.getStylesheets().add("style/style.css");
    }

    /**
     * Set warning if field is incorrect
     * @param mess warning message
     */
    private void setLoginWarning(String mess) {
        logger.warn(mess);
        loginInfoLabel.setText(mess);
        loginInfoLabel.setStyle("-fx-text-fill: red");
    }

    /**
     * Open new window for signup
     */
    public void signUp() {
        App.changeScene(loginAnchorPane, "signUpWindow");
    }
}
