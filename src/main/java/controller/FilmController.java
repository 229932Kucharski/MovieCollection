package controller;

import app.App;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.account.user.User;
import model.dao.JdbcCommentDao;
import model.dao.JdbcFavourite;
import model.dao.JdbcMovieDao;
import model.dao.JdbcUserDao;
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

    private List<Comment> comments;
    private static final ObservableList<CustomRow> commentList = FXCollections.observableArrayList();

    public void initialize() throws Exception {
        Movie movie = MovieController.getPickedMovie();

        JdbcFavourite jdbcFavourite = new JdbcFavourite();
        boolean isFav = jdbcFavourite.isFavVideo(UserController.getLoggedUser().getUserId(),
                    MovieController.getPickedMovie().getId());
        if(isFav) {
            delFavouriteButton.setVisible(true);
            addFavouriteButton.setVisible(false);
        } else {
            addFavouriteButton.setVisible(true);
            delFavouriteButton.setVisible(false);
        }



        if(UserController.isAdmin()) {
            deleteButton.setVisible(true);
            deleteCommentButton.setVisible(true);
            deleteCommentButton.setDisable(true);
            addFavouriteButton.setVisible(false);
        } else if(!UserController.isPremium()) {
            addCommentButton.setDisable(true);
            commentTextArea.setDisable(true);
            commentTextArea.setPromptText("You need premium account to comment");
            commentTextArea.setOpacity(1);
        }

        coverImage.setImage(MovieController.getImage(movie));
        coverImage.setFitWidth(250);
        titleText.setText(movie.getTitle());
        directorText.setText(movie.getDirector());
        genreText.setText(movie.getGenre().toString());
        countryText.setText(movie.getCountry());
        releaseDateText.setText(movie.getPremiereDate().toString());
        timeText.setText(movie.getTimeDuration() + "min");
        ageText.setText(movie.getAgeRestriction().toString());
        descriptionText.setText(movie.getDescription());

        charactersLeftLabel.setText("250 characters left");
        commentTextArea.setText("");
        commentTextArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                int amountOfChars = t1.length();
                if(amountOfChars > 250) {
                    commentTextArea.setText(s);
                    charactersLeftLabel.setText(250 - s.length() + " character left");
                } else {
                    charactersLeftLabel.setText(250 - amountOfChars + " character left");
                }
            }
        });
        addCommentsToListView();
    }

    public void addCommentsToListView() {
        MovieController.setMovies();
        Movie movie = MovieController.getPickedMovie();
        MovieController.setPickedMovie(MovieController.getMovieByTitle(movie.getTitle()));
        comments = MovieController.getPickedMovie().getComments();
        commentsNumber.setText("Comments (" + comments.size() + ")");
        listView = new ListView<CustomRow>(commentList);
        listView.getItems().clear();
        listView.setPrefSize(200, 500);
        listView.setEditable(false);
        listView.setOpacity(1);
        for(Comment comment : comments) {
            User user = null;
            String name;
            try(JdbcUserDao userDao = new JdbcUserDao()){
                user = (User) userDao.findById(comment.getUserId());

            } catch (Exception e) {
                name = "Account deleted";
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

        listView.setCellFactory(new Callback<ListView<CustomRow>, ListCell<CustomRow>>() {
            @Override
            public ListCell<CustomRow> call(ListView<CustomRow> customRowListView) {
                return new CustomListCell();
            }
        });
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CustomRow>() {
            @Override
            public void changed(ObservableValue<? extends CustomRow> observableValue, CustomRow s, CustomRow t1) {
                if(t1 != null) {
                    MovieController.setPickedComment(Integer.parseInt(t1.getCommentId()));
                    deleteCommentButton.setDisable(false);
                }
            }
        });
    }

    public void addComment() throws Exception {
        String content = commentTextArea.getText();
        if(content.equals("")) {
            return;
        }
        int movieId = MovieController.getPickedMovie().getId();
        int userId = UserController.getLoggedUser().getUserId();
        Comment comment = new Comment(1, userId, movieId, content, LocalDate.now());
        try(JdbcCommentDao commentDao = new JdbcCommentDao()) {
            commentDao.add(comment);
        } catch (Exception e) {
            logger.warn("Cant insert comment to database");
        }
        commentTextArea.setText("");
        MovieController.getPickedMovie().addComment(comment);
        vBoxComment.getChildren().remove(listView);
        addCommentsToListView();
    }

    public void previous() {
        MovieController.setPickedMovie(null);
        App.changeScene(mainAnchorPane, "mainWindow");
    }

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
                    movieDao.delete(MovieController.getPickedMovie());
                    MovieController.setPickedMovie(null);
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

    public void deleteComment() throws Exception {
        try(JdbcCommentDao commentDao = new JdbcCommentDao()) {
            commentDao.delete(commentDao.findById(MovieController.getPickedComment()));
            deleteCommentButton.setDisable(true);
        } catch (SQLException e) {
            logger.warn("Cant delete comment");
        } catch (Exception e) {
            e.printStackTrace();
        }
        vBoxComment.getChildren().remove(listView);
        addCommentsToListView();
    }

    public void addFavourite() {
        try(JdbcFavourite jdbcFavourite = new JdbcFavourite()) {
            jdbcFavourite.add(UserController.getLoggedUser().getUserId(), MovieController.getPickedMovie().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        addFavouriteButton.setVisible(false);
        delFavouriteButton.setVisible(true);
    }

    public void delFavourite() {
        try(JdbcFavourite jdbcFavourite = new JdbcFavourite()) {
            jdbcFavourite.delete(UserController.getLoggedUser().getUserId(), MovieController.getPickedMovie().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        addFavouriteButton.setVisible(true);
        delFavouriteButton.setVisible(false);
    }


    // KLASA WIERSZA
    public static class CustomRow {
        private String content;
        private String author;
        private String commentDate;
        private String commentId;

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

    // KLASA KOMORKI
    private class CustomListCell extends ListCell<CustomRow> {
        private VBox vBox;
        private HBox hBox;
        private Text content;
        private Text author;
        private Text commentDate;

        public CustomListCell() {
            super();
            setWrapText(true);
            content = new Text();
            author = new Text();
            commentDate = new Text();
            hBox = new HBox(author, commentDate);
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
