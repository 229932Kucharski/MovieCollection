package model.account;

import model.account.password.PasswordHashing;
import model.exception.PasswordException;

import java.time.LocalDate;
import java.util.Arrays;

public abstract class Account {

    private final String name;
    private byte[] password;
    private final LocalDate registerDate;

    public Account(String name, String password, LocalDate regDate) throws PasswordException{
        this.name = name;
        setPassword(password);
        registerDate = regDate;
    }

    public Account(String name, byte[] password, LocalDate regDate) throws PasswordException{
        this.name = name;
        setPassword(password);
        registerDate = regDate;
    }

    public abstract String welcomeText();

    public String getName() {
        return name;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(String password) throws PasswordException{
        if(password == null || password.length() < 6) {
            throw new PasswordException("Password in null or is too short");
        }
        this.password = PasswordHashing.hashPassword(password);
    }

    public void setPassword(byte[] password){
        int i = password.length - 1;
        while (i >= 0 && password[i] == 0) {
            --i;
        }
        this.password = Arrays.copyOf(password, i + 1);
    }

    public LocalDate getRegisterDate() {
        return registerDate;
    }

}
