package model.movie;

import java.awt.*;
import java.time.LocalDate;

public class Series extends Video{
    private Integer numberOfSeasons;

    public Series(String title, String country, Genres genre, String director, Image cover, LocalDate premiereDate, String description, Double averageRate, Integer ageRestriction, Integer numberOfSeasons) {
        super(title, country, genre, director, cover, premiereDate, description, averageRate, ageRestriction);
        this.numberOfSeasons = numberOfSeasons;
    }

    public Integer getNumberOfSeasons() {
        return numberOfSeasons;
    }
}
