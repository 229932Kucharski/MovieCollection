package controller;

import app.App;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.account.user.Adult;
import model.account.user.User;
import model.dao.JdbcUserDao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

public class ProfileController {
    public Text nameText;
    public Text ageText;
    public Text registerDateText;
    public Text emailText;
    public Text phoneNumber;
    public AnchorPane profileAnchorPane;
    User user;


    public void initialize(){
        user = UserController.getUser();
        nameText.setText(user.getName());
        ageText.setText(String.valueOf(Period.between(user.getBirthDate(), LocalDate.now()).getYears()));
        registerDateText.setText(user.getRegisterDate().toString());
        emailText.setText(user.getEmail());
        if(user instanceof Adult) {
            Adult adult = (Adult) user;
            phoneNumber.setText(adult.getPhoneNumber());
        }
    }


    public void deleteAccount() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete account");
        alert.setContentText("Do you want to delete you account?");
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);
        alert.showAndWait().ifPresent(type -> {
            if (type == okButton) {
                System.out.println("SS");
                try(JdbcUserDao userDao = new JdbcUserDao()){
                    UserController.logout();
                    userDao.delete(user);
                    Stage stage = (Stage) profileAnchorPane.getScene().getWindow();
                    stage.close();
                    App.changeScene(profileAnchorPane, "loginWindow");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                alert.close();
            }
        });
    }

    public void back() {
        App.changeScene(profileAnchorPane, "mainWindow");
    }
}
