package model.movie;

public class Episode {
    private Integer timeDuration;

    public Episode(Integer timeDuration) {
        setTimeDuration(timeDuration);
    }

    public Integer getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(Integer timeDuration) {
        if(timeDuration <= 0) {
            throw new IllegalArgumentException("Czas trwania odcinka nie może byc <= 0");
        }
        this.timeDuration = timeDuration;
    }
}
