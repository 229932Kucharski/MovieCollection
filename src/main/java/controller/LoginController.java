package controller;

import app.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;


public class LoginController {

    public TextField loginTextField;
    public AnchorPane loginAnchorPane;
    public PasswordField passwordField;
    public Label loginInfoLabel;


    public void login() {
        String login = loginTextField.getText();
        String password = passwordField.getText();
        System.out.println(login + "  " + password);
        
        if(login.equals("")) {
            loginInfoLabel.setText("Login or password incorrect");
            loginInfoLabel.setStyle("-fx-text-fill: red");
            return;
        }
        
        App.changeScene(loginAnchorPane, "mainWindow");
    }

    public void signUp() {
        App.changeScene(loginAnchorPane, "signUpWindow");
    }
}
