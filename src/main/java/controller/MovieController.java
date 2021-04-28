package controller;

import javafx.scene.image.Image;
import model.dao.JdbcMovieDao;
import model.movie.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieController {

    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);
    private static Movie pickedMovie;
    private static int pickedComment;
    private static List<Movie> movies;

    public static List<Movie> getMovies() {
        return movies;
    }

    public static Movie getPickedMovie() {
        return pickedMovie;
    }

    public static int getPickedComment() {
        return pickedComment;
    }

    public static void setPickedComment(int pickedComment) {
        MovieController.pickedComment = pickedComment;
    }

    public static void setMovies(List<Movie> movies) {
        MovieController.movies = movies;
    }

    public static void setPickedMovie(Movie movie) {
        MovieController.pickedMovie = movie;
    }

    public static void setMovies() {
        try (JdbcMovieDao movieDao = new JdbcMovieDao()) {
            movies = movieDao.findAll();
            MovieController.setMovies(movies);
        } catch (SQLException e) {
            logger.warn("Cant get movies from database");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<Movie> getMoviesBySearch(String title) {
        List<Movie> tempMovies = new ArrayList<>();
        for(Movie movie : movies) {
            if(movie.getTitle().toLowerCase().contains(title.toLowerCase())){
                tempMovies.add(movie);
            }
        }
        return tempMovies;
    }

    public static Movie getMovieByTitle(String title) {
        for(Movie movie : movies) {
            if(movie.getTitle().equals(title)) {
                return movie;
            }
        }
        return null;
    }



    public static Image getImage(Movie movie) {
        if(movie.getCover() == null) {
            return new Image("/img/noCover.jpg");
        } else {
            return new Image("/img/movieCover/" + movie.getId() + ".jpg");
        }
    }
}
