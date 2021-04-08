package model.account;

import model.account.password.PasswordHashing;

import java.time.LocalDate;

public abstract class Account {

    private final String name;
    private byte[] password;
    private final LocalDate registerDate;

    public Account(String name, String password) throws IllegalArgumentException{
        this.name = name;
        setPassword(password);
        registerDate = LocalDate.now();
    }

    public abstract String welcomeText();

    public String getName() {
        return name;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(String password) throws IllegalArgumentException{
        if(password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password in null or is too short");
        }
        this.password = PasswordHashing.hashPassword(password);
    }

    public LocalDate getRegisterDate() {
        return registerDate;
    }

}
