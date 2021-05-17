package controller;

import app.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import manager.ImageManager;
import manager.MovieManager;
import model.dao.JdbcMovieDao;
import model.movie.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddFilmController {

    public TextField titleTextField;
    public TextField countryTextField;
    public TextField directorTextField;
    public DatePicker premiereDatePicker;
    public TextArea descriptionTextArea;
    public TextField avgRateTextField;
    public TextField ageTextField;
    public TextField timeTextField;
    public ChoiceBox<String> genreChoiceBox;
    public AnchorPane mainAnchorPane;
    private final FileChooser fileChooser = new FileChooser();
    public ImageView coverImage;
    public Label warnMessage;
    private static final Logger logger = LoggerFactory.getLogger(AddFilmController.class);

    private String title;
    private String country;
    private String director;
    private LocalDate date;
    private String description;
    private Double rate;
    private Integer age;
    private Integer time;
    private Genres genre;
    private byte[] cover;

    /**
     * Method for initializing addFilmWindow
     */
    public void initialize() {
        coverImage.setFitWidth(150);
        coverImage.setImage(new Image("/img/noCover.jpg"));
        ObservableList<String> genres = FXCollections.observableArrayList("Action", "Adventure", "Comedy", "Drama",
                "Fantasy", "Horror", "Romance", "Mystery",
                "Thriller", "Western", "ScienceFiction");
        genreChoiceBox.setItems(genres);
    }

    /**
     * Method create movie and add movie to database
     */
    public void addFilm() {
        if(!checkForm()) {
            return;
        }
        MovieManager.addMovie(0, title, country, genre, director, cover, date, description, rate, age, time);
        App.changeScene(mainAnchorPane, "mainWindow");
    }

    /**
     * Check fields in form for adding new movie
     */
    private boolean checkForm() {
        title = titleTextField.getText();
        country = countryTextField.getText();
        director = directorTextField.getText();
        date = premiereDatePicker.getValue();
        description = descriptionTextArea.getText();

        if(!timeTextField.getText().equals("")) {
            time = Integer.valueOf(timeTextField.getText());
        } else {
            time = 0;
        }
        if(!avgRateTextField.getText().equals("")) {
            rate = Double.valueOf(avgRateTextField.getText().replace(',', '.'));
        }
        if(!ageTextField.getText().equals("")) {
            age = Integer.valueOf(ageTextField.getText());
        }

        if(title.equals("") || title.length() > 50) {
            setWarning("Wrong title");
            return false;
        } else if (country.equals("") || country.length() > 50) {
            setWarning("Wrong country");
            return false;
        } else if (director.equals("") || director.length() > 50) {
            setWarning("Wrong director");
            return false;
        }else if (premiereDatePicker.getValue() == null) {
            setWarning("Wrong date");
            return false;
        }else if (description.equals("") || description.length() > 500) {
            setWarning("Wrong description");
            return false;
        } else if (genreChoiceBox.getSelectionModel().isEmpty()) {
            setWarning("No genre selected");
            return false;
        } else if (time < 0) {
            setWarning("Time duration cant be lower than 0");
            return false;
        }
        else if (avgRateTextField.getText().equals("") || rate < 0 || rate > 10) {
            setWarning("Wrong rate");
            return false;
        } else if (ageTextField.getText().equals("")) {
            setWarning("Wrong age restriction");
            return false;
        }
        genre = Genres.valueOf(genreChoiceBox.getValue());
        return true;
    }

    /**
     * Choose cover for new movie from disk
     */
    @FXML
    public void chooseCover() throws IOException {
        cover = ImageManager.chooseCoverFromFile();
        if(cover == null) {
            return;
        }
        File imageFile = new File("src/main/assets/img/movieCover/0.jpg");
        Image image = new Image(imageFile.toURI().toString());
        coverImage.setImage(image);
    }

    /**
     * Set warning if field is incorrect
     * @param mess warning message
     */
    private void setWarning(String mess) {
        logger.warn(mess);
        warnMessage.setText(mess);
        warnMessage.setStyle("-fx-text-fill: red");
    }

    /**
     * Return to mainWindow
     */
    public void previous() {
        App.changeScene(mainAnchorPane, "mainWindow");
    }
}
