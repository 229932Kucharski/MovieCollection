package model.account;

import model.account.user.Adult;
import model.account.user.PremiumAdult;
import org.junit.Assert;
import org.junit.Test;
import java.time.LocalDate;

public class PremiumAdultTest {

    private final LocalDate date = LocalDate.of(1999, 10, 16);

    @Test
    public void welcomeTextTest() {
        PremiumAdult user = new PremiumAdult("Jan", "qwerty", "test@wp.pl", 'M', date, "111222333");
        Adult user2 = new Adult("Jan", "qwerty", "test@wp.pl", 'M', date, "111222333");
        Assert.assertNotEquals(user.welcomeText(), user2.welcomeText());
    }
}
