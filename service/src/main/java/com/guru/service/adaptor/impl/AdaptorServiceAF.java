package com.guru.service.adaptor.impl;


import com.guru.service.adaptor.interf.Adaptor;
import com.guru.vo.utils.Utils;
import com.guru.vo.view.ExtraData;
import com.guru.vo.view.IMTAward;
import com.guru.vo.view.IMTFlight;
import com.guru.vo.view.IMTInfo;
import factory.db.manager.DatabaseManager;
import parser.model.Award;
import parser.model.Flight;
import parser.model.Info;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static parser.Parser.*;

/**
 * Created by Anton on 13.04.2016.
 */
public class AdaptorServiceAF implements Adaptor {
    @Override
    public List<IMTAward> adaptData(List<?> parserData, String... args) {
        String flightClass = args[0];
        String seats = args[1];
        SimpleDateFormat time_format_pac = new SimpleDateFormat("yyyy:MM:dd h:mm a");
        SimpleDateFormat time_format_res = new SimpleDateFormat("HH:mm");

        String result;

        System.out.println("AF handler. Flight size before processing= [" + parserData.size() + "]");

        DatabaseManager dm = new DatabaseManager();

        List<IMTAward> awardList = new LinkedList<IMTAward>();

        for (Object obj : parserData) {
            Award item = (Award) obj;
            int info_id = 0;

            IMTAward award = new IMTAward();

//                if (flightClass.equals(Parser.ALL)) {
//                    Info infoSE = item.getSaverEconomy();
//                    Info infoFE = item.getFullEconomy();
//
//                    if (infoSE != null || infoFE != null) {
//
//                        info_id = getCabinClass(infoSE, ECONOMY, IMTInfo.SAVER, info_id, award);
//                        info_id = getCabinClass(infoFE, ECONOMY, IMTInfo.FULL, info_id, award);
//                    }
//
//                    Info infoSB = item.getSaverBusiness();
//                    Info infoFB = item.getFullBusiness();
//
//                    if (infoSB != null || infoFB != null) {
//
//                        info_id = getCabinClass(infoSB, BUSINESS, IMTInfo.SAVER, info_id, award);
//                        info_id = getCabinClass(infoFB, BUSINESS, IMTInfo.FULL, info_id, award);
//                    }
//
//                    Info infoSF = item.getSaverFirst();
//                    Info infoFF = item.getFullFirst();
//
//                    if (infoSF != null || infoFF != null) {
//
//                        info_id = getCabinClass(infoSF, FIRST, IMTInfo.SAVER, info_id, award);
//                        info_id = getCabinClass(infoFF, FIRST, IMTInfo.FULL, info_id, award);
//                    }
//
//                    Info infoSP = item.getSaverPremium();
//                    Info infoFP = item.getFullPremium();
//
//                    if (infoSP != null || infoFP != null) {
//
//                        info_id = getCabinClass(infoSP, PREMIUM_ECONOMY, IMTInfo.SAVER, info_id, award);
//                        info_id = getCabinClass(infoFP, PREMIUM_ECONOMY, IMTInfo.FULL, info_id, award);
//                    }
//                }
            for (String cabinItem : Utils.getCabins(flightClass)) {

                if (cabinItem.equals(ECONOMY)) {

                    Info infoSE = item.getSaverEconomy();
                    Info infoFE = item.getFullEconomy();

                    if (infoSE != null || infoFE != null) {

                        info_id = getCabinClass(infoSE, ECONOMY, IMTInfo.SAVER, info_id, award);
                        info_id = getCabinClass(infoFE, ECONOMY, IMTInfo.FULL, info_id, award);
                    }

                } else if (cabinItem.equals(BUSINESS)) {

                    Info infoSB = item.getSaverBusiness();
                    Info infoFB = item.getFullBusiness();

                    if (infoSB != null || infoFB != null) {

                        info_id = getCabinClass(infoSB, BUSINESS, IMTInfo.SAVER, info_id, award);
                        info_id = getCabinClass(infoFB, BUSINESS, IMTInfo.FULL, info_id, award);
                    }

                } else if (cabinItem.equals(FIRST)) {

                    Info infoSF = item.getSaverFirst();
                    Info infoFF = item.getFullFirst();

                    if (infoSF != null || infoFF != null) {

                        info_id = getCabinClass(infoSF, FIRST, IMTInfo.SAVER, info_id, award);
                        info_id = getCabinClass(infoFF, FIRST, IMTInfo.FULL, info_id, award);
                    }

                } else if (cabinItem.equals(PREMIUM_ECONOMY)) {

                    Info infoSP = item.getSaverPremium();
                    Info infoFP = item.getFullPremium();

                    if (infoSP != null || infoFP != null) {

                        info_id = getCabinClass(infoSP, PREMIUM_ECONOMY, IMTInfo.SAVER, info_id, award);
                        info_id = getCabinClass(infoFP, PREMIUM_ECONOMY, IMTInfo.FULL, info_id, award);
                    }
                }
            }

            int flight_id = 0;

            Date previousDate = null;

            Date depDate = null;
            Date arrDate = null;

            for (Flight flight : item.getFlights()) {

                IMTFlight imtf = new IMTFlight();
                try {
                    imtf.setAircraft(flight.getAircraft());
                    imtf.setArriveDate(flight.getArriveDate());
                    imtf.setArrivePlace(dm.getCityByCode(flight.getArriveAirport().trim()));
                    imtf.setArriveCode(flight.getArriveAirport());

                    Date arrTime = time_format_pac.parse(flight.getArriveDate() + " " + flight.getArriveTime());

                    imtf.setArriveTime(time_format_res.format(arrTime));
                    imtf.setDepartDate(flight.getDepartDate());
                    imtf.setDepartPlace(dm.getCityByCode(flight.getDepartAirport().trim()));
                    imtf.setDepartCode(flight.getDepartAirport());

                    Date depTime = time_format_pac.parse(flight.getDepartDate() + " " + flight.getDepartTime());

                    if (flight_id == 0) {

                        depDate = depTime;
                    }

                    if (flight_id == (item.getFlights().size() - 1)) {

                        arrDate = arrTime;
                    }

                    //  imtf.setLayoverTime(Utils.getHoursBetweenDays(previousDate, depTime));
                    previousDate = arrTime;

                    imtf.setDepartTime(time_format_res.format(depTime));
                    imtf.setFlightNumber(flight.getFlight());
                    imtf.setAirlineCompany(Utils.getAirCompany(flight.getFlight()));

                    Calendar depCalendar = new GregorianCalendar();
                    depCalendar.setTime(depTime);
                    depCalendar.add(Calendar.SECOND, -dm.getTZByCode(imtf.getDepartCode()));

                    Calendar arrCalendar = new GregorianCalendar();
                    arrCalendar.setTime(arrTime);
                    arrCalendar.add(Calendar.SECOND, -dm.getTZByCode(imtf.getArriveCode()));

                    imtf.setTravelTime(Utils.getHoursBetweenDays(depCalendar.getTime(), arrCalendar.getTime()));

                    imtf.setMeal(flight.getMeal());
//                    imtf.setTravel_time(flight.getTravelTime());
                    imtf.setId(flight_id);

                    award.getFlightList().add(imtf);

                    flight_id++;
                } catch (SQLException e) {
                } catch (ParseException e) {
                } catch (Exception e) {
                }
            }

            award.setTotalDuration(item.getTotalDuration());

            if (award.getClassList().size() > 0) {

                awardList.add(award);
            }
        }
        System.out.println("AF handler. Flight size after processing= [" + awardList.size() + "]");

        return awardList;
    }

