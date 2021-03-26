package model.account;

import model.account.password.PasswordHashing;
import org.junit.Test;
import org.junit.Assert;

import java.time.LocalDate;
import java.util.Arrays;

public class AdultTest {

    private final LocalDate date = LocalDate.of(1999, 10, 16);

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
    public void passwordTest() {
        Adult user = new Adult("Jan", "qwerty", "test@wp.pl", 'M', date, "111222333");
        Assert.assertArrayEquals(user.getPassword(), PasswordHashing.hashPassword("qwerty"));
        Assert.assertFalse(Arrays.equals(user.getPassword(), PasswordHashing.hashPassword("qwert")));
    }

    @Test
    public void welcomeTextTest() {
        Adult user = new Adult("Jan", "qwerty", "test@wp.pl", 'M', date, "111222333");
        System.out.println(user.welcomeText());

    }
}
