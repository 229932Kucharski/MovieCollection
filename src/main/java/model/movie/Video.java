package model.movie;

import java.time.LocalDate;
import java.util.List;

public abstract class Video {
    protected int id;
    protected String title;
    protected String country;
    protected Genres genre;
    protected String director;
    protected byte[] cover;
    protected LocalDate premiereDate;
    protected String description;
    protected Double averageRate;
    protected Integer ageRestriction;
    protected List<Comment> comments;


    public Video(int id, String title, String country, Genres genre, String director, byte[] cover, LocalDate premiereDate, String description, Double averageRate, Integer ageRestriction) {
        this.id = id;
        setTitle(title);
        setCountry(country);
        setGenre(genre);
        setDirector(director);
        setCover(cover);
        setPremiereDate(premiereDate);
        setDescription(description);
        setAverageRate(averageRate);
        setAgeRestriction(ageRestriction);
    }

    public void addComment(Comment comment) {
        comments.add(0, comment);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getId() {
        return id;
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

    public byte[] getCover() {
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

    public void setTitle(String title) {
        if(title.length() == 0) {
            throw new IllegalArgumentException("Tytuł nie może być pusty");
        }
        this.title = title;
    }

    public void setCountry(String country) {
        if(country.length() == 0) {
            throw new IllegalArgumentException("Nazwa kraju nie może być pusta");
        }
        this.country = country;
    }

    public void setGenre(Genres genre) {
        this.genre = genre;
    }

    public void setDirector(String director) {
        if(director.length() == 0) {
            throw new IllegalArgumentException("Nazwa reżysera nie może być pusta");
        }
        this.director = director;
    }

    public void setCover(byte[] cover) {
        this.cover = cover;
    }

    public void setPremiereDate(LocalDate premiereDate) {
        this.premiereDate = premiereDate;
    }

    public void setDescription(String description) {
        if(description.length() == 0) {
            throw new IllegalArgumentException("Opis nie może być pusty");
        }
        this.description = description;
    }

    public void setAverageRate(Double averageRate) {
        this.averageRate = averageRate;
    }

    public void setAgeRestriction(Integer ageRestriction) {
        if(ageRestriction < 0 || ageRestriction > 18) {
            throw new IllegalArgumentException("Podano nieprawidłowe ograniczenie wiekowe");
        }
        this.ageRestriction = ageRestriction;
    }

    @Override
    public String toString() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(this)
                .append("id", id)
                .append("title", title)
                .append("country", country)
                .append("genre", genre)
                .append("director", director)
                .append("premiereDate", premiereDate)
                .append("description", description)
                .append("averageRate", averageRate)
                .append("ageRestriction", ageRestriction)
                .toString();
    }
}
