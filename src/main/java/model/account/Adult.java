package model.account;

import java.util.Date;

public class Adult extends User {

    private String phoneNumber;

    public Adult(String name, byte[] password, String email, char gender, Date birthDate, String phoneNumber) {
        super(name, password, email, gender, birthDate);
        this.phoneNumber = phoneNumber;
    }
}
