package controller;

import model.account.user.User;

public class UserController {

    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        UserController.user = user;
    }

    public static void logout() {
        user = null;
    }
}
