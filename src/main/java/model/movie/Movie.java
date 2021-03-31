package model.movie;


import javafx.scene.image.Image;

import java.time.LocalDate;

public class Movie extends Video {
    private Integer timeDuration;



    public Movie(String title, String country, Genres genre, String director, Image cover, LocalDate premiereDate, String description, Double averageRate, Integer ageRestriction, Integer timeDuration) {
        super(title, country, genre, director, cover, premiereDate, description, averageRate, ageRestriction);
        setTimeDuration(timeDuration);
    }

    public Integer getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(Integer timeDuration) {
        if(timeDuration <= 0) {
            throw new IllegalArgumentException("Czas trwania filmu nie moe byc zerowy");
        }
        this.timeDuration = timeDuration;
    }
}
