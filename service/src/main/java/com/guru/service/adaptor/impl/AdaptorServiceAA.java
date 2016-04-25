package com.guru.service.adaptor.impl;

import com.guru.vo.view.Award;
import com.guru.vo.view.Flight;
import com.guru.vo.view.Info;
import com.guru.service.adaptor.db.DatabaseManager;
import com.guru.service.adaptor.interf.Adaptor;
import com.guru.service.adaptor.utils.Utils;
import com.guru.vo.view.ExtraData;
import com.guru.vo.view.IMTAward;
import com.guru.vo.view.IMTFlight;
import com.guru.vo.view.IMTInfo;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import static parser.Parser.*;


public class AdaptorServiceAA implements Adaptor {

    @Override
    public List<IMTAward> adaptData(List<?> parserData, String... args) {
        String flightClass = args[0];
        String seats = args[1];

        String result = "";

        SimpleDateFormat dt_format_or = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat date_format_res = new SimpleDateFormat("yyyy:MM:dd");
        SimpleDateFormat time_format_res = new SimpleDateFormat("HH:mm");

        date_format_res.setTimeZone(TimeZone.getTimeZone("UTC"));
        time_format_res.setTimeZone(TimeZone.getTimeZone("UTC"));

        DatabaseManager dm = new DatabaseManager();

        List<IMTAward> awardList = new LinkedList<IMTAward>();

        for (Object obj : parserData) {
            Award item = (Award) obj;
            int info_id = 0;

            IMTAward award = new IMTAward();

            int flight_id = 0;

            Date firstDate = null;
            Date lastDate = null;

            Date previousDate = null;

            List<String> cabinList = new LinkedList<>();

            String previousCabin = null;

            boolean mixed = false;

            for (Flight flight : item.getFlights()) {

                IMTFlight imtf = new IMTFlight();

                imtf.setAircraft(flight.getAircraft());

                Date arrDate = flight.getArrDT();

                imtf.setArriveDate(date_format_res.format(arrDate));
                imtf.setArrivePlace(flight.getArriveAirport().trim());
                imtf.setArriveCode(flight.getArrivePlace());
                imtf.setArriveTime(time_format_res.format(arrDate));

                Date depDate = flight.getDepDT();

                if (firstDate == null) {

                    firstDate = depDate;
                }
                try {
                    imtf.setLayoverTime(Utils.getHoursBetweenDays(previousDate, depDate));


                    previousDate = arrDate;

                    imtf.setDepartDate(date_format_res.format(depDate));
                    imtf.setDepartPlace(flight.getDepartAirport().trim());
                    imtf.setDepartCode(flight.getDepartPlace());
                    imtf.setDepartTime(time_format_res.format(depDate));
                    imtf.setFlightNumber(flight.getfNumber());
                    imtf.setAirlineCompany(Utils.getAirCompany(flight.getfNumber()));
                    imtf.setMeal(flight.getMeal());
                    imtf.setTravelTime(Utils.getHoursBetweenDays(depDate, arrDate));
                    imtf.setId(flight_id);
                } catch (Exception e) {
                }
//                    if (previousCabin != null && !previousCabin.equals(flight.getFlightCabin())) {
//
//                        mixed = true;
//                    }

//                    previousCabin = flight.getFlightCabin();
//                    cabinList.add(flight.getFlightCabin());

                award.getFlightList().add(imtf);

                flight_id++;
            }

            Flight flight = item.getFlights().size() > 0 ? item.getFlights().get(item.getFlights().size() - 1) : null;

            lastDate = flight == null ? new Date() : flight.getArrDT();

            for (String cabinItem : Utils.getCabins(flightClass)) {
                try {

                    if (cabinItem.equals(ECONOMY)) {

                        Info infoS = item.getSaverEconomy();
                        Info infoF = item.getFullEconomy();

                        if (infoS != null) {

                            info_id = getCabinClass(infoS, ECONOMY, IMTInfo.SAVER, info_id, award, cabinList, Integer.parseInt(seats), item.getFlights());
                        }

                        if (infoF != null) {

                            info_id = getCabinClass(infoF, ECONOMY, IMTInfo.FULL, info_id, award, cabinList, Integer.parseInt(seats), item.getFlights());
                        }

                    } else if (cabinItem.equals(BUSINESS)) {

                        Info infoS = item.getSaverBusiness();
                        Info infoF = item.getFullBusiness();

                        if (infoS != null) {

                            info_id = getCabinClass(infoS, BUSINESS, IMTInfo.SAVER, info_id, award, cabinList, Integer.parseInt(seats), item.getFlights());
                        }

                        if (infoF != null) {

                            info_id = getCabinClass(infoF, BUSINESS, IMTInfo.FULL, info_id, award, cabinList, Integer.parseInt(seats), item.getFlights());
                        }

                    } else if (cabinItem.equals(FIRST)) {

                        Info infoS = item.getSaverFirst();
                        Info infoF = item.getFullFirst();

                        if (infoS != null) {

                            info_id = getCabinClass(infoS, FIRST, IMTInfo.SAVER, info_id, award, cabinList, Integer.parseInt(seats), item.getFlights());
                        }

                        if (infoF != null) {

                            info_id = getCabinClass(infoF, FIRST, IMTInfo.FULL, info_id, award, cabinList, Integer.parseInt(seats), item.getFlights());
                        }
                    }
                } catch (SQLException e) {

                }
            }
            try {
                award.setTotalDuration(Utils.getHoursBetweenDays(firstDate, lastDate));
            } catch (Exception e) {
            }

            int i = 0;

            for (String conItem : item.getConnections()) {

                ExtraData extraData = new ExtraData();

                extraData.setFieldName("connection");
                extraData.setFieldType("string");
                extraData.setFieldValue(conItem);
                extraData.setFieldLvl("flight_list");
                extraData.setFieldId("flight_list_" + i);

                award.getExtraData().add(extraData);

                i++;
            }

            int j = 0;

            for (IMTInfo imti : award.getClassList()) {

                if (imti.getName().equals(BUSINESS) && !Utils.getCabins(flightClass).contains(BUSINESS)) {

                    award.getClassList().remove(j);
                    break;
                }

                j++;
            }

            if (award.getClassList().size() > 0) {

                awardList.add(award);
            }
        }

        System.out.println("AA handler. Flight size after processing= [" + awardList.size() + "]");


        return awardList;
    }

