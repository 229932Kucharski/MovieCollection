package controller;

import app.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import manager.MovieManager;
import manager.UserManager;
import model.account.user.User;
import model.dao.*;
import model.movie.Comment;
import model.movie.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class FilmController {

    public Text commentsNumber;
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
    public VBox vBoxComment;
    public ImageView coverImage;
    public TextArea commentTextArea;
    public Label charactersLeftLabel;
    public ListView<CustomRow> listView;
    public Button deleteButton;
    public Button addCommentButton;
    public Button deleteCommentButton;
    public Button addFavouriteButton;
    public Button delFavouriteButton;
    private static final ObservableList<CustomRow> commentList = FXCollections.observableArrayList();
    public ChoiceBox<String> rateChoiceBox;
    public Text rateText;

    /**
     * Initializing window
     */
    public void initialize() throws Exception {
        Movie movie = MovieManager.getPickedMovie();
        boolean isFav = MovieManager.isMovieFav(UserManager.getLoggedUser());
        int userRate = MovieManager.getUserRate(UserManager.getLoggedUser());
        double avgRate = MovieManager.getAvgRate();

        ObservableList<String> rateOpt = FXCollections.observableArrayList();
        rateOpt.add("No rate");
        for (int i = 1; i < 11; i++) {
            rateOpt.add(String.valueOf(i));
        }

        rateChoiceBox.setItems(rateOpt);
        if(userRate != 0) {
            rateChoiceBox.setValue(String.valueOf(userRate));
        } else {
            rateChoiceBox.setValue("No rate");
        }

        rateText.setText(String.valueOf(avgRate));

        if(isFav) {
            delFavouriteButton.setVisible(true);
            addFavouriteButton.setVisible(false);
        } else {
            addFavouriteButton.setVisible(true);
            delFavouriteButton.setVisible(false);
        }
        if(UserManager.isAdmin()) {
            deleteButton.setVisible(true);
            deleteCommentButton.setVisible(true);
            deleteCommentButton.setDisable(true);
            addFavouriteButton.setVisible(false);
        } else if(!UserManager.isPremium()) {
            addCommentButton.setDisable(true);
            commentTextArea.setDisable(true);
            commentTextArea.setPromptText("You need premium account to comment");
            commentTextArea.setOpacity(1);
        }

        coverImage.setImage(MovieManager.getImage(movie));
        coverImage.setFitWidth(250);
        titleText.setText(movie.getTitle());
        directorText.setText(movie.getDirector());
        genreText.setText(movie.getGenre().toString());
        countryText.setText(movie.getCountry());
        releaseDateText.setText(movie.getPremiereDate().toString());
        timeText.setText(movie.getTimeDuration() + " min");
        ageText.setText(movie.getAgeRestriction().toString());
        descriptionText.setText(movie.getDescription());

        charactersLeftLabel.setText("250 characters left");
        commentTextArea.setText("");
        commentTextArea.textProperty().addListener((observableValue, s, t1) -> {
            int amountOfChars = t1.length();
            if(amountOfChars > 250) {
                commentTextArea.setText(s);
                charactersLeftLabel.setText(250 - s.length() + " character left");
            } else {
                charactersLeftLabel.setText(250 - amountOfChars + " character left");
            }
        });
        addCommentsToListView();
    }

    /**
     * Add comments to listview
     */
    public void addCommentsToListView() {
        MovieManager.setMovies();
        Movie movie = MovieManager.getPickedMovie();
        MovieManager.setPickedMovie(MovieManager.getMovieByTitle(movie.getTitle()));
        List<Comment> comments = MovieManager.getPickedMovie().getComments();
        commentsNumber.setText("Comments (" + comments.size() + ")");
        listView = new ListView<>(commentList);
        listView.getItems().clear();
        listView.setPrefSize(200, 500);
        listView.setEditable(false);
        listView.setOpacity(1);
        for(Comment comment : comments) {
            User user = null;
            String name;
            try(JdbcUserDao userDao = new JdbcUserDao()){
                user = userDao.findById(comment.getUserId());

            } catch (Exception e) {
                e.printStackTrace();
            }
            if(user != null) {
                name = user.getName();
            } else {
                name = "Account deleted";
            }
            commentList.add(new CustomRow(comment.getContent(), name, comment.getCommentDate().toString(),
                    String.valueOf(comment.getCommentId())));
        }
        listView.setItems(commentList);
        vBoxComment.getChildren().add(listView);

        listView.setCellFactory(customRowListView -> new CustomListCell());
        listView.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if(t1 != null) {
                MovieManager.setPickedComment(Integer.parseInt(t1.getCommentId()));
                deleteCommentButton.setDisable(false);
            }
        });
    }

    /**
     * Add new comment
     */
    public void addComment() {
        String content = commentTextArea.getText();
        if(content.equals("")) {
            return;
        }
        int movieId = MovieManager.getPickedMovie().getId();
        int userId = UserManager.getLoggedUser().getUserId();
        Comment comment = new Comment(1, userId, movieId, content, LocalDate.now());
        try(JdbcCommentDao commentDao = new JdbcCommentDao()) {
            commentDao.add(comment);
        } catch (Exception e) {
            logger.warn("Cant insert comment to database");
        }
        commentTextArea.setText("");
        MovieManager.getPickedMovie().addComment(comment);
        vBoxComment.getChildren().remove(listView);
        addCommentsToListView();
    }

    /**
     * Return to mainWindow page
     */
    public void previous() {
        MovieManager.setPickedMovie(null);
        App.changeScene(mainAnchorPane, "mainWindow");
    }

    /**
     * Delete movie
     */
    public void deleteMovie() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete movie");
        alert.setContentText("Do you want to delete this movie?");
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);
        alert.showAndWait().ifPresent(type -> {
            if (type == okButton) {
                try(JdbcMovieDao movieDao = new JdbcMovieDao()){
                    movieDao.delete(MovieManager.getPickedMovie());
                    MovieManager.setPickedMovie(null);
                    logger.info("Movie has been deleted");
                    Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
                    stage.close();
                    App.changeScene(mainAnchorPane, "mainWindow");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                alert.close();
            }
        });
    }

    /**
     * Delete comment (only for admin)
     */
    public void deleteComment() {
        try(JdbcCommentDao commentDao = new JdbcCommentDao()) {
            commentDao.delete(commentDao.findById(MovieManager.getPickedComment()));
            deleteCommentButton.setDisable(true);
        } catch (SQLException e) {
            logger.warn("Cant delete comment");
        } catch (Exception e) {
            e.printStackTrace();
        }
        vBoxComment.getChildren().remove(listView);
        addCommentsToListView();
    }

    /**
     * Add movie to favourite
     */
    public void addFavourite() {
        try(JdbcFavourite jdbcFavourite = new JdbcFavourite()) {
            jdbcFavourite.add(UserManager.getLoggedUser().getUserId(), MovieManager.getPickedMovie().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        addFavouriteButton.setVisible(false);
        delFavouriteButton.setVisible(true);
    }

    /**
     * Delete movie from favourite
     */
    public void delFavourite() {
        try(JdbcFavourite jdbcFavourite = new JdbcFavourite()) {
            jdbcFavourite.delete(UserManager.getLoggedUser().getUserId(), MovieManager.getPickedMovie().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        addFavouriteButton.setVisible(true);
        delFavouriteButton.setVisible(false);
    }

    /**
     * Give rate to movie
     */
    public void giveRate() throws Exception {
        String rateString = rateChoiceBox.getValue();
        int rate = 0;
        if(!rateString.equals("No rate")) {
            rate = Integer.parseInt(rateString);
        }
        JdbcUserRates userRates = new JdbcUserRates();
        userRates.deleteOfUser(UserManager.getLoggedUser().getUserId());
        userRates.add(UserManager.getLoggedUser().getUserId(), MovieManager.getPickedMovie().getId(), rate);
        vBoxComment.getChildren().remove(listView);
        initialize();
    }


    /**
     * Class of custom row displayed on listview
     */
    public static class CustomRow {
        private final String content;
        private final String author;
        private final String commentDate;
        private final String commentId;

        public CustomRow(String content, String author, String commentDate, String commentId) {
            this.content = content;
            this.author = author;
            this.commentDate = commentDate;
            this.commentId = commentId;
        }

        public String getCommentId() {
            return commentId;
        }

        public String getContent() {
            return content;
        }

        public String getAuthor() {
            return author;
        }

        public String getCommentDate() {
            return commentDate;
        }
    }

    /**
     * Class of custom cell displayed in row on listview
     */
    private static class CustomListCell extends ListCell<CustomRow> {
        private final VBox vBox;
        private final Text content;
        private final Text author;
        private final Text commentDate;

        public CustomListCell() {
            super();
            setWrapText(true);
            content = new Text();
            author = new Text();
            commentDate = new Text();
            HBox hBox = new HBox(author, commentDate);
            vBox = new VBox(content, hBox);
            vBox.setSpacing(10);
            hBox.setSpacing(10);
        }

        @Override
        protected void updateItem(CustomRow item, boolean empty) {
            super.updateItem(item, empty);
            if(item != null && !empty) {
                content.setText(item.getContent());
                content.setWrappingWidth(400);
                author.setText(item.getAuthor());
                commentDate.setText(item.getCommentDate());
                setGraphic(vBox);
            } else {
                setGraphic(null);

            }
        }
    }
}
