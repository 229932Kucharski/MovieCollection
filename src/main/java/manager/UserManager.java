package manager;

import model.account.user.PremiumAdult;
import model.account.user.User;

import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private static User loggedUser;
    private static User pickedUser;
    private static List<User> users;

    /**
     * Check if user has admin permission
     */
    public static boolean isAdmin() {
        return loggedUser.getName().equals("admin");
    }

    /**
     * Check if user has premium account
     */
    public static boolean isPremium() {
        return loggedUser instanceof PremiumAdult;
    }

    /**
     * Get logged user
     */
    public static User getLoggedUser() {
        return loggedUser;
    }

    /**
     * Get picked user from listView (admin feature)
     */
    public static User getPickedUser() {
        return pickedUser;
    }

    /**
     * Get all users
     */
    public static List<User> getUsers() {
        return users;
    }

    /**
     * Find users by name
     * @param name search phrase
     * @return List of users that contain specific name
     */
    public static List<User> getUsersByName(String name) {
        List<User> tempUsers = new ArrayList<>();
        for(User user : users) {
            if(user.getName().toLowerCase().contains(name.toLowerCase())){
                tempUsers.add(user);
            }
        }
        return tempUsers;
    }

    /**
     * Set users list
     */
    public static void setUsers(List<User> accounts) {
        UserManager.users = new ArrayList<>(accounts);
    }

    /**
     * Set picked user
     */
    public static void setPickedUser(User pickedUser) {
        UserManager.pickedUser = pickedUser;
    }

    /**
     * Set logged user
     */
    public static void setLoggedUser(User loggedUser) {
        UserManager.loggedUser = loggedUser;
    }

    /**
     * Logout user
     */
    public static void logout() {
        loggedUser = null;
    }


}
