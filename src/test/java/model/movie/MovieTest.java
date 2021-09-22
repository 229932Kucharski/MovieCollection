package model.movie;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import java.time.LocalDate;

public class MovieTest {


    private static LocalDate date;
    private static Movie movie;

    @BeforeAll
    static void init() {
        date = LocalDate.of(2005, 10, 16);

    }


    @Test
    public void shouldCorrectlyCreateMovie() {
        movie = new FullLengthFilm(1, "Titanic", "USA", Genres.Romance, "Director", null,
                date, "bla bla bla", 7.0, 12, 120);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenTitleIsEmpty() {
        movie = new FullLengthFilm(1, "", "USA", Genres.Romance, "Director", null,
                date, "bla bla bla", 7.0, 12, 120);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenCountryIsEmpty() {
        movie = new FullLengthFilm(1, "Titanic", "", Genres.Romance, "Director", null,
                date, "bla bla bla", 7.0, 12, 120);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenDirectorIsEmpty() {
        movie = new FullLengthFilm(1, "Titanic", "USA", Genres.Romance, "", null,
                date, "bla bla bla", 7.0, 12, 120);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenDescriptionIsEmpty() {
        movie = new FullLengthFilm(1, "Titanic", "USA", Genres.Romance, "Director", null,
                date, "", 7.0, 12, 120);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenAgeIsWrong() {
        movie = new FullLengthFilm(1, "Titanic", "USA", Genres.Romance, "Director", null,
                date, "content", 7.0, -2, 120);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenAgeIsWrong2() {
        movie = new FullLengthFilm(1, "Titanic", "USA", Genres.Romance, "Director", null,
                date, "content", 7.0, 20, 120);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenTimeIsBelowZero() {
        movie = new FullLengthFilm(1, "Titanic", "USA", Genres.Romance, "Director", null,
                date, "content", 7.0, 10, -1);
    }

}
