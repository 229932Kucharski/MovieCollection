package model.account;

import java.time.LocalDate;

public class Kid extends User {
    public Kid(String name, String password, String email, char gender, LocalDate birthDate) {
        super(name, password, email, gender, birthDate);
    }

    @Override
    public String welcomeText() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hello ").append(this.getName()).append("!");
        return sb.toString();
    }
}
