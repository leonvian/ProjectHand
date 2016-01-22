package com.lvc.syndichand.database;

import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.lvc.syndichand.model.Register;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by administrator on 10/12/15.
 */
public class RegisterDAO {


    public static List<Register> retrieveAllNotProcessed() {
        Select select = new Select();
        From from = select.from(Register.class);
        from.where("Status '"+ Register.STATUS_WAITING_PROCESS);
        List<Register> registers = from.execute();
        return registers;
    }

    public static boolean isAllRegistersOk(long idOwner) {
        String nextDate = getDateNextMonth();
        String currentDate = getDateThisMonthStr();

        Select select = new Select();
        From from = select.from(Register.class);
        //from.where("IdOwner = ? AND RegisterDate BETWEEN '"+ currentDate + "' AND '"+ nextDate+ "'", idOwner);
        from.where("RegisterDate BETWEEN '"+ currentDate + "' AND '"+ nextDate+ "'");
       // from.where("IdOwner = ?", idOwner);
        int count = from.count();

        if(count > 0) {
            return true;
        } else {
            return false;
        }
    }

    //2011/02/25
    private static String getDateThisMonthStr() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = getDateThisMonth();
        String dateNow = simpleDateFormat.format(date);

        return dateNow;
    }

    private static String getDateNextMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDateThisMonth());
        calendar.add(Calendar.MONTH, 1);
        Date date = calendar.getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

        //"YYYY/MM/dd"
        String dateNow = simpleDateFormat.format(date);

        return dateNow;
    }

    private static Date getDateThisMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        Date date = calendar.getTime();

        return date;
    }

}
