package app;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;


public class App extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/fxml/loginWindow.fxml"));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);;
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
