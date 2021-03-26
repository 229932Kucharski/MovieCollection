package model.account;

import java.time.LocalDate;
import java.util.Date;

public abstract class User extends Account{

    private String email;
    private char gender;
    private LocalDate birthDate;

    public User(String name, String password, String email, char gender, LocalDate birthDate) {
        super(name, password);
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }


}
