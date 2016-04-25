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

import static parser.Parser.*;

public class AdaptorServiceUA implements Adaptor {

    @Override
    public List<IMTAward> adaptData(List<?> parserData, String... args) {
        String flightClass = args[0];
        String seats = args[1];
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy:MM:dd");

        SimpleDateFormat ua_sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");

        SimpleDateFormat time_format_pac = new SimpleDateFormat("h:mm a");
        SimpleDateFormat time_format_res = new SimpleDateFormat("HH:mm");

        SimpleDateFormat time_format = new SimpleDateFormat("yyyy:MM:dd HH:mm");
        List<IMTAward> awardList = new LinkedList<IMTAward>();

        DatabaseManager dm = new DatabaseManager();

        for (Object obj : parserData) {
            Award item = (Award) obj;

            int info_id = 0;

            IMTAward award = new IMTAward();

//                    if (flightClass.equals(ALL)) {
//                        Info infoSE = item.getSaverEconomy();
//                        Info infoSTE = item.getStandartEconomy();
//
//                        if (infoSE != null || infoSTE != null) {
//
//                            info_id = getCabinClass(item.getSaverEconomy(), ECONOMY, IMTInfo.SAVER, info_id, award);
//                            info_id = getCabinClass(item.getStandartEconomy(), ECONOMY, IMTInfo.FULL, info_id, award);
//                        }
//
//                        Info infoSB = item.getSaverBusiness();
//                        Info infoSTB = item.getStandartBusiness();
//
//                        if (infoSB != null || infoSTB != null) {
//
//                            info_id = getCabinClass(item.getSaverBusiness(), BUSINESS, IMTInfo.SAVER, info_id, award);
//                            info_id = getCabinClass(item.getStandartBusiness(), BUSINESS, IMTInfo.FULL, info_id, award);
//                        }
//
//                        Info infoSF = item.getSaverFirst();
//                        Info infoSTF = item.getStandartFirst();
//
//                        if (infoSF != null || infoSTF != null) {
//
//                            info_id = getCabinClass(item.getSaverFirst(), FIRST, IMTInfo.SAVER, info_id, award);
//                            info_id = getCabinClass(item.getStandartFirst(), FIRST, IMTInfo.FULL, info_id, award);
//                        }
//                    }
            for (String cabinItem : Utils.getCabins(flightClass)) {
                try {

                    if (cabinItem.equals(ECONOMY)) {

                        Info infoS = item.getSaverEconomy();
                        Info info = item.getStandartEconomy();

                        if (infoS != null || info != null) {

                            info_id = getCabinClass(item.getSaverEconomy(), ECONOMY, IMTInfo.SAVER, info_id, award, item.getFlights());
                            info_id = getCabinClass(item.getStandartEconomy(), ECONOMY, IMTInfo.FULL, info_id, award, item.getFlights());
                        }

                    } else if (cabinItem.equals(BUSINESS)) {

                        Info infoS = item.getSaverBusiness();
                        Info info = item.getStandartBusiness();

                        if (infoS != null || info != null) {

                            info_id = getCabinClass(item.getSaverBusiness(), BUSINESS, IMTInfo.SAVER, info_id, award, item.getFlights());
                            info_id = getCabinClass(item.getStandartBusiness(), BUSINESS, IMTInfo.FULL, info_id, award, item.getFlights());
                        }

                    } else if (cabinItem.equals(FIRST)) {

                        Info infoS = item.getSaverFirst();
                        Info info = item.getStandartFirst();

                        if (infoS != null || info != null) {

                            info_id = getCabinClass(item.getSaverFirst(), FIRST, IMTInfo.SAVER, info_id, award, item.getFlights());
                            info_id = getCabinClass(item.getStandartFirst(), FIRST, IMTInfo.FULL, info_id, award, item.getFlights());
                        }

                    }
                } catch (SQLException e) {
                }
            }

            int flight_id = 0;

            Date previousDate = null;

            for (Flight flight : item.getFlights()) {

                IMTFlight imtf = new IMTFlight();

                imtf.setAircraft(flight.getAircraft());
                try {
                    Date date1 = ua_sdf.parse(flight.getArriveDate().trim());

                    //01/13/2016 22:55
                    imtf.setArriveDate(sdf1.format(date1));
                    imtf.setArrivePlace(flight.getArrivePlace());
                    imtf.setArriveCode(flight.getArriveAirport());
                    imtf.setArriveTime(time_format_res.format(date1));

                    Date date2 = ua_sdf.parse(flight.getDepartDate().trim());

                    imtf.setDepartDate(sdf1.format(date2));
                    imtf.setDepartPlace(flight.getDepartPlace());
                    imtf.setDepartCode(flight.getDepartAirport());
                    imtf.setDepartTime(time_format_res.format(date2));
                    imtf.setFlightCabin(flight.getFlightCabin());
                    imtf.setFlightNumber(flight.getFlight());
                    imtf.setAirlineCompany(Utils.getAirCompany(flight.getFlight()));
                    imtf.setMeal("");

//                        String travelTime = flight.getTravelTime();
//                        imtf.setTravel_time(Utils.getHoursBetweenDays(date2, date1));
                    imtf.setId(flight_id);

                    Date arrDate = time_format.parse(imtf.getArriveDate() + " " + imtf.getArriveTime());
                    Date depDate = time_format.parse(imtf.getDepartDate() + " " + imtf.getDepartTime());

                    Calendar depCalendar = new GregorianCalendar();
                    depCalendar.setTime(depDate);
                    depCalendar.add(Calendar.SECOND, -dm.getTZByCode(imtf.getDepartCode()));

                    Calendar arrCalendar = new GregorianCalendar();
                    arrCalendar.setTime(arrDate);
                    arrCalendar.add(Calendar.SECOND, -dm.getTZByCode(imtf.getArriveCode()));

                    imtf.setTravelTime(Utils.getHoursBetweenDays(depCalendar.getTime(), arrCalendar.getTime()));

                    //  imtf.setLayoverTime(Utils.getHoursBetweenDays(previousDate, depDate));
                    previousDate = arrDate;
                } catch (ParseException e) {
                } catch (SQLException e) {
                } catch (Exception e) {
                }

                award.getFlightList().add(imtf);

                flight_id++;
            }

            award.setTotalDuration(item.getTotalDuration());

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

            if (award.getClassList().size() > 0) {

                awardList.add(award);
            }
        }

        System.out.println("UA handler. Flight size after processing= [" + awardList.size() + "]");

        return awardList;
    }

