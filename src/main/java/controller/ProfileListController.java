package controller;

import app.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import manager.UserManager;
import model.account.user.Adult;
import model.account.user.Kid;
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
    public TextField usernameTextField;
    public Button promoteButton;
    private List<User> users;
    public ListView<CustomRow> listView;
    private static final ObservableList<CustomRow> userList = FXCollections.observableArrayList();

    /**
     * Initializing window
     */
    public void initialize() {
        UserManager.setPickedUser(null);
        try (JdbcUserDao userDao = new JdbcUserDao()) {
            users = userDao.findAll();
            UserManager.setUsers(users);
        } catch (SQLException e) {
            logger.warn("Cant get users from database");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        addUsersToListView();
    }

    /**
     * Add users to listview
     */
    private void addUsersToListView() {
        listView = new ListView<>(userList);
        listView.getItems().clear();
        listView.setPrefSize(200, 500);
        listView.setEditable(true);
        boolean isPremium;
        for(User user : users) {
            isPremium = user instanceof PremiumAdult;
            userList.add(new CustomRow(user.getUserId(), user.getName(), user.getRegisterDate().toString(),
                    user.getEmail(), isPremium));
        }
        listView.setItems(userList);
        vBoxList.getChildren().add(listView);
        listView.setCellFactory(customRowListView -> new CustomListCell());
        listView.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if(t1 != null) {
                User user = UserManager.getUsersByName(t1.getName()).get(0);
                promoteButton.setDisable(user instanceof Kid || user instanceof PremiumAdult);
                UserManager.setPickedUser(UserManager.getUsersByName(t1.getName()).get(0));
            }
        });
    }

    /**
     * Return to mainWindow
     */
    public void previous() {
        App.changeScene(mainAnchorPane, "mainWindow");
    }

    /**
     * Search user by name
     */
    public void searchByName() {
        String username = usernameTextField.getText();
        users = UserManager.getUsersByName(username);
        vBoxList.getChildren().remove(listView);
        addUsersToListView();
    }

    /**
     * Delete picked user
     */
    public void deleteUser() {
        if(UserManager.getPickedUser() == null) {
            return;
        }
        UserManager.deleteUser(UserManager.getPickedUser());
        vBoxList.getChildren().remove(listView);
        UserManager.setPickedUser(null);
        users = UserManager.getUsers();
        addUsersToListView();
    }

    /**
     * Promote user to premium account
     */
    public void promoteUser() {
        if(UserManager.getPickedUser() instanceof Kid || UserManager.getPickedUser() instanceof PremiumAdult) {
            return;
        }
        Adult user = (Adult) UserManager.getPickedUser();
        UserManager.promoteUser(user);
        vBoxList.getChildren().remove(listView);
        UserManager.setPickedUser(null);
        users = UserManager.getUsers();
        addUsersToListView();
    }

    /**
     * Class of custom row displayed on listview
     */
    public static class CustomRow {
        private final int id;
        private final String name;
        private final String registerDate;
        private final String email;
        private final boolean premium;

        public CustomRow(int id, String name, String registerDate, String email, boolean premium) {
            this.id = id;
            this.name = name;
            this.registerDate = registerDate;
            this.email = email;
            this.premium = premium;
        }

        public int getId() {
            return id;
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

    /**
     * Class of custom cell displayed in row on listview
     */
    private static class CustomListCell extends ListCell<CustomRow> {
        private final HBox content;
        private final Text id;
        private final Text name;
        private Text date;
        private final Text email;
        private final Text premium;

        public CustomListCell() {
            super();
            id = new Text();
            name = new Text();
            date = new Text();
            email = new Text();
            date = new Text();
            premium = new Text();
            content = new HBox(id, name, email, date, premium);
            content.setSpacing(10);
            content.setPrefHeight(50);
            content.setPadding(new Insets(20, 20, 20, 20));
            HBox.setHgrow(name, Priority.ALWAYS);
            HBox.setHgrow(email, Priority.ALWAYS);
            HBox.setHgrow(date, Priority.ALWAYS);
            HBox.setHgrow(premium, Priority.ALWAYS);
        }

        @Override
        protected void updateItem(CustomRow item, boolean empty) {
            super.updateItem(item, empty);
            if(item != null && !empty) {
                id.setText(String.valueOf(item.getId()));
                name.setText(item.getName());
                date.setText(item.getRegisterDate());
                email.setText(item.getEmail());
                premium.setText(item.isPremium()?"Premium":"");
                setGraphic(content);
            } else {
                setGraphic(null);
            }
        }
    }
}
