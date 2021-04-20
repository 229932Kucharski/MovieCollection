package controller;

import app.App;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.account.password.PasswordHashing;
import model.account.user.Adult;
import model.account.user.User;
import model.dao.JdbcUserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;


public class ProfileController {
    public Text nameText;
    public Text ageText;
    public Text registerDateText;
    public Text emailText;
    public Text phoneNumber;
    public AnchorPane profileAnchorPane;
    public VBox passwordChangeVBox;
    public PasswordField oldPassTexField;
    public PasswordField newPassTextField;
    public PasswordField newPassRepTextField;
    public Button savePassword;
    public Text passwordWarning;
    User user;
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    public void initialize(){
        passwordChangeVBox.setVisible(false);
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
                try(JdbcUserDao userDao = new JdbcUserDao()){
                    UserController.logout();
                    userDao.delete(user);
                    logger.info("Account has been deleted");
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

    private void setPasswordWarning(String mess) {
        passwordWarning.setText(mess);
        passwordWarning.setStyle("-fx-text-fill: red");
    }

    public void back() {
        App.changeScene(profileAnchorPane, "mainWindow");
    }

    public void savePassword() {
        if(oldPassTexField.getText().equals("") || newPassTextField.getText().equals("")
                || newPassRepTextField.getText().equals("")) {
            setPasswordWarning("The field cant be empty");
            return;
        }
        if(!Arrays.equals(PasswordHashing.hashPassword(oldPassTexField.getText()), user.getPassword())) {
            setPasswordWarning("Old password is incorrect");
            return;
        }
        if(!newPassTextField.getText().equals(newPassRepTextField.getText())) {
            setPasswordWarning("New password is not the same");
            return;
        }
        if(newPassTextField.getText().length() < 6) {
            setPasswordWarning("Password is too short");
            return;
        }

        user.setPassword(newPassTextField.getText());

        try {
            JdbcUserDao userDao = new JdbcUserDao();
            userDao.update(user);
        } catch (SQLException e) {
            logger.warn("Cant change password");
            e.printStackTrace();
        }

        logger.info("User " + user.getName() + " changed password");
        setPasswordWarning("Password has been changed");
    }

    public void changePassword() {
        passwordChangeVBox.setVisible(true);
    }
}
