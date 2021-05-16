package manager;

import model.account.user.PremiumAdult;
import model.account.user.User;
import model.dao.JdbcCommentDao;
import model.dao.JdbcFavourite;
import model.dao.JdbcUserDao;
import model.dao.JdbcUserRates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for managing users
 */
public class UserManager {

    private static User loggedUser;
    private static User pickedUser;
    private static List<User> users;
    private static final Logger logger = LoggerFactory.getLogger(UserManager.class);

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
     * Method delete user from db. Param user can be null, then method takes logged user.
     * @param user user that you want to delete. If null, take logged user.
     */
    public static void deleteUser(User user) {
        if (user == null) {
            user = getLoggedUser();
            logout();
        }
        try(JdbcUserDao userDao = new JdbcUserDao()) {
            try(JdbcCommentDao commentDao = new JdbcCommentDao()) {
                commentDao.deleteCommentsOfUser(user.getUserId());
            }
            try(JdbcFavourite userFav = new JdbcFavourite()) {
                userFav.deleteOfUser(user.getUserId());
            }
            try(JdbcUserRates userRates = new JdbcUserRates()) {
                userRates.deleteOfUser(user.getUserId());
            }
            userDao.delete(user);
            UserManager.setUsers(userDao.findAll());
        } catch (SQLException e) {
            logger.warn("Cant delete user");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