    private Integer getCabinClass(Info info, String fCabin, String classDescription, int id, IMTAward award) {

        IMTInfo imti = new IMTInfo();

        if (info != null && info.getStatus() != Info.NA) {

            String tax = info.getTax().replaceAll("\\+", "").replaceAll("\\*", "");

            Pattern pattern = Pattern.compile("(\\d+\\.\\d*).{1}(\\D+)");

            Matcher matcher = pattern.matcher(tax);

            if (matcher.find()) {

                imti.setTax(matcher.group(1).trim());
                imti.setCurrency(matcher.group(2).trim().replaceAll(" ", ""));
            }

            pattern = Pattern.compile("(\\d+)(.+)");

            matcher = pattern.matcher(info.getMileage().replaceAll(" ", ""));

            if (matcher.find()) {

                imti.setMileage(matcher.group(1).trim());
            }

            imti.setName(fCabin);
            imti.setStatus(info.getStringStatus());

            imti.setId(id);

            award.getClassList().add(imti);

            ExtraData extraData = new ExtraData();

            extraData.setFieldName("class_description");
            extraData.setFieldType("string");
            extraData.setFieldValue(classDescription);
            extraData.setFieldLvl("class_list");
            extraData.setFieldId("class_list_" + id);

            award.getExtraData().add(extraData);

            id++;
        }

        return id;
    }
}