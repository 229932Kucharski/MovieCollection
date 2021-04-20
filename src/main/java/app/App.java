package app;
import controller.DatabaseController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.SQLException;


public class App extends Application{

    private DatabaseController databaseController;
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    @Override
    public void start(Stage stage) throws Exception {
        logger.info("Application is starting");

        try {
            databaseController = new DatabaseController();
            databaseController.createDatabase();
        } catch (SQLException e) {
            logger.info("Database was already created");
        }

        try {
            databaseController.createAccount();
        } catch (SQLException e) {
            logger.info("Table account was already created");
        }

        try {
            databaseController.createMovie();
        } catch (SQLException e) {
            logger.info("Table movie was already created");
        }

        try {
            databaseController.createComment();
        } catch (SQLException e) {
            logger.info("Table comment was already created");
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/fxml/loginWindow.fxml"));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.setTitle("Sign in");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void changeScene(Pane old, String name) {
        Parent root;
        try {
            root = FXMLLoader.load(App.class.getResource("/fxml/" + name + ".fxml"));
            Stage stage = (Stage) old.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException e) {
            logger.error("Cant load new window");
            e.printStackTrace();
        }
    }

}
