package controller;

import app.App;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import manager.UserManager;
import model.account.user.Adult;
import model.account.user.Kid;
import model.account.user.User;
import model.exception.AgeException;
import model.exception.EmailException;
import model.exception.PasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Period;


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
    public Label loginWarning;
    public Label passwordInfoLabel;
    private static final Logger logger = LoggerFactory.getLogger(SignUpController.class);

    /**
     * Initializing window
     */
    public void initialize() {
        loginWarning.setStyle("-fx-text-fill: red");
        dateDatePicker.valueProperty().addListener((ov, oldVal, newVal) -> {
            LocalDate now = LocalDate.now();
            Period period = Period.between(newVal, now);
            if (period.getYears() >= 18) {
                phoneNumber.setDisable(false);
                phoneNumber.setText("");
            } else {
                phoneNumber.setDisable(true);
            }
        });

        passwordField.textProperty().addListener((observableValue, s, t1) -> {
            if(t1.equals("")) {
                passwordInfoLabel.setText("");
            }
            else if(t1.length() < 6) {
                passwordInfoLabel.setText("Password is too short");
                passwordInfoLabel.setStyle("-fx-text-fill: red");
            } else {
                passwordInfoLabel.setText("Password is correct");
                passwordInfoLabel.setStyle("-fx-text-fill: green");
            }
        });
    }

    /**
     * Return to loginWindow
     */
    public void signIn() {
        App.changeScene(signUpAnchorPane, "loginWindow");
    }

    /**
     * Mehtod that checks filed in form
     */
    public boolean checkForm() {
        String login = loginTextField.getText();
        String pass = passwordField.getText();
        String email = emailTextField.getText();
        LocalDate dateOfBirth = dateDatePicker.getValue();

        if(login.equals("")) {
            loginWarning.setText("Login cant be empty");
            return false;
        } else if(pass.equals("")) {
            loginWarning.setText("Password cant be empty");
            return false;
        } else if(email.equals("")) {
            loginWarning.setText("Email cant be empty");
            return false;
        } else if(!femaleRadio.isSelected() && !maleRadio.isSelected()) {
            loginWarning.setText("You have to choose gender");
            return false;
        } else if(dateOfBirth == null) {
            loginWarning.setText("Date of birth cant be empty");
            return false;
        }
        return true;
    }

    /**
     * Sign up new user
     */
    public void signUp() {
        if(!checkForm()) {
            return;
        }
        String login = loginTextField.getText();
        String pass = passwordField.getText();
        String email = emailTextField.getText();
        char gender;
        if (femaleRadio.isSelected()) {
            gender = 'K';
        } else {
            gender = 'M';
        }
        LocalDate dateOfBirth = dateDatePicker.getValue();
        String phoneNum = phoneNumber.getText();
        if (!UserManager.isLoginAvailable(login)) {
            loginWarning.setText("Login already in use");
            return;
        }

        User newUser;
        Period period = Period.between(dateOfBirth, LocalDate.now());
        if (period.getYears() >= 18) {
            try{
                newUser = new Adult(1, login, pass, LocalDate.now(), email, gender, dateOfBirth, phoneNum);
            } catch (PasswordException e) {
                loginWarning.setText("Password is too short");
                return;
            } catch (EmailException e) {
                loginWarning.setText("Email is incorrect");
                return;
            } catch (AgeException e) {
                loginWarning.setText("You are too young");
                return;
            }

        } else {
            try{
                newUser = new Kid(1, login, pass, LocalDate.now(), email, gender, dateOfBirth);
            } catch (PasswordException e) {
                loginWarning.setText("Password is too short");
                return;
            } catch (EmailException e) {
                loginWarning.setText("Email is incorrect");
                return;
            } catch (AgeException e) {
                loginWarning.setText("You are too young");
                return;
            }
        }
        UserManager.signUpUser(newUser);

        App.changeScene(signUpAnchorPane, "loginWindow");
    }
}
