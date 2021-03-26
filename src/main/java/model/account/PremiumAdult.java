package model.account;

import java.time.LocalDate;
import java.util.Calendar;


public class PremiumAdult extends Adult {

    byte[] avatar;

    public PremiumAdult(String name, String password, String email, char gender, LocalDate birthDate, String phoneNumber) {
        super(name, password, email, gender, birthDate, phoneNumber);
    }

    @Override
    public String welcomeText() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.welcomeText());
        Calendar date = Calendar.getInstance();
        int dayNum = date.get(Calendar.DAY_OF_WEEK);
        if(dayNum == 7 || dayNum == 1) {
            sb.append("\nIt's a weekend!");
        } else {
            sb.append("\nWeekend in ").append(7 - dayNum).append(" day(s)");
        }
        return sb.toString();
    }
}
