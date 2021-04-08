package controller;

import app.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpController {

    public TextField loginTextField;
    public TextField emailTextField;
    public RadioButton femaleRadio;
    public RadioButton maleRadio;
    public ToggleGroup gender;
    public TextField phoneNumber;
    public DatePicker dateDatePicker;
    public AnchorPane signUpAnchorPane;
    public PasswordField passwordField;

    public void signIn() {
        App.changeScene(signUpAnchorPane, "loginWindow");
    }

    public void signUp() {

    }
}
