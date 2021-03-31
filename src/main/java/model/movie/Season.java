package model.movie;

import java.time.LocalDate;

public class Season {
    private Integer numberOfEpisodes;
    private LocalDate seasonPremiere;

    public Season(Integer numberOfEpisodes, LocalDate seasonPremiere) {
        setNumberOfEpisodes(numberOfEpisodes);
        setSeasonPremiere(seasonPremiere);
    }

    public Integer getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public LocalDate getSeasonPremiere() {
        return seasonPremiere;
    }

    public void setNumberOfEpisodes(Integer numberOfEpisodes) {
        if(numberOfEpisodes <= 0) {
            throw new IllegalArgumentException("Liczba odcinków nie może byc <= 0");
        }
        this.numberOfEpisodes = numberOfEpisodes;
    }

    public void setSeasonPremiere(LocalDate seasonPremiere) {
        this.seasonPremiere = seasonPremiere;
    }
}
