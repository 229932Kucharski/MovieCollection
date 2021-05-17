package manager;

import javafx.scene.image.Image;
import model.account.user.User;
import model.dao.*;
import model.movie.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
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
     * Add movie
     */
    public static void addMovie(int id, String title, String country, Genres genre, String director,
                                byte[] cover, LocalDate date, String description, double rate,
                                int age, int time) {
        Movie movie;
        if(time == 0 || time > 55) {
            movie = new FullLengthFilm(0, title, country, genre, director, cover, date, description, rate, age, time);
        } else {
            movie = new ShortFilm(0, title, country, genre, director, cover, date, description, rate, age, time);
        }
        try(JdbcMovieDao movieDao = new JdbcMovieDao()) {
            movieDao.add(movie);
        } catch (SQLException e) {
            logger.warn("Cant add movie");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add comment to pickedMovie
     */
    public static void addComment(String content) {
        if(content.equals("")) {
            return;
        }
        int movieId = MovieManager.getPickedMovie().getId();
        int userId = UserManager.getLoggedUser().getUserId();
        Comment comment = new Comment(1, userId, movieId, content, LocalDate.now());
        try(JdbcCommentDao commentDao = new JdbcCommentDao()) {
            commentDao.add(comment);
        } catch (Exception e) {
            logger.warn("Cant insert comment to database");
        }
        getPickedMovie().addComment(comment);
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
        JdbcUserFav jdbcUserFav = new JdbcUserFav();
        return jdbcUserFav.isFavVideo(user.getUserId(),
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
     * Delete movie, comments, rates and favourite list
     */
    public static void deleteMovie() {
        Movie movie = getPickedMovie();
        try(JdbcMovieDao movieDao = new JdbcMovieDao()) {
            try(JdbcCommentDao commentDao = new JdbcCommentDao()) {
                commentDao.deleteCommentsFromMovie(movie.getId());
            }
            try(JdbcUserFav userFav = new JdbcUserFav()) {
                userFav.deleteForMovie(movie.getId());
            }
            try(JdbcUserRates userRates = new JdbcUserRates()) {
                userRates.deleteForMovie(movie.getId());
            }
            movieDao.delete(movie);
        } catch (SQLException e) {
            logger.warn("Cant delete movie");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete picked comment
     */
    public static void deleteComment() {
        if(UserManager.isAdmin()) {
            try(JdbcCommentDao commentDao = new JdbcCommentDao()) {
                commentDao.delete(commentDao.findById(MovieManager.getPickedComment()));
            } catch (SQLException e) {
                logger.warn("Cant delete comment");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Add movie to logged user favourite list
     */
    public static void addToFavourite() {
        try(JdbcUserFav jdbcUserFav = new JdbcUserFav()) {
            jdbcUserFav.add(UserManager.getLoggedUser().getUserId(), getPickedMovie().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add movie to logged user favourite list
     */
    public static void delFromFavourite() {
        try(JdbcUserFav jdbcUserFav = new JdbcUserFav()) {
            jdbcUserFav.delete(UserManager.getLoggedUser().getUserId(), MovieManager.getPickedMovie().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Rate movie
     */
    public static void rateMovie(int rate) {
        try(JdbcUserRates userRates = new JdbcUserRates()) {
            userRates.deleteOfUser(UserManager.getLoggedUser().getUserId());
            userRates.add(UserManager.getLoggedUser().getUserId(), MovieManager.getPickedMovie().getId(), rate);
        } catch (SQLException e) {
            logger.warn("Cant rate movie");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

}
