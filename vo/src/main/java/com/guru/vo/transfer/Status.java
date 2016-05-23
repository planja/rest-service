package com.guru.vo.transfer;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Никита on 07.05.2016.
 */
public class Status {

    public static List<StatusCount> statusCountList = new ArrayList<>();
    public static int count = 0;

    public static void deleteFromStatusList(Long queryId) {
        StatusCount statusCount = statusCountList.stream().filter(o -> Objects.equals(o.getQueryId(), queryId)).findFirst().get();
        statusCountList.remove(statusCount);
    }

    public static StatusCount getStatusCountByQueryId(Long queryId) {
        return statusCountList.stream().filter(o -> Objects.equals(o.getQueryId(), queryId)).findFirst().get();
    }

    public static void updateStatus(Long queryId) {
        statusCountList.forEach(o -> {
            if (Objects.equals(o.getQueryId(), queryId))
                o.setCurrentStatus(o.getCurrentStatus() + 1);
        });

    }

    private static int CalculateForParser(String parser, String type, int daysCount) {
        int count = 0;
        switch (parser) {
            case "QF":
                //if (Objects.equals(type, "rt")) count = daysCount;
                // else count = daysCount;
                break;
            case "DL":
                break;
            case "KE":
                break;
        }
        return count;
    }


    public static int CalculateStatus(RequestData requestData) throws ParseException {
        int totalCount = 0;
        for (String parser : requestData.getParsers()) {
            switch (parser) {
                case "QF":
                    int count;
                    if (Objects.equals(requestData.getType(), "rt"))
                        count = requestData.getReturnDates().size()/* + requestData.getOwDates().size()*/;
                    else count = requestData.getOwDates().size();
                    totalCount += count;
                    break;
                case "DL":
                    count = 0;
                    if (Objects.equals(requestData.getType(), "rt"))
                        count = requestData.getReturnDates().size() + requestData.getOwDates().size();
                    else count = requestData.getOwDates().size();
                    totalCount += count;
                    break;
                case "KE":
                    count = 0;
                    if (Objects.equals(requestData.getType(), "rt"))
                        count = requestData.getReturnDates().size() + requestData.getOwDates().size();
                    else count = requestData.getOwDates().size();
                    totalCount += count;
                    break;
                case "AC":
                    int acCount;
                    if (Objects.equals(requestData.getType(), "rt"))
                        acCount = requestData.getReturnDates().size()/* + requestData.getOwDates().size()*/;
                    else acCount = requestData.getOwDates().size();
                    totalCount += acCount;
                    break;
            }
        }
        return totalCount;
    }

}
