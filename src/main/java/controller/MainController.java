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
import manager.MovieManager;
import manager.UserManager;
import model.movie.Genres;
import model.movie.ImageConverter;
import model.movie.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    public AnchorPane mainAnchorPane;
    public Text welcomeText;
    public VBox vBoxList;
    public TextField searchTextField;
    public ListView<CustomRow> listView;
    public Button addFilmButton;
    public TextField yearTextField;
    public ChoiceBox<Genres> genreChoiceBox;

    private List<Movie> movies;
    private static final ObservableList<CustomRow> movieList = FXCollections.observableArrayList();

    /**
     * Initializing window
     */
    public void initialize() throws IOException {
        welcomeText.setText(UserManager.getLoggedUser().welcomeText());
        if(UserManager.isAdmin()) {
            addFilmButton.setVisible(true);
        }

        ObservableList<Genres> genres = FXCollections.observableArrayList(Genres.values());
        genreChoiceBox.setItems(genres);
        genreChoiceBox.setValue(Genres.All);


        MovieManager.setMovies();
        movies = MovieManager.getMovies();
        addMoviesToListView();

        yearTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                yearTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    /**
     * Add movies from movieList to ListView
     */
    private void addMoviesToListView() throws IOException {
        listView = new ListView<>(movieList);
        listView.getItems().clear();
        listView.setPrefSize(200, 500);
        listView.setEditable(true);
        //save all covers to files
        for (Movie movie : movies) {
            if (movie.getCover() != null) {
                ImageConverter.byteArrayToImage(movie.getId(), movie.getCover());
            }
        }
        //add movie to row and add it to listview
        for (Movie movie : movies) {
            movieList.add(new CustomRow(MovieManager.getImage(movie), movie.getTitle(), movie.getGenre().toString()));
        }
        listView.setItems(movieList);
        vBoxList.getChildren().add(listView);

        listView.setCellFactory(customRowListView -> new CustomListCell());

        //If movie picked, open filmWindow
        listView.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if(t1 != null) {
                MovieManager.setPickedMovie(MovieManager.getMovieByTitle(t1.getTitle()));
                App.changeScene(mainAnchorPane, "filmWindow");
            }
        });
    }

    /**
     * Search movies by String and add them to listView
     */
    public void search() throws IOException {
        String searchString = searchTextField.getText();
        String year = yearTextField.getText();
        Genres genre = genreChoiceBox.getValue();
        movies = MovieManager.getMoviesBySearch(searchString, year, genre);
        //Remove current listView and add new one
        vBoxList.getChildren().remove(listView);
        addMoviesToListView();
    }

    /**
     * Logout current user
     */
    public void logOut() {
        logger.info("User has been logged out");
        UserManager.logout();
        App.changeScene(mainAnchorPane, "loginWindow");
    }

    /**
     * Open new window that shows user profile. If user is Admin, open users list
     */
    public void showProfile() {
        if(UserManager.isAdmin()) {
            App.changeScene(mainAnchorPane, "profileListWindow");
        } else {
            App.changeScene(mainAnchorPane, "profileWindow");
        }
    }

    /**
     * Open new window (only admin) to add new film
     */
    public void addFilm() {
        App.changeScene(mainAnchorPane, "addFilmWindow");
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
                image.setFitWidth(100);
                setGraphic(content);
            } else {
                setGraphic(null);
            }
        }
    }
}
