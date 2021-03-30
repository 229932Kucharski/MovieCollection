package model.movie;

import java.awt.*;
import java.time.LocalDate;

public class Movie extends Video {
    private Integer timeDuration;

    public Movie(String title, String country, Genres genre, String director, Image cover, LocalDate premiereDate, String description, Double averageRate, Integer ageRestriction, Integer timeDuration) {
        super(title, country, genre, director, cover, premiereDate, description, averageRate, ageRestriction);
        this.timeDuration = timeDuration;
    }

    public Integer getTimeDuration() {
        return timeDuration;
    }
}
