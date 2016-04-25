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
import java.text.NumberFormat;
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
public class AdaptorServiceJL implements Adaptor {
    @Override
    public List<IMTAward> adaptData(List<?> parserData, String... args) {
        String flightClass = args[0];
        String seats = args[1];
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat time_format_res = new SimpleDateFormat("yyyy:MM:dd HH:mm");
        DatabaseManager dm = new DatabaseManager();
        String result = "";
        List<IMTAward> awardList = new LinkedList<IMTAward>();

        for (Object obj : parserData) {
            Award item = (Award) obj;

            int info_id = 0;

            IMTAward award = new IMTAward();

//                    if (flightClass.equals(ALL)) {

            Info infoE = item.getEconomy();
            Info infoB = item.getBusiness();
            Info infoF = item.getFirst();
            Info infoPE = item.getPremiumEconomy();

            try {

                if (infoE != null) {

                    info_id = getCabinClass(infoE, ECONOMY, info_id, award);
                }

                if (infoB != null) {

                    info_id = getCabinClass(infoB, BUSINESS, info_id, award);
                }

                if (infoF != null) {

                    info_id = getCabinClass(infoF, FIRST, info_id, award);
                }

                if (infoPE != null) {

                    info_id = getCabinClass(infoPE, PREMIUM_ECONOMY, info_id, award);
                }

//                    }

//                    else if (flightClass.equals(ECONOMY)) {
//
//                        Info info = item.getEconomy();
//
//                        if (info != null) {
//
//                            info_id = getCabinClass(info, ECONOMY, info_id, award);
//                        }
//
//                    } else if (flightClass.equals(BUSINESS)) {
//
//                        Info info = item.getBusiness();
//
//                        if (info != null) {
//
//                            info_id = getCabinClass(info, BUSINESS, info_id, award);
//                        }
//
//                    } else if (flightClass.equals(FIRST)) {
//
//                        Info info = item.getFirst();
//
//                        if (info != null) {
//
//                            info_id = getCabinClass(info, FIRST, info_id, award);
//                        }
//
//                    } else if (flightClass.equals(PREMIUM_ECONOMY)) {
//
//                        Info info = item.getPremiumEconomy();
//
//                        if (info != null) {
//
//                            info_id = getCabinClass(info, PREMIUM_ECONOMY, info_id, award);
//                        }
//                    }
            } catch (ParseException e) {
            }
            int flight_id = 0;

            Date previousDate = null;

            for (Flight flight : item.getFlights()) {

                IMTFlight imtf = new IMTFlight();

                try {

                    imtf.setAircraft(flight.getAircraft());
                    imtf.setArriveDate(flight.getArriveDate());

                    imtf.setArrivePlace(dm.getCityByCode(flight.getArriveAirport().trim()));
                    imtf.setArriveCode(flight.getArriveAirport());
                    imtf.setArriveTime(flight.getArriveTime());
                    imtf.setDepartDate(flight.getDepartDate());

                    imtf.setDepartPlace(dm.getCityByCode(flight.getDepartAirport().trim()));
                    imtf.setDepartCode(flight.getDepartAirport());
                    imtf.setDepartTime(flight.getDepartTime());
                    imtf.setFlightNumber(flight.getFlight());
                    imtf.setAirlineCompany(Utils.getAirCompany(flight.getFlight()));
                    imtf.setMeal(flight.getMeal());

                    Date depDate = time_format_res.parse(flight.getDepartDate() + " " + flight.getDepartTime());
                    Date arrDate = time_format_res.parse(flight.getArriveDate() + " " + flight.getArriveTime());

                    imtf.setTravelTime(Utils.getHoursBetweenDays(depDate, arrDate));

                    //   imtf.setLayoverTime(Utils.getHoursBetweenDays(previousDate, depDate));
                    previousDate = arrDate;

                    imtf.setId(flight_id);

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

        System.out.println("JL handler. Flight size after processing= [" + awardList.size() + "]");

        return awardList;
    }

    private Integer getCabinClass(Info info, String fCabin, int id, IMTAward award) throws ParseException {

        NumberFormat nf_in = NumberFormat.getNumberInstance(Locale.UK);

        IMTInfo imti = new IMTInfo();

        if (info.isNa()) {

            info.setStatus(Info.NA);
        }

        if (info != null && info.getStatus() != Info.NA && info.getStatus() != Info.WAITLIST && !info.isNa()) {

            Integer mileage = nf_in.parse(info.getMileage()).intValue();

            String tax = info.getTax();

            Pattern pattern = Pattern.compile("(\\d+\\.\\d*)(\\D+)");

            Matcher matcher = pattern.matcher(tax);

            if (matcher.find()) {

                imti.setTax(Double.parseDouble(matcher.group(1).trim()) / 2 + "");
                imti.setCurrency(matcher.group(2).trim().replaceAll(" ", ""));
            }

            imti.setMileage(nf_in.format(mileage / 2));
            imti.setName(fCabin);
            imti.setStatus(info.getStringStatus());
//                imti.setTax(info.getTax());
            imti.setId(id);

            ExtraData extraData = new ExtraData();

            extraData.setFieldName("class_description");
            extraData.setFieldType("string");
            extraData.setFieldValue(IMTInfo.SAVER);
            extraData.setFieldLvl("class_list");
            extraData.setFieldId("class_list_" + id);

            award.getExtraData().add(extraData);

            id++;

            award.getClassList().add(imti);
        }

        return id;
    }
}