package service.processrequest;

import viewmodel.ParamsViewModel;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Никита on 06.04.2016.
 */
public class ProcessRequestService {

    private ProcessRequestHelper processRequestHelper = new ProcessRequestHelper();

    protected void processRequest(ParamsViewModel viewModel) throws Exception {
        String startDate = viewModel.getStartDate();
        String endDate = viewModel.getEndDate();
        String returnStartDate = viewModel.getReturnStartDate();
        String returnEndDate = viewModel.getReturnEndDate();
        String origin1 = viewModel.getOrigin();
        String destination1 = viewModel.getDestination();
        String seats = viewModel.getSeats();
        String flightClass = viewModel.getFlightClass();
        String requestId = viewModel.getRequestId();
        String userId = viewModel.getUserId();
        String type = viewModel.getType();

        String except_days = viewModel.getExcept_days();
        String except_return_days = viewModel.getExcept_return_days();

        String login = viewModel.getLogin();

        String[] user = processRequestHelper.splitLogin(login);

        List<String> exceptDates = processRequestHelper.getExceptDays(except_days);
        List<String> exceptReturnDates = processRequestHelper.getExceptDays(except_return_days);

        final List<Date> dates = processRequestHelper.getDaysBetweenDates(processRequestHelper.sdf.parse(startDate), processRequestHelper.sdf.parse(endDate));
        final List<Date> returnDates = processRequestHelper.getReturnDates(returnStartDate, returnEndDate, dates);

        Map<List<Date>, List<Date>> map = processRequestHelper.getORDates(dates, returnDates, exceptReturnDates, exceptDates);
        final List<Date> oDates = map.values().stream().findFirst().get();
        final List<Date> rDates = map.keySet().stream().findFirst().get();

        String result = "";
    }
}
