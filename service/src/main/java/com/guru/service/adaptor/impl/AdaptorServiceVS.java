package service.adaptor.impl;

import parser.model.Award;
import parser.model.Flight;
import parser.model.Info;
import service.adaptor.db.DatabaseManager;
import service.adaptor.interf.Adaptor;
import service.adaptor.utils.Utils;
import vo.view.ExtraData;
import vo.view.IMTAward;
import vo.view.IMTFlight;
import vo.view.IMTInfo;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static parser.Parser.*;

/**
 * Created by Anton on 13.04.2016.
 */
public class AdaptorServiceVS implements Adaptor {
    @Override
    public List<IMTAward> adaptData(List<?> parserData, String... args) {
        String flightClass = args[0];
        String seats = args[1];
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy:MM:dd");
        SimpleDateFormat vs_sdf = new SimpleDateFormat("EEEE d MMMM yyyy", Locale.US);
        SimpleDateFormat time_format_res = new SimpleDateFormat("yyyy:MM:dd HH:mm");
        List<IMTAward> awardList = new LinkedList<IMTAward>();
        String result = "";

        DatabaseManager dm = new DatabaseManager();

        for (Object obj : parserData) {
            Award item = (Award) obj;

            int info_id = 0;

            IMTAward award = new IMTAward();

//                    if (flightClass.equals(ALL)) {
//                        Info infoE = item.getEconomy();
//                        Info infoB = item.getBusiness();
//                        Info infoPE = item.getPremiumEconomy();
//
//                        if (infoE != null) {
//
//                            info_id = getCabinClass(infoE, ECONOMY, IMTInfo.SAVER, info_id, award);
//                        }
//
//                        if (infoB != null) {
//
//                            info_id = getCabinClass(infoB, BUSINESS, IMTInfo.SAVER, info_id, award);
//                        }
//
//                        if (infoPE != null) {
//
//                            info_id = getCabinClass(infoPE, PREMIUM_ECONOMY, IMTInfo.SAVER, info_id, award);
//                        }
//                    }

            for (String cabinItem : Utils.getCabins(flightClass)) {

                if (cabinItem.equals(ECONOMY)) {

                    Info info = item.getEconomy();

                    if (info != null) {

                        info_id = getCabinClass(info, ECONOMY, IMTInfo.SAVER, info_id, award);
                    }

                } else if (cabinItem.equals(BUSINESS)) {

                    Info info = item.getBusiness();

                    if (info != null) {

                        info_id = getCabinClass(info, BUSINESS, IMTInfo.SAVER, info_id, award);
                    }

                } else if (cabinItem.equals(PREMIUM_ECONOMY)) {

                    Info info = item.getPremiumEconomy();

                    if (info != null) {

                        info_id = getCabinClass(info, PREMIUM_ECONOMY, IMTInfo.SAVER, info_id, award);
                    }
                }
            }

            int flight_id = 0;

            Date previousDate = null;

            for (Flight flight : item.getFlights()) {

                IMTFlight imtf = new IMTFlight();

                imtf.setAircraft(flight.getAircraft());

                String arrTime = flight.getArriveDate();

                try {

                    Date arrDate = vs_sdf.parse(flight.getDepartDate().trim());

                    if (arrTime.contains("+")) {

                        String dayDiff = arrTime.substring(arrTime.indexOf("+") + 1, arrTime.length());

                        dayDiff = dayDiff.substring(0, dayDiff.replaceAll(" ", " ").indexOf(" "));

                        Calendar calendar = new GregorianCalendar();
                        calendar.setTime(arrDate);
                        calendar.add(Calendar.DAY_OF_YEAR, Integer.parseInt(dayDiff));

                        flight.setArriveDate(sdf1.format(calendar.getTime()));

                    } else {

                        flight.setArriveDate(sdf1.format(arrDate));
                    }

                    imtf.setArriveDate(flight.getArriveDate());
                    imtf.setArrivePlace(dm.getCityByCode(flight.getArriveAirport().trim()));
                    imtf.setArriveCode(flight.getArriveAirport());
                    imtf.setArriveTime(flight.getArriveTime());

                    Date date1 = vs_sdf.parse(flight.getDepartDate().trim());

                    imtf.setDepartDate(sdf1.format(date1));
                    imtf.setDepartPlace(dm.getCityByCode(flight.getDepartAirport().trim()));
                    imtf.setDepartCode(flight.getDepartAirport());
                    imtf.setDepartTime(flight.getDepartTime());
                    imtf.setFlightNumber(flight.getFlight());
                    imtf.setAirlineCompany(Utils.getAirCompany(flight.getFlight()));
                    imtf.setMeal(flight.getMeal());
                    imtf.setId(flight_id);

                    Date depDateT = time_format_res.parse(imtf.getDepartDate() + " " + imtf.getDepartTime());
                    Date arrDateT = time_format_res.parse(imtf.getArriveDate() + " " + imtf.getArriveTime());

                    Calendar depCalendar = new GregorianCalendar();
                    depCalendar.setTime(depDateT);
                    depCalendar.add(Calendar.SECOND, -dm.getTZByCode(imtf.getDepartCode()));

                    Calendar arrCalendar = new GregorianCalendar();
                    arrCalendar.setTime(arrDateT);
                    arrCalendar.add(Calendar.SECOND, -dm.getTZByCode(imtf.getArriveCode()));

                    imtf.setLayoverTime(Utils.getHoursBetweenDays(previousDate, depCalendar.getTime()));
                    imtf.setTravelTime(Utils.getHoursBetweenDays(depCalendar.getTime(), arrCalendar.getTime()));

                    previousDate = arrCalendar.getTime();

                    award.getFlightList().add(imtf);

                    flight_id++;
                } catch (ParseException e) {
                } catch (SQLException e) {
                } catch (Exception e) {
                }
            }

            award.setTotalDuration(item.getTotalDuration());

            if (award.getClassList().size() > 0) {

                awardList.add(award);
            }
        }

        System.out.println("VS handler. Flight size after processing= [" + awardList.size() + "]");

        return awardList;
    }

    private Integer getCabinClass(Info info, String fCabin, String classDescription, int id, IMTAward award) {

        IMTInfo imti = new IMTInfo();

        if (info != null && info.getStatus() != Info.NA) {

            imti.setMileage(info.getMileage().trim());
            imti.setName(fCabin);
            imti.setStatus(info.getStringStatus());

            String[] taxArray = info.getTax().trim().split(" ");

            imti.setCurrency(taxArray[0].trim());
            imti.setTax(taxArray[1].replaceAll(",", "").trim());
            imti.setId(id);

            ExtraData extraData = new ExtraData();

            extraData.setFieldName("class_description");
            extraData.setFieldType("string");
            extraData.setFieldValue(classDescription);
            extraData.setFieldLvl("class_list");
            extraData.setFieldId("class_list_" + id);

            award.getExtraData().add(extraData);

            id++;

            award.getClassList().add(imti);
        }

        return id;
    }
}