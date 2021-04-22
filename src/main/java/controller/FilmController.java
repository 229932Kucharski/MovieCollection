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
import javafx.util.Callback;
import model.account.user.User;
import model.dao.JdbcCommentDao;
import model.dao.JdbcUserDao;
import model.movie.Comment;
import model.movie.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.Collections;
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
    public Button addComment;
    public Label charactersLeftLabel;
    public ListView<CustomRow> listView;

    private List<Comment> comments;
    private static final ObservableList<CustomRow> commentList = FXCollections.observableArrayList();

    public void initialize() throws Exception {
        Movie movie = MovieController.getPickedMovie();

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
        List<Comment> tempComments = MovieController.getPickedMovie().getComments();
        System.out.println(tempComments);
        //Collections.reverse(tempComments);
        comments = tempComments;
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
            commentList.add(new CustomRow(comment.getContent(), name, comment.getCommentDate().toString()));
        }
        listView.setItems(commentList);
        vBoxComment.getChildren().add(listView);

        listView.setCellFactory(new Callback<ListView<CustomRow>, ListCell<CustomRow>>() {
            @Override
            public ListCell<CustomRow> call(ListView<CustomRow> customRowListView) {
                return new CustomListCell();
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
        MovieController.getPickedMovie().addComment(comment);
        vBoxComment.getChildren().remove(listView);
        initialize();
    }

    public void previous() {
        MovieController.setPickedMovie(null);
        App.changeScene(mainAnchorPane, "mainWindow");
    }


    // KLASA WIERSZA
    public static class CustomRow {
        private String content;
        private String author;
        private String commentDate;

        public CustomRow(String content, String author, String commentDate) {
            this.content = content;
            this.author = author;
            this.commentDate = commentDate;
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