    private Integer getCabinClass(Info info, String fCabin, String classDescription, int id, IMTAward award, List<Flight> flightList) throws SQLException {

        IMTInfo imti = new IMTInfo();

        if (info != null && info.getStatus() != Info.NA) {

            DatabaseManager dm = new DatabaseManager();

            boolean usa = false;

            for (Flight flight : flightList) {

                boolean depart = dm.isUSA(flight.getDepartAirport());
                boolean arrive = dm.isUSA(flight.getArriveAirport());

                if (depart && arrive) {

                    flight.setUsa(true);
                    usa = true;

                } else {

                    flight.setUsa(false);
                }
            }

            imti.setMileage(((Double) Double.parseDouble(info.getMileage())).intValue() + "");
            imti.setStatus(info.getStringStatus());
            imti.setTax(info.getTax());
            imti.setId(id);

            ExtraData extraData = new ExtraData();

            extraData.setFieldName("class_description");
            extraData.setFieldType("string");
            extraData.setFieldValue(classDescription);
            extraData.setFieldLvl("class_list");
            extraData.setFieldId("class_list_" + id);

            award.getExtraData().add(extraData);

            int flight_id = 0;

            boolean mixed = false;

            String currCabin = fCabin;

            for (String mixedCabins : info.getMixedCabins()) {

                mixed = true;

                mixedCabins = Utils.getMixedCabinClass(mixedCabins.replaceAll("United", "").trim());

                extraData = new ExtraData();

                currCabin = Utils.getCabin(mixedCabins, currCabin);

                if (currCabin.equals(FIRST)) {

                    if (flightList.get(flight_id).isUsa()) {

                        mixedCabins = BUSINESS;

                        if (usa) {

                            currCabin = BUSINESS;
                        }

                    } else {

                        usa = false;
                        currCabin = FIRST;
                    }
                }

                extraData.setFieldName("mixed_cabins");
                extraData.setFieldType("string");
                extraData.setFieldValue(mixedCabins);
                extraData.setFieldLvl("class_list");
                extraData.setFieldId("class_list_" + id);
                extraData.setFieldSubLvl("flight_list_" + flight_id);

                award.getExtraData().add(extraData);

                flight_id++;
            }

            imti.setName(fCabin);

            if (mixed) {

                extraData = new ExtraData();

                extraData.setFieldName("mixed_description");
                extraData.setFieldType("string");
                extraData.setFieldValue(currCabin);
                extraData.setFieldLvl("class_list");
                extraData.setFieldId("class_list_" + id);

                award.getExtraData().add(extraData);

                if (usa) {

                    imti.setName(currCabin);
                }

            } else {

                if (usa && fCabin.equals(FIRST)) {

                    imti.setName(BUSINESS);
                }
            }

            award.getClassList().add(imti);

            id++;
        }

        return id;
    }
}

