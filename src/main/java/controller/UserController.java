package controller;

import model.account.Account;
import model.account.user.User;
import model.movie.Movie;

import java.util.ArrayList;
import java.util.List;

public class UserController {

    private static User loggedUser;
    private static User pickedUser;
    private static List<User> users;

    public static User getPickedUser() {
        return pickedUser;
    }

    public static void setPickedUser(User pickedUser) {
        UserController.pickedUser = pickedUser;
    }

    public static List<User> getUsers() {
        return users;
    }

    public static boolean isAdmin() {
        return loggedUser.getName().equals("admin");
    }

    public static List<User> getUsersByName(String name) {
        List<User> tempUsers = new ArrayList<>();
        for(User user : users) {
            if(user.getName().toLowerCase().contains(name.toLowerCase())){
                tempUsers.add(user);
            }
        }
        return tempUsers;
    }

    public static void setUsers(List<User> accounts) {
        List<model.account.user.User> users = new ArrayList<>();
        for(Account a : accounts) {
            users.add((model.account.user.User) a);
        }
        UserController.users = users;
    }

    public static User getLoggedUser() {
        return loggedUser;
    }

    public static void setLoggedUser(User loggedUser) {
        UserController.loggedUser = loggedUser;
    }

    public static void logout() {
        loggedUser = null;
    }


}
