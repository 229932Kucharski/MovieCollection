package model.movie;

import java.awt.*;
import java.time.LocalDate;

public abstract class Video {
    protected String title;
    protected String country;
    protected Genres genre;
    protected String director;
    protected Image cover;
    protected LocalDate premiereDate;
    protected String description;
    protected Double averageRate;
    protected Integer ageRestriction;

    public Video(String title, String country, Genres genre, String director, Image cover, LocalDate premiereDate, String description, Double averageRate, Integer ageRestriction) {
        this.title = title;
        this.country = country;
        this.genre = genre;
        this.director = director;
        this.cover = cover;
        this.premiereDate = premiereDate;
        this.description = description;
        this.averageRate = averageRate;
        this.ageRestriction = ageRestriction;
    }

    public void addComment(){
    }
    public void deleteComment(Integer id) {
    }

    public String getTitle() {
        return title;
    }

    public String getCountry() {
        return country;
    }

    public Genres getGenre() {
        return genre;
    }

    public String getDirector() {
        return director;
    }

    public Image getCover() {
        return cover;
    }

    public LocalDate getPremiereDate() {
        return premiereDate;
    }

    public String getDescription() {
        return description;
    }

    public Double getAverageRate() {
        return averageRate;
    }

    public Integer getAgeRestriction() {
        return ageRestriction;
    }

}
