package controller;

import app.App;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.util.Callback;
import model.account.Account;
import model.account.user.Adult;
import model.account.user.Kid;
import model.account.user.PremiumAdult;
import model.account.user.User;
import model.dao.JdbcCommentDao;
import model.dao.JdbcUserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
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

    public void initialize() throws IOException {
        UserController.setPickedUser(null);
        try (JdbcUserDao userDao = new JdbcUserDao()) {
            users = userDao.findAll();
            UserController.setUsers(users);
        } catch (SQLException e) {
            logger.warn("Cant get users from database");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        addUsersToListView();
    }

    private void addUsersToListView() throws IOException {
        listView = new ListView<>(userList);
        listView.getItems().clear();
        listView.setPrefSize(200, 500);
        listView.setEditable(true);
        boolean isPremium;
        for(User user : users) {
            isPremium = user instanceof PremiumAdult;
            userList.add(new CustomRow(user.getUserId(), user.getName(), user.getRegisterDate().toString(), user.getEmail(), isPremium));
        }
        listView.setItems(userList);
        vBoxList.getChildren().add(listView);
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
                    User user = UserController.getUsersByName(t1.getName()).get(0);
                    if(user instanceof Kid || user instanceof PremiumAdult)  {
                        promoteButton.setDisable(true);
                    } else {
                        promoteButton.setDisable(false);
                    }
                    UserController.setPickedUser(UserController.getUsersByName(t1.getName()).get(0));
                }
            }
        });
    }

    public void previous() {
        App.changeScene(mainAnchorPane, "mainWindow");
    }

    public void searchByName() throws IOException {
        String username = usernameTextField.getText();
        users = UserController.getUsersByName(username);
        vBoxList.getChildren().remove(listView);
        addUsersToListView();
    }

    public void deleteUser() throws IOException {
        if(UserController.getPickedUser() == null) {
            return;
        }
        try(JdbcUserDao userDao = new JdbcUserDao()) {
            try(JdbcCommentDao commentDao = new JdbcCommentDao()) {
                commentDao.deleteCommentsOfUser(UserController.getPickedUser().getUserId());
            }
            userDao.delete(UserController.getPickedUser());
            UserController.setUsers(userDao.findAll());
        } catch (SQLException e) {
            logger.warn("Cant delete user");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        vBoxList.getChildren().remove(listView);
        UserController.setPickedUser(null);
        users = UserController.getUsers();
        addUsersToListView();
    }

    public void promoteUser() throws IOException {
        if(UserController.getPickedUser() instanceof Kid || UserController.getPickedUser() instanceof PremiumAdult) {
            return;
        }
        Adult user = (Adult)UserController.getPickedUser();
        PremiumAdult premiumAdult = new PremiumAdult(user.getUserId(), user.getName(), user.getPassword(), user.getEmail(),
                user.getGender(), user.getBirthDate(), user.getPhoneNumber());
        try(JdbcUserDao userDao = new JdbcUserDao()){
            userDao.update(premiumAdult);
            UserController.setUsers(userDao.findAll());
        } catch (SQLException e) {
            logger.warn("Cant promote user");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        vBoxList.getChildren().remove(listView);
        UserController.setPickedUser(null);
        users = UserController.getUsers();
        addUsersToListView();
    }

    // KLASA WIERSZA
    public static class CustomRow {
        private int id;
        private String name;
        private String registerDate;
        private String email;
        private boolean premium;

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
    // KLASA KOMORKI
    private class CustomListCell extends ListCell<CustomRow> {
        private HBox content;
        private Text id;
        private Text name;
        private Text date;
        private Text email;
        private Text premium;

        public CustomListCell() {
            super();
            id = new Text();
            name = new Text();
            date = new Text();
            email = new Text();
            date = new Text();
            premium = new Text();
            content = new HBox(id, name, email, date, premium);
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
