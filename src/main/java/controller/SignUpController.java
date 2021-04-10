package controller;

import app.App;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import model.account.Account;
import model.account.password.PasswordHashing;
import model.account.user.Adult;
import model.account.user.Kid;
import model.dao.JdbcUserDao;
import model.exception.AgeException;
import model.exception.EmailException;
import model.exception.PasswordException;

import java.sql.SQLException;
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

    @FXML
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

    public void signIn() {
        App.changeScene(signUpAnchorPane, "loginWindow");
    }

    public boolean checkForm() {
        String login = loginTextField.getText();
        String pass = passwordField.getText();
        String email = emailTextField.getText();
        char gender;
        if (femaleRadio.isSelected()) {
            gender = 'K';
        } else if(maleRadio.isSelected()) {
            gender = 'M';
        }
        LocalDate dateOfBirth = dateDatePicker.getValue();
        System.out.println(dateOfBirth);
        String phoneNum = phoneNumber.getText();

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

        Account user = null;
        try (JdbcUserDao userDao = new JdbcUserDao()) {
            user = userDao.findByName(login);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user != null) {
            loginWarning.setText("Login already in use");
            return;
        }

        Account newUser = null;
        Period period = Period.between(dateOfBirth, LocalDate.now());
        if (period.getYears() >= 18) {
            try{
                newUser = new Adult(login, pass, email, gender, dateOfBirth, phoneNum);
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
                newUser = new Kid(login, pass, email, gender, dateOfBirth);
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
        try (JdbcUserDao userDao = new JdbcUserDao()) {
            userDao.add(newUser);
        } catch (Exception e) {
            e.printStackTrace();
        }

        App.changeScene(signUpAnchorPane, "loginWindow");

    }
}
