package model.account;

import model.account.password.PasswordHashing;

import java.time.LocalDate;
import java.util.Date;

public abstract class Account {

    private final String name;
    private byte[] password;
    private final LocalDate registerDate;

    public Account(String name, String password) {
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

    public void setPassword(String password) {
        if(password != null) {
            this.password = PasswordHashing.hashPassword(password);
        }
    }

    public LocalDate getRegisterDate() {
        return registerDate;
    }

}
