package model.account;

import java.util.Date;

public abstract class Account {

    private final String name;
    private byte[] password;
    private final Date registerDate;

    public Account(String name, byte[] password) {
        this.name = name;
        this.password = password;
        registerDate = new Date();
    }


    public String getName() {
        return name;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public Date getDate() {
        return registerDate;
    }

}
