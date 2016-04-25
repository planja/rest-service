package  com.guru.service.adaptor.impl;

import com.guru.vo.view.Award;
import com.guru.vo.view.Flight;
import com.guru.vo.view.Info;
import service.adaptor.db.DatabaseManager;
import com.guru.service.adaptor.interf.Adaptor;
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
public class AdaptorServiceAC implements Adaptor {
    @Override
    public List<IMTAward> adaptData(List<?> parserData, String... args) {
        String flightClass = args[0];
        String seats = args[1];
        String result = "";

        SimpleDateFormat dt_format_or = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat date_format_res = new SimpleDateFormat("yyyy:MM:dd");
        SimpleDateFormat time_format_res = new SimpleDateFormat("HH:mm");

        DatabaseManager dm = new DatabaseManager();

        List<IMTAward> awardList = new LinkedList<IMTAward>();

        for (Object obj : parserData) {
            Award item = (Award) obj;
            int info_id = 0;

            IMTAward award = new IMTAward();

            int flight_id = 0;

            Date previousDate = null;

            List<String> cabinList = new LinkedList<>();

            String previousCabin = null;

            boolean mixed = false;

            for (Flight flight : item.getFlights()) {

                IMTFlight imtf = new IMTFlight();

                imtf.setAircraft(flight.getAircraft());
                try {
                    Date arrDate = dt_format_or.parse(flight.getArriveDate() + " " + flight.getArriveTime());

                    imtf.setArriveDate(date_format_res.format(arrDate));
                    imtf.setArrivePlace(dm.getCityByCode(flight.getArriveAirport().trim()));
                    imtf.setArriveCode(flight.getArriveAirport());
                    imtf.setArriveTime(time_format_res.format(arrDate));

                    Date depDate = dt_format_or.parse(flight.getDepartDate() + " " + flight.getDepartTime());

                    imtf.setLayoverTime(Utils.getHoursBetweenDays(previousDate, depDate));
                    previousDate = arrDate;

                    imtf.setDepartDate(date_format_res.format(depDate));
                    imtf.setDepartPlace(dm.getCityByCode(flight.getDepartAirport().trim()));
                    imtf.setDepartCode(flight.getDepartAirport());
                    imtf.setDepartTime(time_format_res.format(depDate));
                    imtf.setFlightNumber(flight.getFlight());
                    imtf.setAirlineCompany(Utils.getAirCompany(flight.getFlight()));
                    imtf.setMeal(flight.getMeal());
                    imtf.setTravelTime(flight.getTravelTime());
                    imtf.setId(flight_id);
                } catch (ParseException e) {
                } catch (SQLException e) {
                } catch (Exception e) {
                }
                if (previousCabin != null && !previousCabin.equals(flight.getFlightCabin())) {

                    mixed = true;
                }

                previousCabin = flight.getFlightCabin();
                cabinList.add(flight.getFlightCabin());

                award.getFlightList().add(imtf);

                flight_id++;
            }

//                if (flightClass.equals(ALL)) {
//                Info infoE = item.getEconomy();
//                Info infoB = item.getBusiness();
//                Info infoPE = item.getPremiumEconomy();
//                Info infoF = item.getFirst();
//
//                if (infoE != null) {
//
//                    info_id = getCabinClass(infoE, ECONOMY, info_id, award, cabinList, mixed, Integer.parseInt(seats));
//                }
//
//                if (infoB != null) {
//
//                    info_id = getCabinClass(infoB, BUSINESS, info_id, award, cabinList, mixed, Integer.parseInt(seats));
//                }
//
//                if (infoPE != null) {
//
//                    info_id = getCabinClass(infoPE, PREMIUM_ECONOMY, info_id, award, cabinList, mixed, Integer.parseInt(seats));
//                }
//
//                if (infoF != null) {
//
//                    info_id = getCabinClass(infoF, FIRST, info_id, award, cabinList, mixed, Integer.parseInt(seats));
//                }
            for (String cabinItem : Utils.getCabins(flightClass)) {

                try {

                    if (cabinItem.equals(ECONOMY)) {

                        Info info = item.getEconomy();

                        if (info != null) {

                            info_id = getCabinClass(info, ECONOMY, info_id, award, cabinList, mixed, Integer.parseInt(seats), item.getFlights());
                        }

                    } else if (cabinItem.equals(BUSINESS)) {

                        Info info = item.getBusiness();

                        if (info != null) {

                            info_id = getCabinClass(info, BUSINESS, info_id, award, cabinList, mixed, Integer.parseInt(seats), item.getFlights());
                        }

                    } else if (cabinItem.equals(FIRST)) {

                        Info info = item.getFirst();

                        if (info != null) {

                            info_id = getCabinClass(info, FIRST, info_id, award, cabinList, mixed, Integer.parseInt(seats), item.getFlights());
                        }

                    } else if (cabinItem.equals(PREMIUM_ECONOMY)) {

                        Info info = item.getPremiumEconomy();

                        if (info != null) {

                            info_id = getCabinClass(info, PREMIUM_ECONOMY, info_id, award, cabinList, mixed, Integer.parseInt(seats), item.getFlights());
                        }
                    }
                } catch (SQLException e) {
                }
            }

//                }
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

        System.out.println("AC handler. Flight size after processing= [" + awardList.size() + "]");

        return awardList;
    }

    private Integer getCabinClass(Info info, String fCabin, int id, IMTAward award, List<String> cabinList, boolean mixed, int seats, List<Flight> flightList) throws SQLException {

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

            String mil = info.getMileage();

            imti.setMileage(Integer.parseInt(mil) / seats + "");
//                imti.setName(fCabin);
            imti.setStatus(info.getStringStatus());
            imti.setTax(info.getTax() != null && info.getTax().trim().length() > 0 ? Double.parseDouble(info.getTax().replaceAll(",", "").trim()) / seats + "" : "");
            imti.setCurrency(info.getCurrency());
            imti.setId(id);

            System.out.println("Tax = [" + info.getTax() + "]");

            ExtraData extraData = new ExtraData();

            extraData.setFieldName("class_description");
            extraData.setFieldType("string");
            extraData.setFieldValue(IMTInfo.SAVER);
            extraData.setFieldLvl("class_list");
            extraData.setFieldId("class_list_" + id);

            award.getExtraData().add(extraData);

            int flight_id = 0;

            String currCabin = fCabin;

//                if (mixed) {
            for (String mixedCabins : cabinList) {

                mixed = true;

                extraData = new ExtraData();

                currCabin = Utils.getCabin(mixedCabins, currCabin);

                if (currCabin.equals(FIRST)) {

                    if (flightList.get(flight_id).isUsa() && mixedCabins.equals(FIRST)) {

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
//                }

            imti.setName(fCabin);

            if (mixed) {

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
