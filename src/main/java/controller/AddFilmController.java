package controller;

import app.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import model.dao.JdbcMovieDao;
import model.movie.FullLengthFilm;
import model.movie.Genres;
import model.movie.ImageConverter;
import model.movie.Movie;
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
    public byte[] cover;
    public Label warnMessage;
    private static final Logger logger = LoggerFactory.getLogger(AddFilmController.class);
    private ObservableList<String> genres;

    public void initialize() {
        coverImage.setFitWidth(150);
        coverImage.setImage(new Image("/img/noCover.jpg"));
        genres = FXCollections.observableArrayList("Action", "Adventure", "Comedy", "Drama", "Fantasy", "Horror", "Romance", "Mystery",
                "Thriller", "Western", "ScienceFiction");
        genreChoiceBox.setItems(genres);


    }

    public void addFilm() {
        if(!checkForm()) {
            return;
        }
        String title = titleTextField.getText();
        String country = countryTextField.getText();
        String director = directorTextField.getText();
        LocalDate date = premiereDatePicker.getValue();
        String description = descriptionTextArea.getText();
        Double rate = Double.valueOf(avgRateTextField.getText());
        Integer age = Integer.valueOf(ageTextField.getText());
        Integer time = Integer.valueOf(timeTextField.getText());
        Genres genre = Genres.valueOf(genreChoiceBox.getValue());

        Movie movie = new FullLengthFilm(0, title, country, genre, director, cover, date, description, rate, age, time);
        try(JdbcMovieDao movieDao = new JdbcMovieDao()) {
            movieDao.add(movie);
        } catch (SQLException e) {
            logger.warn("Cant add movie");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        App.changeScene(mainAnchorPane, "mainWindow");
    }

    boolean checkForm() {
        if(titleTextField.getText().equals("")) {
            setWarning("No title");
            return false;
        } else if (countryTextField.getText().equals("")) {
            setWarning("No country");
            return false;
        } else if (directorTextField.getText().equals("")) {
            setWarning("No director");
            return false;
        }else if (premiereDatePicker.getValue() == null) {
            setWarning("No date");
            return false;
        }else if (descriptionTextArea.getText().equals("")) {
            setWarning("No description");
            return false;
        } else if (genreChoiceBox.getSelectionModel().isEmpty()) {
            setWarning("No genre");
            return false;
        }
        return true;
    }

    public void chooseCover() throws IOException, InterruptedException {
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Image Files", "jpg", "jpeg");
        fileChooser.setSelectedExtensionFilter(filter);
        fileChooser.setTitle("Choose a cover (.jpg)");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        File file = fileChooser.showOpenDialog(null);
        if (file == null) {
            return;
        }
        cover = ImageConverter.imageToByteArray(file.getAbsolutePath());
        ImageConverter.byteArrayToImage(0, cover);
        coverImage.setImage(new Image("/img/movieCover/0.jpg"));
    }

    private void setWarning(String mess) {
        logger.warn(mess);
        warnMessage.setText(mess);
        warnMessage.setStyle("-fx-text-fill: red");
    }

    public void previous() {
        App.changeScene(mainAnchorPane, "mainWindow");
    }
}
