package app;
import controller.DatabaseController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.SQLException;


public class App extends Application{

    private DatabaseController databaseController;

    @Override
    public void start(Stage stage) throws Exception {

        try {
            databaseController = new DatabaseController();
            databaseController.createDatabase();
        } catch (SQLException e) {
            System.out.println("Database was already created");
        }

        try {
            databaseController.createAccount();
        } catch (SQLException e) {
            System.out.println("Table account was already created");
        }

        try {
            databaseController.createMovie();
        } catch (SQLException e) {
            System.out.println("Table account was already created");
        }

        try {
            databaseController.createComment();
        } catch (SQLException e) {
            System.out.println("Table account was already created");
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
            e.printStackTrace();
        }
    }

}
