package model.movie;

import java.awt.*;
import java.time.LocalDate;

public class ShortFilm extends Movie{
    public ShortFilm(String title, String country, Genres genre, String director, Image cover, LocalDate premiereDate, String description, Double averageRate, Integer ageRestriction, Integer timeDuration) {
        super(title, country, genre, director, cover, premiereDate, description, averageRate, ageRestriction, timeDuration);
    }
}
