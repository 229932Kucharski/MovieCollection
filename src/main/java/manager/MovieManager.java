package manager;

import javafx.scene.image.Image;
import model.dao.JdbcMovieDao;
import model.movie.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
     * Search movie
     * @param title search phrase
     * @return list of movies that contain search phrase
     */
    public static List<Movie> getMoviesBySearch(String title) {
        List<Movie> tempMovies = new ArrayList<>();
        for(Movie movie : movies) {
            if(movie.getTitle().toLowerCase().contains(title.toLowerCase())){
                tempMovies.add(movie);
            }
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
