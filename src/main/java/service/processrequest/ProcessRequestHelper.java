package service.processrequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by Никита on 07.04.2016.
 */
public class ProcessRequestHelper {


    public SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");//02/18/2016


    public List<String> getExceptDays(String except_days) {
        List<String> exceptDates = new ArrayList<>();
        if (except_days != null) {
            SerializedPhpParser serializedPhpParser = new SerializedPhpParser(except_days);
            Object res = serializedPhpParser.parse();
            Map<Object, String> qqq = (Map<Object, String>) res;

            for (Object key : qqq.keySet()) {
                String value = qqq.get(key);
                exceptDates.add(value);
                System.out.println("Except date: " + value);
            }
        }
        return exceptDates;
    }

    public List<Date> getDaysBetweenDates(Date startDate, Date endDate) {
        List<Date> dates = new ArrayList<Date>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);
        while (calendar.getTime().before(endDate)) {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        dates.add(endDate);
        return dates;
    }

    public List<Date> getReturnDates(String returnStartDate, String returnEndDate, List<Date> dates) throws ParseException {
        List<Date> returnDates = new ArrayList<>();
        if (returnStartDate == null || returnStartDate.trim().length() == 0) {
            dates.stream().forEach(o -> {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(o);
                calendar.add(Calendar.DAY_OF_MONTH, 2);

                returnDates.add(calendar.getTime());
            });
        } else {
            returnDates.addAll(this.getDaysBetweenDates(sdf.parse(returnStartDate), sdf.parse(returnEndDate)));
        }
        return returnDates;
    }

    public Map<List<Date>, List<Date>> getORDates(List<Date> dates, List<Date> returnDates, List<String> exceptReturnDates, List<String> exceptDates) {
        List<Date> rDates = new ArrayList<>();
        List<Date> oDates = new ArrayList<>();
        if (dates.size() > returnDates.size()) {
            int dSize = (dates.size() - returnDates.size()) / 2;
            IntStream.range(0, dSize).forEach(o -> rDates.add(null));
            returnDates.stream().map(o -> exceptReturnDates.contains(this.sdf.format(o)) ? rDates.add(null) : rDates.add(o));
            IntStream.range(dates.size() - dSize, dates.size()).forEach(o -> rDates.add(null));
            dates.stream().map(o -> exceptDates.contains(this.sdf.format(o)) ? oDates.add(null) : oDates.add(o));
        }
        if (returnDates.size() > dates.size()) {
            int dSize = (returnDates.size() - dates.size()) / 2;
            IntStream.range(0, dSize).forEach(o -> rDates.add(null));
            dates.stream().map(o -> exceptDates.contains(this.sdf.format(o)) ? oDates.add(null) : oDates.add(o));
            IntStream.range(dates.size() - dSize, dates.size()).forEach(o -> oDates.add(null));
            returnDates.stream().map(o -> exceptReturnDates.contains(this.sdf.format(o)) ? rDates.add(null) : rDates.add(o));
        }

        if (returnDates.size() == dates.size()) {
            returnDates.stream().map(o -> exceptReturnDates.contains(sdf.format(o)) ? rDates.add(null) : rDates.add(o));
            dates.stream().map(o -> exceptDates.contains(sdf.format(o)) ? oDates.add(null) : oDates.add(o));

        }
        return new HashMap<List<Date>, List<Date>>() {{
            put(rDates, oDates);
        }};
    }

    public String[] splitLogin(String login) {
        String[] user = new String[3];
        if (login.contains("-")) {
            user = login.split("-");
        } else {

            user[0] = login.substring(0, 3);
            user[1] = login.substring(3, 6);
            user[2] = login.substring(6, 9);
        }
        return user;
    }


}
