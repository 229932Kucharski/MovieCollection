package controller;

import model.account.Account;
import model.account.user.User;

import java.util.ArrayList;
import java.util.List;

public class UserController {

    private static User loggedUser;
    private static List<User> users;

    public static List<User> getUsers() {
        return users;
    }

    public static boolean isAdmin() {
        return loggedUser.getName().equals("admin");
    }

    public static void setUsers(List<Account> accounts) {
        List<User> users = new ArrayList<>();
        for(Account a : accounts) {
            users.add((User) a);
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
