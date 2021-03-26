package model.account;

import java.util.Date;

public class User extends Account{

    private String email;
    private char gender;
    private Date birthDate;

    public User(String name, byte[] password, String email, char gender, Date birthDate) {
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
