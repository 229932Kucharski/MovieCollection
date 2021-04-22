package model.account.user;

import java.time.LocalDate;
import java.util.Calendar;

public class Adult extends User {

    private String phoneNumber;

    public Adult(int id, String name, String password, String email, char gender, LocalDate birthDate, String phoneNumber) throws IllegalArgumentException{
        super(id, name, password, email, gender, birthDate);
        this.phoneNumber = phoneNumber;
    }

    public Adult(int id, String name, byte[] password, String email, char gender, LocalDate birthDate, String phoneNumber) throws IllegalArgumentException{
        super(id, name, password, email, gender, birthDate);
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String welcomeText() {
        StringBuilder sb = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if(hour >= 5 && hour < 12) {
            sb.append("Good morning ").append(this.getName());
        } else if(hour >= 12 && hour < 17){
            sb.append("Good afternoon ").append(this.getName());
        } else {
            sb.append("Good evening ").append(this.getName());
        }
        return sb.toString();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
