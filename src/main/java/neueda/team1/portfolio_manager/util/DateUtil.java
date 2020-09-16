package neueda.team1.portfolio_manager.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateUtil {
    public static Date getStartDate() {
        //        2016-01-04T00:00:00.000Z
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));

        calendar.set(2016, Calendar.JANUARY, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getToday() {
        Calendar today = new GregorianCalendar();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getDateBeforeToday(int days) {
        Date today = getToday();

        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        todayCalendar.setTime(today);
        todayCalendar.add(Calendar.DAY_OF_MONTH, -days);
        return todayCalendar.getTime();
    }
}
