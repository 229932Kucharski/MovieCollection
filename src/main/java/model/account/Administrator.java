package model.account;

public class Administrator extends Supervisor{
    public Administrator(String name, String password) {
        super(name, password);
    }

    @Override
    public String welcomeText() {
        return null;
    }
}
