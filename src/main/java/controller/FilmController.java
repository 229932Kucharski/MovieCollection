package controller;

import app.App;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import model.movie.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilmController {

    private static final Logger logger = LoggerFactory.getLogger(FilmController.class);
    public AnchorPane mainAnchorPane;
    public Text titleText;
    public Text directorText;
    public Text genreText;
    public Text countryText;
    public Text releaseDateText;
    public Text timeText;
    public Text ageText;
    public TextArea descriptionText;

    public void initialize() {
        logger.info("Opening movie site");
        Movie movie = MovieController.getPickedMovie();
        titleText.setText(movie.getTitle());
        directorText.setText(movie.getDirector());
        genreText.setText(movie.getGenre().toString());
        countryText.setText(movie.getCountry());
        releaseDateText.setText(movie.getPremiereDate().toString());
        timeText.setText(movie.getTimeDuration() + "min");
        ageText.setText(movie.getAgeRestriction().toString());
        descriptionText.setText(movie.getDescription());

    }


    public void previous() {
        MovieController.setPickedMovie(null);
        App.changeScene(mainAnchorPane, "mainWindow");
    }
}
