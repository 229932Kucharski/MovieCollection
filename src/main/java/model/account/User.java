package model.account;

import java.time.LocalDate;
import java.time.Period;

public abstract class User extends Account{

    private String email;
    private char gender;
    private LocalDate birthDate;

    public User(String name, String password, String email, char gender, LocalDate birthDate) throws IllegalArgumentException{
        super(name, password);
        this.email = email;
        setGender(gender);
        setBirthDate(birthDate);
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
        if(gender == 'K' || gender == 'M' || gender == 'k' || gender == 'm') {
            this.gender = gender;
        } else {
            throw new IllegalArgumentException("Wrong gender");
        }
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) throws IllegalArgumentException{
        LocalDate now = LocalDate.now();
        Period period = Period.between(birthDate, now);
        if(period.getYears() < 8) {
            throw new IllegalArgumentException("You are too young");
        }
        this.birthDate = birthDate;
    }


}
