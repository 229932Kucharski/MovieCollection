package model.account;

import java.util.Date;

public class PremiumAdult extends Adult {

    byte[] avatar;

    public PremiumAdult(String name, byte[] password, String email, char gender, Date birthDate, String phoneNumber) {
        super(name, password, email, gender, birthDate, phoneNumber);
    }
}
