package model.account;

import model.account.user.Adult;
import model.account.user.Kid;
import model.account.user.PremiumAdult;
import org.junit.Assert;
import org.junit.Test;
import java.time.LocalDate;

public class AccountTest {

    private final LocalDate date = LocalDate.of(1999, 10, 16);
    private final LocalDate date2 = LocalDate.of(2005, 10, 16);
    private final LocalDate date3 = LocalDate.of(LocalDate.now().getYear() - 5, 10, 16);

    @Test
    public void createUserTest() {
        Adult user = new Adult(1, "Jan", "qwerty", "test@wp.pl", 'M', date, "111222333");
        Assert.assertEquals(user.getName(), "Jan");
        Assert.assertNotNull(user.getPassword());
        Assert.assertEquals(user.getEmail(), "test@wp.pl");
        Assert.assertEquals(user.getGender(), 'M');
        Assert.assertEquals(user.getBirthDate(), date);
        Assert.assertEquals(user.getPhoneNumber(), "111222333");
    }


    @Test
    public void welcomeTextTest() {
        Adult user = new Adult(1, "Jan", "qwerty", "test@wp.pl", 'M', date, "111222333");
        PremiumAdult user1 = new PremiumAdult(3, "Jan", "qwerty".getBytes(), "test@wp.pl", 'M', date, "111222333");
        Kid user2 = new Kid(2, "Jan", "qwerty", "test@wp.pl", 'M', date2);
        Assert.assertNotEquals(user.welcomeText(), user2.welcomeText());
        Assert.assertNotEquals(user.welcomeText(), user1.welcomeText());
    }


    @Test
    public void wrongBirthDayTest() {
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            Kid user = new Kid(1, "Jan", "qwerty", "test@wp.pl", 'M', date3);
        });
        Kid user = new Kid(2, "Jan", "qwerty", "test@wp.pl", 'M', date2);
        Assert.assertEquals("Jan", user.getName());
    }

}
