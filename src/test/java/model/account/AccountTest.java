package model.account;

import model.account.password.PasswordHashing;
import model.account.user.Adult;
import model.account.user.Kid;
import model.account.user.PremiumAdult;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.time.LocalDate;

public class AccountTest {

    private static final LocalDate date = LocalDate.of(1999, 10, 16);
    private static final LocalDate date2 = LocalDate.of(2005, 10, 16);
    private static final LocalDate regDate = LocalDate.of(2019, 10, 16);
    private static final LocalDate date3 = LocalDate.of(LocalDate.now().getYear() - 5, 10, 16);

    private static Adult adult;
    private static PremiumAdult premiumAdult;
    private static Kid kid;


    @BeforeAll
    static void init() {
        adult = new Adult(1, "Jan", "qwerty", regDate, "test@wp.pl", 'M', date, "111222333");
        premiumAdult = new PremiumAdult(1, "Jan", PasswordHashing.hashPassword("qwerty"), regDate,
                "test@wp.pl", 'M', date, "111222333");
        kid = new Kid(2, "Adam", "qwerty", regDate, "test2@wp.pl", 'M', date);
    }


    @Test
    public void createAdultUserTest() {
        Assert.assertEquals(adult.getName(), "Jan");
        Assert.assertNotNull(adult.getPassword());
        Assert.assertEquals(adult.getEmail(), "test@wp.pl");
        Assert.assertEquals(adult.getGender(), 'M');
        Assert.assertEquals(adult.getRegisterDate(), regDate);
        Assert.assertEquals(adult.getBirthDate(), date);
        Assert.assertEquals(adult.getPhoneNumber(), "111222333");
    }

    @Test
    public void createKidUserTest() {
        Assert.assertEquals(kid.getName(), "Adam");
        Assert.assertNotNull(kid.getPassword());
        Assert.assertEquals(kid.getEmail(), "test2@wp.pl");
        Assert.assertEquals(kid.getGender(), 'M');
        Assert.assertEquals(kid.getRegisterDate(), regDate);
        Assert.assertEquals(kid.getBirthDate(), date);
    }


    @Test
    public void generateWelcomeTextTest() {
        Assert.assertNotEquals(adult.welcomeText(), kid.welcomeText());
        Assert.assertNotEquals(adult.welcomeText(), premiumAdult.welcomeText());
    }


    @Test
    public void wrongAndCorrectBirthDayTest() {
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            Kid user = new Kid(2, "Jan", "qwerty", regDate, "test2@wp.pl", 'M', date3);
        });
        Kid user = new Kid(2, "Jan", "qwerty", regDate, "test2@wp.pl", 'M', date2);
    }

}
