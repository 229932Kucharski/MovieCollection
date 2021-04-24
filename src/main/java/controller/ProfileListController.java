package controller;

import app.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.account.Account;
import model.account.user.PremiumAdult;
import model.account.user.User;
import model.dao.JdbcUserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.sql.SQLException;
import java.util.List;

public class ProfileListController {
    private static final Logger logger = LoggerFactory.getLogger(ProfileListController.class);
    public AnchorPane mainAnchorPane;
    public VBox vBoxList;
    public ListView<CustomRow> listView;
    private List<User> users;
    private static final ObservableList<CustomRow> userList = FXCollections.observableArrayList();

    public void initialize() {
        try (JdbcUserDao userDao = new JdbcUserDao()) {
            users = userDao.findAll();
        } catch (SQLException e) {
            logger.warn("Cant get users from database");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        listView = new ListView<CustomRow>(userList);
        listView.getItems().clear();
        listView.setPrefSize(200, 500);
        listView.setEditable(true);
        boolean isPremium = false;
        for(Account user : users) {
            if(user instanceof PremiumAdult) {
                isPremium = true;
            }
           // userList.add(new CustomRow(user.getName(), user.getRegisterDate().toString(), isPremium));
        }
        listView.setItems(userList);
        vBoxList.getChildren().add(listView);

    }

    public void previous() {
        App.changeScene(mainAnchorPane, "mainWindow");
    }

    // KLASA WIERSZA
    public static class CustomRow {
        private String name;
        private String registerDate;
        private String email;
        private boolean premium;

        public CustomRow(String name, String registerDate, String email, boolean premium) {
            this.name = name;
            this.registerDate = registerDate;
            this.email = email;
            this.premium = premium;
        }

        public String getName() {
            return name;
        }

        public String getRegisterDate() {
            return registerDate;
        }

        public String getEmail() {
            return email;
        }

        public boolean isPremium() {
            return premium;
        }
    }
    // KLASA KOMORKI
    private class CustomListCell extends ListCell<CustomRow> {
        private HBox content;
        private Text name;
        private Text date;
        private Text email;
        private Text premium;

        public CustomListCell() {
            super();
            name = new Text();
            date = new Text();
            email = new Text();
            date = new Text();
            premium = new Text();
            content = new HBox(name, email, date, premium);
            content.setSpacing(5);
        }

        @Override
        protected void updateItem(CustomRow item, boolean empty) {
            super.updateItem(item, empty);
            if(item != null && !empty) {
                name.setText(item.getName());
                date.setText(item.getRegisterDate().toString());
                email.setText(item.getEmail());
                setGraphic(content);
            } else {
                setGraphic(null);
            }
        }
    }
}
