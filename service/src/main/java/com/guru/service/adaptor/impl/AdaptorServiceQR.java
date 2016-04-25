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
import java.text.SimpleDateFormat;
import java.util.*;

import static parser.Parser.*;

/**
 * Created by Anton on 13.04.2016.
 */
public class AdaptorServiceQR implements Adaptor {
    @Override
    public List<IMTAward> adaptData(List<?> parserData, String... args) {
        String flightClass = args[0];
        String seats = args[1];
        DatabaseManager dm = new DatabaseManager();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat dt_format_or = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US);
        SimpleDateFormat date_format_res = new SimpleDateFormat("yyyy:MM:dd");
        SimpleDateFormat time_format_res = new SimpleDateFormat("HH:mm");
        String result = "";
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

                    Calendar arrCalendar = new GregorianCalendar();
                    arrCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
                    arrCalendar.setTime(flight.getArrDT());

//                        System.out.println(arrCalendar.getTime());
                    arrCalendar.add(Calendar.SECOND, dm.getTZByCode(flight.getArriveAirport().trim()));

                    Calendar depCalendar = new GregorianCalendar();
                    depCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
                    depCalendar.setTime(flight.getDepDT());

//                        System.out.println(depCalendar.getTime());
                    depCalendar.add(Calendar.SECOND, dm.getTZByCode(flight.getDepartAirport().trim()));

                    imtf.setArriveDate(date_format_res.format(arrCalendar.getTime()));
                    imtf.setArrivePlace(dm.getCityByCode(Utils.replaceAirport(flight.getArriveAirport(), Utils.QR)));
                    imtf.setArriveCode(flight.getArriveAirport());
                    imtf.setArriveTime(time_format_res.format(arrCalendar.getTime()));

                    imtf.setLayoverTime(flight.getLayover());

                    imtf.setDepartDate(date_format_res.format(depCalendar.getTime()));
                    imtf.setDepartPlace(dm.getCityByCode(Utils.replaceAirport(flight.getDepartAirport(), Utils.QR)));
                    imtf.setDepartCode(flight.getDepartAirport());
                    imtf.setDepartTime(time_format_res.format(depCalendar.getTime()));
                    imtf.setFlightNumber(flight.getFlight());
                    imtf.setAirlineCompany(Utils.getAirCompany(flight.getFlight()));
                    imtf.setMeal(flight.getMeal());
                    imtf.setTravelTime(flight.getTravelTime());
                    imtf.setId(flight_id);

                    award.getFlightList().add(imtf);

                    flight_id++;
                } catch (SQLException e) {
                }
            }

//                    if (flightClass.equals(ALL)) {
//                        Info infoSE = item.getSaverEconomy();
//                        Info infoSB = item.getSaverBusiness();
//                        Info infoSF = item.getSaverFirst();
//
//                        if (infoSE != null) {
//
//                            info_id = getCabinClass(infoSE, ECONOMY, SAVER, info_id, award);
//                        }
//
//                        if (infoSB != null) {
//
//                            info_id = getCabinClass(infoSB, BUSINESS, SAVER, info_id, award);
//                        }
//
//                        if (infoSF != null) {
//
//                            info_id = getCabinClass(infoSF, FIRST, SAVER, info_id, award);
//                        }
//                    }
            for (String cabinItem : Utils.getCabins(flightClass)) {

                if (cabinItem.equals(ECONOMY)) {

                    Info infoS = item.getSaverEconomy();

                    if (infoS != null) {

                        info_id = getCabinClass(infoS, ECONOMY, IMTInfo.SAVER, info_id, award);
                    }

                } else if (cabinItem.equals(BUSINESS)) {

                    Info infoS = item.getSaverBusiness();

                    if (infoS != null) {

                        info_id = getCabinClass(infoS, BUSINESS, IMTInfo.SAVER, info_id, award);
                    }

                } else if (cabinItem.equals(FIRST)) {

                    Info infoS = item.getSaverFirst();

                    if (infoS != null) {

                        info_id = getCabinClass(infoS, FIRST, IMTInfo.SAVER, info_id, award);
                    }
                }
            }

            award.setTotalDuration(item.getTotalDuration());

            if (award.getClassList().size() > 0) {

                awardList.add(award);
            }
        }

        System.out.println("QR handler. Flight size after processing= [" + awardList.size() + "]");

        return awardList;
    }

    private Integer getCabinClass(Info info, String fCabin, String classDescription, int id, IMTAward award) {

        IMTInfo imti = new IMTInfo();


        if (info != null && info.getStatus() != Info.NA) {

            imti.setMileage(info.getMileage());
            imti.setName(fCabin);
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

            award.getClassList().add(imti);

            id++;
        }

        return id;
    }
}