package model.account;

import java.util.Date;

public class Kid extends User {
    public Kid(String name, byte[] password, String email, char gender, Date birthDate) {
        super(name, password, email, gender, birthDate);
    }
}
