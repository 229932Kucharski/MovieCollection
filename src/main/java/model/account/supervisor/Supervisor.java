package model.account.supervisor;

import model.account.Account;

public abstract class Supervisor extends Account {

    public Supervisor(String name, String password) {
        super(name, password);
    }

}
