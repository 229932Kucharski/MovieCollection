package controller;

import app.App;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import manager.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
        if(login.equals("") || password.equals("") || !UserManager.loginUser(login, password)) {
            setLoginWarning("Login or password incorrect");
            return;
        }

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
