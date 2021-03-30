package model.account;

import org.junit.Assert;
import org.junit.Test;
import java.time.LocalDate;

public class UserTest {

    private final LocalDate date = LocalDate.of(1999, 10, 16);
    private final LocalDate date2 = LocalDate.of(2005, 10, 16);
    private final LocalDate date3 = LocalDate.of(LocalDate.now().getYear() - 5, 10, 16);

    @Test
    public void createUserTest() {
        Adult user = new Adult("Jan", "qwerty", "test@wp.pl", 'M', date, "111222333");
        Assert.assertEquals(user.getName(), "Jan");
        Assert.assertNotNull(user.getPassword());
        Assert.assertEquals(user.getEmail(), "test@wp.pl");
        Assert.assertEquals(user.getGender(), 'M');
        Assert.assertEquals(user.getBirthDate(), date);
        Assert.assertEquals(user.getPhoneNumber(), "111222333");
    }


    @Test
    public void welcomeTextTest() {
        Adult user = new Adult("Jan", "qwerty", "test@wp.pl", 'M', date, "111222333");
        Kid user2 = new Kid("Jan", "qwerty", "test@wp.pl", 'M', date2);
        Assert.assertNotEquals(user.welcomeText(), user2.welcomeText());
    }



    @Test
    public void wrongBirthDayTest() {
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            Kid user = new Kid("Jan", "qwerty", "test@wp.pl", 'M', date3);
        });
        Kid user = new Kid("Jan", "qwerty", "test@wp.pl", 'M', date2);
        Assert.assertEquals("Jan", user.getName());
    }

}
