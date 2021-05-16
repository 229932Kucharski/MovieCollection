package controller;

import app.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import manager.MovieManager;
import manager.UserManager;
import model.account.password.PasswordHashing;
import model.account.user.Adult;
import model.account.user.User;
import model.dao.JdbcFavourite;
import model.dao.JdbcUserDao;
import model.movie.ImageConverter;
import model.movie.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;


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
    public Text passwordWarning;
    public VBox vBoxList;
    public ListView<CustomRow> listView;

    private List<Movie> movies;
    private static final ObservableList<CustomRow> movieList = FXCollections.observableArrayList();
    private User user;
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    /**
     * Initializing window
     */
    public void initialize() throws IOException {
        passwordChangeVBox.setVisible(false);
        user = UserManager.getLoggedUser();
        nameText.setText(user.getName());
        ageText.setText(String.valueOf(Period.between(user.getBirthDate(), LocalDate.now()).getYears()));
        registerDateText.setText(user.getRegisterDate().toString());
        emailText.setText(user.getEmail());
        if(user instanceof Adult) {
            Adult adult = (Adult) user;
            phoneNumber.setText(adult.getPhoneNumber());
        }

        //Get all favourite movies
        try (JdbcFavourite jdbcFavourite = new JdbcFavourite()){
            movies = jdbcFavourite.getAllFavVideo(UserManager.getLoggedUser().getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        addMoviesToListView();
    }

    /**
     * Add favourite movies to listview
     */
    private void addMoviesToListView() throws IOException {
        listView = new ListView<>(movieList);
        listView.getItems().clear();
        listView.setPrefSize(200, 500);
        listView.setEditable(true);
        for(Movie movie : movies) {
            if(movie.getCover() != null) {
                ImageConverter.byteArrayToImage(movie.getId(), movie.getCover());
            }
            movieList.add(new CustomRow(MovieManager.getImage(movie), movie.getTitle(), movie.getGenre().toString()));
        }
        listView.setItems(movieList);
        vBoxList.getChildren().add(listView);

        listView.setCellFactory(customRowListView -> new CustomListCell());
    }

    /**
     * Delete account, popup window
     */
    public void deleteAccount() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        ((Stage) alert.getDialogPane().getScene()
                .getWindow()).getIcons().add(new Image("/img/cam.png"));
        alert.setTitle("Delete account");
        alert.setContentText("Do you want to delete you account?");
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);
        alert.showAndWait().ifPresent(type -> {
            if (type == okButton) {
                try{
                    UserManager.deleteUser(null);
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

    /**
     * Show warning
     */
    private void setPasswordWarning(String mess) {
        passwordWarning.setText(mess);
        passwordWarning.setStyle("-fx-text-fill: red");
    }

    /**
     * Save new password for user
     */
    public void savePassword() {
        //Check if fields are correct
        if(oldPassTexField.getText().equals("") || newPassTextField.getText().equals("")
                || newPassRepTextField.getText().equals("")) {
            setPasswordWarning("The field cant be empty");
            return;
        } else if(!Arrays.equals(PasswordHashing.hashPassword(oldPassTexField.getText()), user.getPassword())) {
            setPasswordWarning("Old password is incorrect");
            return;
        } else if(!newPassTextField.getText().equals(newPassRepTextField.getText())) {
            setPasswordWarning("New password is not the same");
            return;
        } else if(newPassTextField.getText().length() < 6) {
            setPasswordWarning("Password is too short");
            return;
        }

        //Set new password
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

    /**
     * Return to mainWindow
     */
    public void back() {
        App.changeScene(profileAnchorPane, "mainWindow");
    }

    /**
     * Display textFields for password change
     */
    public void changePassword() {
        passwordChangeVBox.setVisible(true);
    }

    /**
     * Class of custom row displayed on listview
     */
    public static class CustomRow {
        private final Image image;
        private final String title;
        private final String genre;

        public CustomRow(Image image, String title, String genre) {
            this.image = image;
            this.title = title;
            this.genre = genre;
        }

        public Image getImage() {
            return image;
        }

        public String getTitle() {
            return title;
        }

        public String getGenre() {
            return genre;
        }
    }

    /**
     * Class of custom cell displayed in row on listview
     */
    private static class CustomListCell extends ListCell<CustomRow> {
        private final HBox content;
        private final Text title;
        private final Text genre;
        private final ImageView image;

        public CustomListCell() {
            super();
            title = new Text();
            genre = new Text();
            image = new ImageView();
            VBox vBox = new VBox(title, genre);
            content = new HBox(image, vBox);
            content.setSpacing(10);
        }

        @Override
        protected void updateItem(CustomRow item, boolean empty) {
            super.updateItem(item, empty);
            if(item != null && !empty) {
                title.setText(item.getTitle());
                genre.setText(item.getGenre());
                image.setImage(item.getImage());
                image.setPreserveRatio(true);
                image.setFitWidth(50);
                setGraphic(content);
            } else {
                setGraphic(null);
            }
        }
    }
}
