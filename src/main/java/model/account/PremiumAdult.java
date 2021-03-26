package model.account;

import java.time.LocalDate;
import java.util.Date;

public class PremiumAdult extends Adult {

    byte[] avatar;

    public PremiumAdult(String name, String password, String email, char gender, LocalDate birthDate, String phoneNumber) {
        super(name, password, email, gender, birthDate, phoneNumber);
    }
}
