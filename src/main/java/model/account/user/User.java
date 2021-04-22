package model.account.user;

import model.account.Account;
import model.exception.AgeException;
import model.exception.EmailException;

import java.time.LocalDate;
import java.time.Period;

public abstract class User extends Account {

    private int userId;
    private String email;
    private char gender;
    private LocalDate birthDate;

    public User(int id, String name, String password, String email, char gender, LocalDate birthDate) throws IllegalArgumentException{
        super(name, password);
        this.userId = id;
        setEmail(email);
        setGender(gender);
        setBirthDate(birthDate);
    }

    public User(int id, String name, byte[] password, String email, char gender, LocalDate birthDate) throws IllegalArgumentException{
        super(name, password);
        this.userId = id;
        setEmail(email);
        setGender(gender);
        setBirthDate(birthDate);
    }

    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws EmailException{
        if(email == null || !email.contains("@")) {
            throw new EmailException("Wrong email");
        }
        this.email = email;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        if(gender == 'K' || gender == 'M' || gender == 'k' || gender == 'm') {
            this.gender = gender;
        } else {
            throw new IllegalArgumentException("Wrong gender");
        }
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) throws AgeException{
        LocalDate now = LocalDate.now();
        Period period = Period.between(birthDate, now);
        if(period.getYears() < 8) {
            throw new AgeException("You are too young");
        }
        this.birthDate = birthDate;
    }


}
