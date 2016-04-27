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
import parser.qf.QFParser;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static parser.Parser.*;

/**
 * Created by Anton on 13.04.2016.
 */
public class AdaptorServiceQF implements Adaptor {
    @Override
    public List<IMTAward> adaptData(List<?> parserData, String... args) {
        String flightClass = args[0];
        String seats = args[1];
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat dt_format_or_time = new SimpleDateFormat("EEE dd MMM yy HH:mm", Locale.US);
        SimpleDateFormat date_format_res = new SimpleDateFormat("yyyy:MM:dd");

        String result;
        System.out.println("QF handler. Flight size before processing= [" + parserData.size() + "]");
        DatabaseManager dm = new DatabaseManager();
        List<IMTAward> awardList = new LinkedList<IMTAward>();

        for (Object obj : parserData) {
            Award item = (Award) obj;

            int info_id = 0;

            IMTAward award = new IMTAward();

            int flight_id = 0;

            Date previousDate = null;

            for (Flight flight : item.getFlights()) {

                IMTFlight imtf = new IMTFlight();

                try {

                    imtf.setAircraft(flight.getAircraft());

                    Date arrDate = dt_format_or_time.parse(flight.getArriveDate().trim() + " " + flight.getArriveTime());

                    imtf.setArriveDate(date_format_res.format(arrDate));
                    imtf.setArrivePlace(flight.getArriveAirport() == null || flight.getArriveAirport().trim().length() == 0 ? flight.getArrivePlace() : dm.getCityByCode(flight.getArriveAirport().trim()));
                    imtf.setArriveCode(flight.getArriveAirport() == null || flight.getArriveAirport().trim().length() == 0 ? flight.getArrivePlace() : flight.getArriveAirport());

                    imtf.setArriveTime(flight.getArriveTime());

                    Date depDate = dt_format_or_time.parse(flight.getDepartDate().trim() + " " + flight.getDepartTime());

                    //    imtf.setLayoverTime(Utils.getHoursBetweenDays(previousDate, depDate));
                    previousDate = arrDate;

                    imtf.setDepartDate(date_format_res.format(depDate));

                    imtf.setDepartPlace(flight.getDepartAirport() == null || flight.getDepartAirport().trim().length() == 0 ? flight.getDepartPlace() : dm.getCityByCode(flight.getDepartAirport().trim()));
                    imtf.setDepartCode(flight.getDepartAirport() == null || flight.getDepartAirport().trim().length() == 0 ? flight.getDepartPlace() : flight.getDepartAirport());

                    imtf.setDepartTime(flight.getDepartTime());
                    imtf.setFlightNumber(flight.getFlight());
                    imtf.setAirlineCompany(Utils.getAirCompany(flight.getFlight()));
                    imtf.setMeal(flight.getMeal());
                    imtf.setTravelTime(flight.getTravelTime());
                    imtf.setId(flight_id);

                    award.getFlightList().add(imtf);

                    flight_id++;
                } catch (ParseException e) {
                } catch (Exception e) {
                }
            }

            String totalDuration = item.getTotalDuration();

            try {

                if ((totalDuration == null || totalDuration.trim().length() == 0) && award.getFlightList().size() < 2) {

                    award.setTotalDuration(parser.utils.Utils.getTotalTime(award.getFlightList().get(0).getTravelTime(), new QFParser()));

                } else {

                    award.setTotalDuration(totalDuration);
                }

                for (String cabinItem : Utils.getCabins(flightClass)) {

                    if (cabinItem.equals(ECONOMY)) {

                        Info infoSE = item.getSaverEconomy();

                        if (infoSE != null) {

                            info_id = getCabinClass(infoSE, ECONOMY, IMTInfo.SAVER, info_id, award, item.getFlights());
                        }

                    } else if (cabinItem.equals(BUSINESS)) {

                        Info infoSB = item.getSaverBusiness();

                        if (infoSB != null) {

                            info_id = getCabinClass(infoSB, BUSINESS, IMTInfo.SAVER, info_id, award, item.getFlights());
                        }

                    } else if (cabinItem.equals(FIRST)) {

                        Info infoSF = item.getSaverFirst();

                        if (infoSF != null) {

                            info_id = getCabinClass(infoSF, FIRST, IMTInfo.SAVER, info_id, award, item.getFlights());
                        }

                    } else if (cabinItem.equals(PREMIUM_ECONOMY)) {

                        Info infoSP = item.getSaverPremium();

                        if (infoSP != null) {

                            info_id = getCabinClass(infoSP, PREMIUM_ECONOMY, IMTInfo.SAVER, info_id, award, item.getFlights());
                        }

                    }
                }

                if (award.getClassList().size() > 0) {

                    awardList.add(award);
                }
            } catch (ParseException e) {
            } catch (SQLException e) {
            }
        }

        System.out.println("QF handler. Flight size after processing= [" + awardList.size() + "]");

        return awardList;
    }

