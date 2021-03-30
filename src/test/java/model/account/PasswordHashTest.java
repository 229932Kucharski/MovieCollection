package model.account;

import model.account.password.PasswordHashing;
import org.junit.Assert;
import org.junit.Test;
import java.time.LocalDate;
import java.util.Arrays;

public class PasswordHashTest {

    private final LocalDate date = LocalDate.of(1999, 10, 16);

    @Test
    public void passwordTest() {
        Adult user = new Adult("Jan", "qwerty", "test@wp.pl", 'M', date, "111222333");
        Assert.assertArrayEquals(user.getPassword(), PasswordHashing.hashPassword("qwerty"));
        Assert.assertFalse(Arrays.equals(user.getPassword(), PasswordHashing.hashPassword("qwert")));
    }


}
