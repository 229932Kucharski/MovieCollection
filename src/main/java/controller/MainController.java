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
import javafx.stage.Stage;
import model.movie.Genres;

import java.io.IOException;

public class MainController {


    public AnchorPane mainAnchorPane;
    public SplitMenuButton genreSearcher;
    public void initialize() {
        MenuItem mi = new MenuItem(Genres.Action.name());
        MenuItem mi2 = new MenuItem(Genres.Adventure.name());
        MenuItem mi3 = new MenuItem(Genres.Comedy.name());
        MenuItem mi4 = new MenuItem(Genres.Drama.name().toString());
        MenuItem mi5 = new MenuItem(Genres.Fantasy.name());
        MenuItem mi6 = new MenuItem(Genres.Horror.name());
        MenuItem mi7 = new MenuItem(Genres.Romance.name());
        MenuItem mi8 = new MenuItem(Genres.Mystery.name());
        MenuItem mi9 = new MenuItem(Genres.Thriller.name());
        MenuItem mi10 = new MenuItem(Genres.Western.name());
        MenuItem mi11 = new MenuItem(Genres.ScienceFiction.name());
        genreSearcher.getItems().addAll(mi,mi2,mi3,mi4,mi5,mi6,mi7,mi8,mi9,mi10,mi11);

    }

    public void logOut() {
        App.changeScene(mainAnchorPane, "loginWindow");
    }

    public void showProfile(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane root = fxmlLoader.load(getClass().getResource("/fxml/profileWindow.fxml").openStream());
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Your profile");
        stage.showAndWait();
    }

}