    private Integer getCabinClass(Info info, String fCabin, String classDescription, int id, IMTAward award, List<Flight> fList) throws SQLException {

        IMTInfo imti = new IMTInfo();

        if (info != null && info.getStatus() != Info.NA) {

            DatabaseManager dm = new DatabaseManager();

            boolean usa = false;

            for (Flight flight : fList) {

                boolean depart = dm.isUSA(flight.getDepartAirport());
                boolean arrive = dm.isUSA(flight.getArriveAirport());

                System.out.println(depart);
                System.out.println(arrive);

                if (depart && arrive) {

                    flight.setUsa(true);
                    usa = true;

                } else {

                    flight.setUsa(false);
                }
            }

            imti.setMileage(info.getMileage().replaceAll("K", "000"));
//                imti.setName(fCabin);
            imti.setStatus(info.getStringStatus());
            imti.setTax(info.getTax());
            imti.setId(id);

//                award.getClass_list().add(imti);

            ExtraData extraData = new ExtraData();

            extraData.setFieldName("class_description");
            extraData.setFieldType("string");
            extraData.setFieldValue(classDescription);
            extraData.setFieldLvl("class_list");
            extraData.setFieldId("class_list_" + id);

            award.getExtraData().add(extraData);

            String currCabin = fCabin;

            if (info.isMixed()) {

                int flight_id = 0;

                for (String mixedCabins : info.getMixedCabins()) {

                    Mixed mixed = checkMixed(mixedCabins);

                    int f_id = 0;

                    for (IMTFlight flight : award.getFlightList()) {

                        System.out.println(flight.getDepartPlace());
                        System.out.println(mixed.getFrom());

                        if (flight.getDepartPlace().replaceAll("-", " ").toLowerCase().contains(mixed.getFrom().split(" ").length > 0 ? mixed.getFrom().toLowerCase().split(" ")[0].trim() : mixed.getFrom().toLowerCase())) {

                            System.out.println("FID = [" + f_id + "]");

                            break;
                        }

                        f_id++;
                    }

                    extraData = new ExtraData();

                    mixedCabins = Utils.getMixedCabinClass(mixed.getCabin());

                    currCabin = Utils.getCabin(mixedCabins, currCabin);

                    if (currCabin.equals(FIRST)) {

                        System.out.println("Curr Cabin " + currCabin);
                        System.out.println("Mixed Cabin " + mixedCabins);

                        System.out.println("Is USA " + fList.get(f_id).isUsa());

                        if (fList.get(f_id).isUsa() && mixedCabins.equals(FIRST)) {

                            mixedCabins = BUSINESS;

                            if (usa) {

                                currCabin = BUSINESS;
                            }

                        } else if (mixedCabins.equals(FIRST)) {

                            usa = false;
                            currCabin = FIRST;
                        }
                    }

                    System.out.println(currCabin);
                    System.out.println(mixedCabins);

                    extraData.setFieldName("mixed_cabins");
                    extraData.setFieldType("string");
                    extraData.setFieldValue(mixedCabins);
                    extraData.setFieldLvl("class_list");
                    extraData.setFieldId("class_list_" + id);
                    extraData.setFieldSubLvl("flight_list_" + f_id);

                    award.getExtraData().add(extraData);

                    flight_id++;
                }

                if (currCabin.equals(FIRST) && usa) {

                    currCabin = BUSINESS;
                }

                extraData = new ExtraData();

                extraData.setFieldName("mixed_description");
                extraData.setFieldType("string");
                extraData.setFieldValue(currCabin);
                extraData.setFieldLvl("class_list");
                extraData.setFieldId("class_list_" + id);

                award.getExtraData().add(extraData);
            }

            imti.setName(fCabin);

            if (info.isMixed()) {

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

    private Mixed checkMixed(String value) {

        Pattern pattern = Pattern.compile("(\\D*)\\sto\\s(\\D*):\\s(\\D*)");

        Matcher matcher = pattern.matcher(value);

        if (matcher.find()) {

            return new Mixed(matcher.group(1).trim(), matcher.group(2).trim(), matcher.group(3).trim());
        }

        return null;
    }

    private class Mixed {

        private String from;
        private String to;
        private String cabin;

        public Mixed(String from, String to, String cabin) {

            this.from = from;
            this.to = to;
            this.cabin = cabin;
        }

        /**
         * @return the from
         */
        public String getFrom() {
            return from;
        }

        /**
         * @param from the from to set
         */
        public void setFrom(String from) {
            this.from = from;
        }

        /**
         * @return the to
         */
        public String getTo() {
            return to;
        }

        /**
         * @param to the to to set
         */
        public void setTo(String to) {
            this.to = to;
        }

        /**
         * @return the cabin
         */
        public String getCabin() {
            return cabin;
        }

        /**
         * @param cabin the cabin to set
         */
        public void setCabin(String cabin) {
            this.cabin = cabin;
        }
    }
}