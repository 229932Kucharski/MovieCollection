package manager;

import model.account.password.PasswordHashing;
import model.account.user.Adult;
import model.account.user.PremiumAdult;
import model.account.user.User;
import model.dao.JdbcCommentDao;
import model.dao.JdbcUserFav;
import model.dao.JdbcUserDao;
import model.dao.JdbcUserRates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
     * Login user
     * @param login user login
     * @param password user password
     * @return boolean if user log in successfully
     */
    public static boolean loginUser(String login, String password) {
        User user;
        //Check if user login exist in database
        try(JdbcUserDao userDao = new JdbcUserDao()) {
            user = userDao.findByName(login);
        } catch (Exception e) {
            return false;
        }
        //Check if password is correct
        if(user == null || !Arrays.equals(user.getPassword(), PasswordHashing.hashPassword(password))) {
            return false;
        }
        UserManager.setLoggedUser(user);
        logger.info("User " + user.getName() + " has logged in");
        return true;
    }

    /**
     * Check if login is available
     */
    public static boolean isLoginAvailable(String login) {
        User user = null;
        try (JdbcUserDao userDao = new JdbcUserDao()) {
            user = userDao.findByName(login);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user == null;
    }

    /**
     * Sign up new user
     */
    public static void signUpUser(User user) {
        try (JdbcUserDao userDao = new JdbcUserDao()) {
            logger.info("Adding new user");
            userDao.add(user);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Cant add new user");
        }
    }

    /**
     * Change current password to new one
     * @param newPass new password
     */
    public static void changePassword(String newPass) {
        UserManager.getLoggedUser().setPassword(newPass);
        try {
            JdbcUserDao userDao = new JdbcUserDao();
            userDao.update(UserManager.getLoggedUser());
        } catch (SQLException e) {
            logger.warn("Cant change password");
            e.printStackTrace();
        }
    }

    /**
     * Promote user to premium account
     */
    public static void promoteUser(Adult user) {
        PremiumAdult premiumAdult = new PremiumAdult(user.getUserId(), user.getName(), user.getPassword(),
                user.getRegisterDate(), user.getEmail(), user.getGender(), user.getBirthDate(), user.getPhoneNumber());
        try(JdbcUserDao userDao = new JdbcUserDao()){
            userDao.update(premiumAdult);
            UserManager.setUsers(userDao.findAll());
        } catch (SQLException e) {
            logger.warn("Cant promote user");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
     * Delete user. Param user can be null, then method takes logged user.
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
            try(JdbcUserFav userFav = new JdbcUserFav()) {
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
     * Logout user
     */
    public static void logout() {
        loggedUser = null;
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




}
