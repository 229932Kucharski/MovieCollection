package controller;

import model.movie.Movie;

import java.util.List;

public class MovieController {

    private static Movie pickedMovie;
    private static List<Movie> movies;

    public static List<Movie> getMovies() {
        return movies;
    }

    public static void setMovies(List<Movie> movies) {
        MovieController.movies = movies;
    }

    public static Movie getMovieByTitle(String title) {
        for(Movie movie : movies) {
            if(movie.getTitle().equals(title)) {
                return movie;
            }
        }
        return null;
    }

    public static Movie getPickedMovie() {
        return pickedMovie;
    }

    public static void setPickedMovie(Movie movie) {
        MovieController.pickedMovie = movie;
    }
}
