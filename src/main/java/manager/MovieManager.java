package manager;

import javafx.scene.image.Image;
import model.account.user.User;
import model.dao.JdbcFavourite;
import model.dao.JdbcMovieDao;
import model.dao.JdbcUserRates;
import model.movie.Genres;
import model.movie.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for managing movies
 */
public class MovieManager {

    private static final Logger logger = LoggerFactory.getLogger(MovieManager.class);
    private static Movie pickedMovie;
    private static int pickedComment;
    private static List<Movie> movies;

    /**
     * Get all movies
     */
    public static List<Movie> getMovies() {
        return movies;
    }

    /**
     * Get picked movie on listview
     */
    public static Movie getPickedMovie() {
        return pickedMovie;
    }

    /**
     * Get picked comment (admin feature)
     */
    public static int getPickedComment() {
        return pickedComment;
    }

    /**
     * Set picked comment (admin feature)
     */
    public static void setPickedComment(int pickedComment) {
        MovieManager.pickedComment = pickedComment;
    }

    /**
     * Set movies
     */
    public static void setMovies(List<Movie> movies) {
        MovieManager.movies = movies;
    }

    /**
     * Set picked movie
     */
    public static void setPickedMovie(Movie movie) {
        MovieManager.pickedMovie = movie;
    }

    /**
     * Take movies from DB and set them
     */
    public static void setMovies() {
        try (JdbcMovieDao movieDao = new JdbcMovieDao()) {
            movies = movieDao.findAll();
            MovieManager.setMovies(movies);
        } catch (SQLException e) {
            logger.warn("Cant get movies from database");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if picked movie is add to user favourite list
     */
    public static boolean isMovieFav(User user) throws SQLException {
        JdbcFavourite jdbcFavourite = new JdbcFavourite();
        return jdbcFavourite.isFavVideo(user.getUserId(),
                MovieManager.getPickedMovie().getId());
    }

    /**
     * Check user rate for movie
     */
    public static int getUserRate(User user) throws SQLException {
        JdbcUserRates userRates = new JdbcUserRates();
        return userRates.findRateForMovie(user.getUserId(), pickedMovie.getId());
    }

    /**
     * Get average rate for movie
     */
    public static double getAvgRate() throws SQLException {
        JdbcUserRates userRates = new JdbcUserRates();
        List<Integer> rates = userRates.getRatesForMovie(pickedMovie.getId());
        if(rates.size() == 0) {
            return 0;
        }
        double sum = 0;
        for(Integer rate : rates) {
            sum += rate;
        }
        return Math.round((sum / rates.size()) * 100.0) / 100.0;
    }

    /**
     * Search movie by title, year and genre
     * @param title search phrase
     * @param year search year
     * @param genre search genre
     * @return list of movies that contain search phrase
     */
    public static List<Movie> getMoviesBySearch(String title, String year, Genres genre) {
        List<Movie> tempMovies = new ArrayList<>();
        for(Movie movie : movies) {
            if(movie.getTitle().toLowerCase().contains(title.toLowerCase())) {
                tempMovies.add(movie);
            }
        }
        if(!year.equals("")) {
            List<Movie> tempMovies2 = new ArrayList<>();
            for(Movie movie : tempMovies) {
                if(movie.getPremiereDate().getYear() == Integer.parseInt(year)) {
                    tempMovies2.add(movie);
                }
            }
            tempMovies = tempMovies2;
        }

        if(!genre.equals(Genres.All)) {
            List<Movie> tempMovies2 = new ArrayList<>();
            for(Movie movie : tempMovies) {
                if(movie.getGenre().equals(genre)) {
                    tempMovies2.add(movie);
                }
            }
            tempMovies = tempMovies2;
        }

        return tempMovies;
    }

    /**
     * Get specific movie
     * @param title search phrase
     * @return movie with title like param
     */
    public static Movie getMovieByTitle(String title) {
        for(Movie movie : movies) {
            if(movie.getTitle().equals(title)) {
                return movie;
            }
        }
        return null;
    }

    /**
     * Get cover image for movie
     * @param movie movie object
     * @return cover image for specific movie
     */
    public static Image getImage(Movie movie) {
        if(movie.getCover() == null) {
            return new Image("/img/noCover.jpg");
        } else {
            File imageFile = new File("src/main/assets/img/movieCover/" + movie.getId() + ".jpg");
            return new Image(imageFile.toURI().toString());
        }
    }
}
