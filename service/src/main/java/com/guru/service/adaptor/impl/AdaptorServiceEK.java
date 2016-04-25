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
import java.util.Locale;

import static parser.Parser.*;

/**
 * Created by Anton on 13.04.2016.
 */
public class AdaptorServiceEK implements Adaptor {
    @Override
    public List<IMTAward> adaptData(List<?> parserData, String... args) {
        String flightClass = args[0];
        String seats = args[1];
        SimpleDateFormat dt_format_or = new SimpleDateFormat("dd MMMMM yyyy HH:mm", Locale.US);
        SimpleDateFormat date_format_res = new SimpleDateFormat("yyyy:MM:dd");
        SimpleDateFormat time_format_res = new SimpleDateFormat("HH:mm");

        System.out.println("EK handler. Flight size before processing= [" + parserData.size() + "]");

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

                try {

                    imtf.setAircraft(flight.getAircraft());

                    //28 July 2015 19:45
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
                    imtf.setTravelTime(flight.getTravelTime());
                    imtf.setId(flight_id);

                    previousCabin = flight.getFlightCabin();
                    cabinList.add(flight.getFlightCabin());

                    award.getFlightList().add(imtf);

                    flight_id++;
                } catch (ParseException e) {
                } catch (SQLException e) {
                } catch (Exception e) {
                }
            }

//                if (flightClass.equals(ALL)) {

            Info infoE = item.getEconomy();
            Info infoB = item.getBusiness();
            Info infoF = item.getFirst();

            if (infoE != null) {

                info_id = getCabinClass(infoE, ECONOMY, info_id, award, cabinList, mixed, Integer.parseInt(seats));
            }

            if (infoB != null) {

                info_id = getCabinClass(infoB, BUSINESS, info_id, award, cabinList, mixed, Integer.parseInt(seats));
            }

            if (infoF != null) {

                info_id = getCabinClass(infoF, FIRST, info_id, award, cabinList, mixed, Integer.parseInt(seats));
            }

//                }

//                else if (flightClass.equals(ECONOMY)) {
//
//                    Info info = item.getEconomy();
//
//                    if (info != null) {
//
//                        info_id = getCabinClass(info, ECONOMY, info_id, award, cabinList, mixed, Integer.parseInt(seats));
//                    }
//
//                } else if (flightClass.equals(BUSINESS)) {
//
//                    Info info = item.getBusiness();
//
//                    if (info != null) {
//
//                        info_id = getCabinClass(info, BUSINESS, info_id, award, cabinList, mixed, Integer.parseInt(seats));
//                    }
//
//                } else if (flightClass.equals(FIRST)) {
//
//                    Info info = item.getFirst();
//
//                    if (info != null) {
//
//                        info_id = getCabinClass(info, FIRST, info_id, award, cabinList, mixed, Integer.parseInt(seats));
//                    }
//
//                }

            award.setTotalDuration(item.getTotalDuration());

            if (award.getClassList().size() > 0) {

                awardList.add(award);
            }
        }

        System.out.println("EK handler. Flight size after processing= [" + awardList.size() + "]");

        return awardList;
    }

    private Integer getCabinClass(Info info, String fCabin, int id, IMTAward award, List<String> cabinList, boolean mixed, int seats) {

        IMTInfo imti = new IMTInfo();

        if (info != null && info.getStatus() != Info.NA) {

            String tax = info.getTax();
            String miles = info.getMileage().replaceAll(",", "").replaceAll("Miles", "").trim();

            String currency = tax.substring(0, 3);
            String t = tax.substring(3);

            imti.setMileage(Integer.parseInt(miles) / seats + "");
            imti.setName(fCabin);
            imti.setStatus(info.getStringStatus());
            imti.setTax(Double.parseDouble(t.replaceAll(",", "").trim()) / seats + "");
            imti.setCurrency(currency);
            imti.setId(id);

            ExtraData extraData = new ExtraData();

            extraData.setFieldName("class_description");
            extraData.setFieldType("string");
            extraData.setFieldValue(IMTInfo.SAVER);
            extraData.setFieldLvl("class_list");
            extraData.setFieldId("class_list_" + id);

            award.getExtraData().add(extraData);

            award.getClassList().add(imti);

            id++;
        }

        return id;
    }

}