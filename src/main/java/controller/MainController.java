package controller;

import app.App;
import javafx.scene.layout.AnchorPane;

public class MainController {


    public AnchorPane mainAnchorPane;

    public void logOut() {
        App.changeScene(mainAnchorPane, "loginWindow");
    }

}
