package model.movie;


import java.time.LocalDate;

public class FullLengthFilm extends Movie {
    public FullLengthFilm(int id, String title, String country, Genres genre, String director, byte[] cover, LocalDate premiereDate, String description, Double averageRate, Integer ageRestriction, Integer timeDuration) {
        super(id, title, country, genre, director, cover, premiereDate, description, averageRate, ageRestriction, timeDuration);
    }
}
