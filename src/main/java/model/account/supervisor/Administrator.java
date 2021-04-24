package model.account.supervisor;
import model.account.user.User;

import java.time.LocalDate;

public class Administrator extends User {
    public Administrator(int id, String name, byte[] password, String email, char gender, LocalDate birthDate) {
        super(id, name, password, email, gender, birthDate);
    }

    @Override
    public String welcomeText() {
        return "GOD MODE";
    }
}
