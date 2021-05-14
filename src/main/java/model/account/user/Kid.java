package model.account.user;

import java.time.LocalDate;

public class Kid extends User {
    public Kid(int id, String name, String password, LocalDate regDate, String email, char gender, LocalDate birthDate) throws IllegalArgumentException{
        super(id, name, password, regDate, email, gender, birthDate);
    }

    public Kid(int id, String name, byte[] password, LocalDate regDate, String email, char gender, LocalDate birthDate) throws IllegalArgumentException{
        super(id, name, password, regDate, email, gender, birthDate);
    }

    @Override
    public String welcomeText() {
        return "Hello " + this.getName() + "!";
    }
}
