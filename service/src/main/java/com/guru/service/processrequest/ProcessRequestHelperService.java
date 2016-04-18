package com.guru.service.processrequest;

import java.text.ParseException;
import java.util.*;
import java.util.stream.IntStream;

public class ProcessRequestHelperService {

    public static List<Date> getDaysBetweenDates(Date startDate, Date endDate) {
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

    public static List<Date> getReturnDates(Date returnStartDate, Date returnEndDate, List<Date> dates) throws ParseException {
        List<Date> returnDates = new ArrayList<>();
        if (returnStartDate == null || returnStartDate.toString().trim().length() == 0) {
            dates.stream().forEach(o -> {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(o);
                calendar.add(Calendar.DAY_OF_MONTH, 2);

                returnDates.add(calendar.getTime());
            });
        } else {
            returnDates.addAll(getDaysBetweenDates(returnStartDate, returnEndDate));
        }
        return returnDates;
    }

    public static List<Date> getODates(List<Date> dates, List<Date> returnDates, List<Date> exceptDates) {
        List<Date> owDates = new ArrayList<>();
        if (dates.size() > returnDates.size()) {
            dates.stream().map(o -> exceptDates.contains(o) ? owDates.add(null) : owDates.add(o));
        }
        if (returnDates.size() > dates.size()) {
            int dSize = (returnDates.size() - dates.size()) / 2;
            dates.stream().map(o -> exceptDates.contains(o) ? owDates.add(null) : owDates.add(o));
            IntStream.range(dates.size() - dSize, dates.size()).forEach(o -> owDates.add(null));
        }

        if (returnDates.size() == dates.size()) {
            dates.stream().map(o -> exceptDates.contains(o) ? owDates.add(null) : owDates.add(o));

        }
        return owDates;
    }

    public static List<Date> getRDates(List<Date> dates, List<Date> returnDates, List<Date> exceptReturnDates) {
        List<Date> rDates = new ArrayList<>();
        if (dates.size() > returnDates.size()) {
            int dSize = (dates.size() - returnDates.size()) / 2;
            IntStream.range(0, dSize).forEach(o -> rDates.add(null));
            returnDates.stream().map(o -> exceptReturnDates.contains(o) ? rDates.add(null) : rDates.add(o));
            IntStream.range(dates.size() - dSize, dates.size()).forEach(o -> rDates.add(null));
        }
        if (returnDates.size() > dates.size()) {
            int dSize = (returnDates.size() - dates.size()) / 2;
            IntStream.range(0, dSize).forEach(o -> rDates.add(null));
            returnDates.stream().map(o -> exceptReturnDates.contains(o) ? rDates.add(null) : rDates.add(o));
        }

        if (returnDates.size() == dates.size()) {
            returnDates.stream().map(o -> exceptReturnDates.contains(o) ? rDates.add(null) : rDates.add(o));

        }
        return rDates;
    }
}
