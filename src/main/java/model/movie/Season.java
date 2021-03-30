package model.movie;

import java.time.LocalDate;

public class Season {
    private Integer numberOfEpisodes;
    private LocalDate seasonPremiere;

    public Season(Integer numberOfEpisodes, LocalDate seasonPremiere) {
        this.numberOfEpisodes = numberOfEpisodes;
        this.seasonPremiere = seasonPremiere;
    }

    public Integer getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public LocalDate getSeasonPremiere() {
        return seasonPremiere;
    }
}
