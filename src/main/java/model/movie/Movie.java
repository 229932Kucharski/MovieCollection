package model.movie;


import java.time.LocalDate;

public abstract class Movie extends Video {
    private Integer timeDuration;



    public Movie(int id, String title, String country, Genres genre, String director, byte[] cover, LocalDate premiereDate, String description, Double averageRate, Integer ageRestriction, Integer timeDuration) {
        super(id, title, country, genre, director, cover, premiereDate, description, averageRate, ageRestriction);
        setTimeDuration(timeDuration);
    }

    public Integer getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(Integer timeDuration) {
        if(timeDuration < 0) {
            throw new IllegalArgumentException("Czas trwania filmu nie moze byc mniejszy niz zero");
        }
        this.timeDuration = timeDuration;
    }
}
