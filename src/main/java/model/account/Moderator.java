package model.account;

public class Moderator extends Supervisor {
    public Moderator(String name, String password) {
        super(name, password);
    }

    @Override
    public String welcomeText() {
        return null;
    }
}