    private Integer getCabinClass(Info info, String fCabin, String classDescription, int id, IMTAward award, List<String> cabinList, int seats, List<Flight> flightList) throws SQLException {

        IMTInfo imti = new IMTInfo();

        if (info != null && info.getStatus() != Info.NA) {

//                DatabaseManager dm = new DatabaseManager();
//
//                boolean usa = false;
//
//                for (Flight flight : flightList) {
//
//                    boolean depart = dm.isUSA(flight.getDepartAirport());
//                    boolean arrive = dm.isUSA(flight.getArriveAirport());
//
//                    if (depart && arrive) {
//
//                        flight.setUsa(true);
//                        usa = true;
//
//                    } else {
//
//                        flight.setUsa(false);
//                    }
//                }

            String mil = info.getMileage();

            imti.setMileage(mil);
//                imti.setMileage(Integer.parseInt(mil) / seats + "");
//                imti.setName(fCabin);
            imti.setStatus(info.getStringStatus());
            imti.setTax(info.getTax().replaceAll("$", ""));
            imti.setCurrency("USD");
            imti.setId(id);

            ExtraData extraData = new ExtraData();

            extraData.setFieldName("class_description");
            extraData.setFieldType("string");
            extraData.setFieldValue(classDescription);
            extraData.setFieldLvl("class_list");
            extraData.setFieldId("class_list_" + id);

            award.getExtraData().add(extraData);

            int flight_id = 0;

            String currCabin = fCabin;

//                if (mixed) {
//
//                    extraData = new ExtraData();
//
//                    extraData.setField_name("mixed_description");
//                    extraData.setField_type("string");
//                    extraData.setField_value(currCabin);
//                    extraData.setField_lvl("class_list");
//                    extraData.setField_id("class_list_" + id);
//
//                    award.getExtra_data().add(extraData);
//                }
//                }

            imti.setName(fCabin);

//                if (mixed) {
//
//                    if (usa) {
//
//                        imti.setName(currCabin);
//                    }
//
//                } else {
//
//                    if (usa && fCabin.equals(FIRST)) {
//
//                        imti.setName(BUSINESS);
//                    }
//                }

            award.getClassList().add(imti);

            id++;
        }

        return id;
    }
}


