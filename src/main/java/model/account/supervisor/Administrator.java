package model.account.supervisor;

public class Administrator extends Supervisor{
    public Administrator(String name, String password) {
        super(name, password);
    }

    @Override
    public String welcomeText() {
        return null;
    }
}
