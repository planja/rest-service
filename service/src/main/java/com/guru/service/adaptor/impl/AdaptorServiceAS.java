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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static parser.Parser.*;

/**
 * Created by Anton on 13.04.2016.
 */
public class AdaptorServiceAS implements Adaptor {
    @Override
    public List<IMTAward> adaptData(List<?> parserData, String... args) {
        String flightClass = args[0];
        String seats = args[1];
        String result = "";

        SimpleDateFormat dt_format_or = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat date_format_res = new SimpleDateFormat("yyyy:MM:dd");
        SimpleDateFormat time_format_res = new SimpleDateFormat("HH:mm");


        List<IMTAward> awardList = new LinkedList<IMTAward>();

        DatabaseManager dm = new DatabaseManager();

        for (Object obj : parserData) {
            Award item = (Award) obj;

            int info_id = 0;

            IMTAward award = new IMTAward();

            for (String cabinItem : Utils.getCabins(flightClass)) {

                try {

                    if (cabinItem.equals(ECONOMY)) {

                        Info infoSE = item.getSaverEconomy();
                        Info infoFE = item.getFullEconomy();

                        if (infoSE != null) {

                            info_id = getCabinClass(infoSE, ECONOMY, IMTInfo.SAVER, info_id, award, item.getFlights());
                        }

                        if (infoFE != null) {

                            info_id = getCabinClass(infoFE, ECONOMY, IMTInfo.FULL, info_id, award, item.getFlights());
                        }

                    } else if (cabinItem.equals(BUSINESS)) {

                        Info infoSB = item.getSaverBusiness();

                        if (infoSB != null) {

                            info_id = getCabinClass(infoSB, BUSINESS, IMTInfo.SAVER, info_id, award, item.getFlights());
                        }

                    } else if (cabinItem.equals(FIRST)) {

                        Info infoSF = item.getSaverFirst();
                        Info infoFF = item.getFullFirst();

                        if (infoSF != null) {

                            info_id = getCabinClass(infoSF, FIRST, IMTInfo.SAVER, info_id, award, item.getFlights());
                        }

                        if (infoFF != null) {

                            info_id = getCabinClass(infoFF, FIRST, IMTInfo.FULL, info_id, award, item.getFlights());
                        }

                    } else if (cabinItem.equals(PREMIUM_ECONOMY)) {

                        Info infoSP = item.getSaverPremium();

                        if (infoSP != null) {

                            info_id = getCabinClass(infoSP, PREMIUM_ECONOMY, IMTInfo.SAVER, info_id, award, item.getFlights());
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

                    Date arrDate = dt_format_or.parse(flight.getArriveDate() + " " + flight.getArriveTime());

                    imtf.setArriveDate(date_format_res.format(arrDate));
                    imtf.setArrivePlace(dm.getCityByCode(flight.getArriveAirport()));
                    imtf.setArriveCode(flight.getArriveAirport());
                    imtf.setArriveTime(time_format_res.format(arrDate));

                    Date depDate = dt_format_or.parse(flight.getDepartDate() + " " + flight.getDepartTime());

                    imtf.setLayoverTime(Utils.getHoursBetweenDays(previousDate, depDate));
                    previousDate = arrDate;

                    imtf.setDepartDate(date_format_res.format(depDate));
                    imtf.setDepartPlace(dm.getCityByCode(flight.getDepartAirport()));
                    imtf.setDepartCode(flight.getDepartAirport());
                    imtf.setDepartTime(time_format_res.format(depDate));
                    imtf.setFlightNumber(flight.getFlight());
                    imtf.setAirlineCompany(Utils.getAirCompany(flight.getFlight()));
                    imtf.setMeal(flight.getMeal());
                    imtf.setTravelTime(Utils.getHoursBetweenDays(depDate, arrDate));
                    imtf.setId(flight_id);

                    award.getFlightList().add(imtf);

                    flight_id++;
                } catch (ParseException e) {
                } catch (Exception e) {
                }
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

        System.out.println("AS handler. Flight size after processing= [" + awardList.size() + "]");

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

                if (depart && arrive) {

                    flight.setUsa(true);
                    usa = true;

                } else {

                    flight.setUsa(false);
                }
            }


            Double dMileage = Double.parseDouble(info.getMileage().replaceAll("k", "").trim()) * 1000;

            imti.setMileage(dMileage.intValue() + "");
//                imti.setName(fCabin);
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

//                award.getClass_list().add(imti);

            int flight_id = 0;

            boolean mixed = false;

            String currCabin = fCabin;

            for (String mixedCabins : info.getMixedCabins()) {

                mixed = true;

                extraData = new ExtraData();

                currCabin = Utils.getCabin(mixedCabins, currCabin);

                if (currCabin.equals(FIRST)) {

                    if (fList.get(flight_id).isUsa() && mixedCabins.equals(FIRST)) {

                        mixedCabins = BUSINESS;

                        if (usa) {

                            currCabin = BUSINESS;
                        }

                    } else if (mixedCabins.equals(FIRST)) {

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

            if (mixed) {

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
}