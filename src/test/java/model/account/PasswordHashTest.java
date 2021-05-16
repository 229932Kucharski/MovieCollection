package model.account;

import model.account.password.PasswordHashing;
import model.account.user.Adult;
import model.account.user.Kid;
import org.junit.Assert;
import org.junit.Test;
import java.time.LocalDate;
import java.util.Arrays;

public class PasswordHashTest {

    private final LocalDate date = LocalDate.of(1999, 10, 16);

    @Test
    public void passwordTest() {
        Adult user = new Adult(1, "Jan", "qwerty", LocalDate.now(),"test@wp.pl", 'M', date, "111222333");
        Assert.assertArrayEquals(user.getPassword(), PasswordHashing.hashPassword("qwerty"));
        Assert.assertFalse(Arrays.equals(user.getPassword(), PasswordHashing.hashPassword("qwert")));
//        Assert.assertThrows(IllegalArgumentException.class, () -> {
//            user.setPassword("qwer");
//        });
    }


}
