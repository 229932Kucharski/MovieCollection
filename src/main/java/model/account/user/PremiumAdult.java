package model.account.user;

import java.time.LocalDate;
import java.util.Calendar;

/**
 * Premium adult can be created only by admin by promote method
 */
public class PremiumAdult extends Adult {

    public PremiumAdult(int id, String name, byte[] password, LocalDate regDate, String email, char gender, LocalDate birthDate, String phoneNumber) {
        super(id, name, password, regDate, email, gender, birthDate, phoneNumber);
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
