package model.movie;


import java.time.LocalDate;

public class Series extends Video{
    private Integer numberOfSeasons;

    public Series(int id, String title, String country, Genres genre, String director, byte[] cover, LocalDate premiereDate, String description, Double averageRate, Integer ageRestriction, Integer numberOfSeasons) {
        super(id, title, country, genre, director, cover, premiereDate, description, averageRate, ageRestriction);
        setNumberOfSeasons(numberOfSeasons);
    }

    public Integer getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(Integer numberOfSeasons) {
        if(numberOfSeasons <= 0) {
            throw new IllegalArgumentException("Liczba sezonów nie może być <= 0");
        }
        this.numberOfSeasons = numberOfSeasons;
    }
}
