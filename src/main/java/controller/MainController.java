package controller;

import app.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.dao.JdbcMovieDao;
import model.movie.Comment;
import model.movie.Genres;
import model.movie.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MainController {

    public AnchorPane mainAnchorPane;
    public SplitMenuButton genreSearcher;
    public Text welcomeText;
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    private List<Movie> movies;

    public void initialize(){
        //welcomeText.setText(UserController.getUser().welcomeText());
        MenuItem mi = new MenuItem(Genres.Action.name());
        MenuItem mi2 = new MenuItem(Genres.Adventure.name());
        MenuItem mi3 = new MenuItem(Genres.Comedy.name());
        MenuItem mi4 = new MenuItem(Genres.Drama.name());
        MenuItem mi5 = new MenuItem(Genres.Fantasy.name());
        MenuItem mi6 = new MenuItem(Genres.Horror.name());
        MenuItem mi7 = new MenuItem(Genres.Romance.name());
        MenuItem mi8 = new MenuItem(Genres.Mystery.name());
        MenuItem mi9 = new MenuItem(Genres.Thriller.name());
        MenuItem mi10 = new MenuItem(Genres.Western.name());
        MenuItem mi11 = new MenuItem(Genres.ScienceFiction.name());
        genreSearcher.getItems().addAll(mi,mi2,mi3,mi4,mi5,mi6,mi7,mi8,mi9,mi10,mi11);

        try (JdbcMovieDao movieDao = new JdbcMovieDao()) {
            logger.info("Getting list of movies");
            movies = movieDao.findAll();
        } catch (SQLException e) {
            logger.warn("Cant get movies from database");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


//        for (Movie e : movies) {
//            List<Comment> comments = e.getComments();
//            if(comments.size() > 0) {
//                System.out.println(comments.get(0));
//            }
//
//            System.out.println(e.toString());
//        }

    }

    public void logOut() {
        logger.info("User has been logged out");
        UserController.logout();
        App.changeScene(mainAnchorPane, "loginWindow");
    }

    public void showProfile() throws IOException {
        App.changeScene(mainAnchorPane, "profileWindow");
    }

}
