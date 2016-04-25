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
public class AdaptorServiceSQ implements Adaptor {
    @Override
    public List<IMTAward> adaptData(List<?> parserData, String... args) {
        String flightClass = args[0];
        String seats = args[1];
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy:MM:dd");
        SimpleDateFormat sq_sdf = new SimpleDateFormat("dd MMM (EEE) yyyy", Locale.US);//15 Mar (Tue) 2016
        SimpleDateFormat time_format_res = new SimpleDateFormat("yyyy:MM:dd HH:mm");

        String result;

        System.out.println("SQ handler. Flight size before processing= [" + parserData.size() + "]");

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


                    Date date1 = sq_sdf.parse(flight.getArriveDate().trim());

                    imtf.setArriveDate(sdf1.format(date1));
                    imtf.setArrivePlace(dm.getCityByCode(flight.getArriveAirport().trim()));
                    imtf.setArriveCode(flight.getArriveAirport());
                    imtf.setArriveTime(flight.getArriveTime().trim());

                    Date date2 = sq_sdf.parse(flight.getDepartDate().trim());

                    imtf.setDepartDate(sdf1.format(date2));
                    imtf.setDepartPlace(dm.getCityByCode(flight.getDepartAirport().trim()));
                    imtf.setDepartCode(flight.getDepartAirport());
                    imtf.setDepartTime(flight.getDepartTime().trim());
                    imtf.setFlightNumber(flight.getfNumber());
                    imtf.setAirlineCompany(Utils.getAirCompany(flight.getfNumber()));
                    imtf.setMeal(flight.getMeal());
                    imtf.setId(flight_id);

                    Date arrDate = time_format_res.parse(imtf.getArriveDate() + " " + imtf.getArriveTime());
                    Date depDate = time_format_res.parse(imtf.getDepartDate() + " " + imtf.getDepartTime());

                    Calendar depCalendar = new GregorianCalendar();
                    depCalendar.setTime(depDate);
                    depCalendar.add(Calendar.SECOND, -dm.getTZByCode(imtf.getDepartCode()));

                    Calendar arrCalendar = new GregorianCalendar();
                    arrCalendar.setTime(arrDate);
                    arrCalendar.add(Calendar.SECOND, -dm.getTZByCode(imtf.getArriveCode()));

                    imtf.setTravelTime(Utils.getHoursBetweenDays(depCalendar.getTime(), arrCalendar.getTime()));

                    imtf.setLayoverTime(Utils.getHoursBetweenDays(previousDate, depDate));
                    previousDate = arrDate;

                    award.getFlightList().add(imtf);

                    flight_id++;
                } catch (ParseException e) {
                } catch (SQLException e) {
                } catch (Exception e) {
                }
            }

            award.setTotalDuration(item.getTotalDuration());

//                if (flightClass.equals(ALL)) {

            Info infoSE = item.getSaverEconomy();
            Info infoSTE = item.getStandartEconomy();
            Info infoFE = item.getFullEconomy();

            if (infoSE != null || infoSTE != null || infoFE != null) {

                info_id = getCabinClass(infoSE, ECONOMY, IMTInfo.SAVER, info_id, award, Integer.parseInt(seats));
                info_id = getCabinClass(infoSTE, ECONOMY, IMTInfo.STANDART, info_id, award, Integer.parseInt(seats));
                info_id = getCabinClass(infoFE, ECONOMY, IMTInfo.FULL, info_id, award, Integer.parseInt(seats));
            }

            Info infoSB = item.getSaverBusiness();
            Info infoSTB = item.getStandartBusiness();
            Info infoFB = item.getFullBusiness();

            if (infoSB != null || infoSTB != null || infoFB != null) {

                info_id = getCabinClass(infoSB, BUSINESS, IMTInfo.SAVER, info_id, award, Integer.parseInt(seats));
                info_id = getCabinClass(infoSTB, BUSINESS, IMTInfo.STANDART, info_id, award, Integer.parseInt(seats));
                info_id = getCabinClass(infoFB, BUSINESS, IMTInfo.FULL, info_id, award, Integer.parseInt(seats));
            }

            Info infoSF = item.getSaverFirst();
            Info infoSTF = item.getStandartFirst();
            Info infoFF = item.getFullFirst();

            if (infoSF != null || infoSTF != null || infoFF != null) {

                info_id = getCabinClass(infoSF, FIRST, IMTInfo.SAVER, info_id, award, Integer.parseInt(seats));
                info_id = getCabinClass(infoSTF, FIRST, IMTInfo.STANDART, info_id, award, Integer.parseInt(seats));
                info_id = getCabinClass(infoFF, FIRST, IMTInfo.FULL, info_id, award, Integer.parseInt(seats));
            }

//                }

//                else if (flightClass.equals(ECONOMY)) {
//
//                    Info info1 = item.getSaverEconomy();
//                    Info info2 = item.getStandartEconomy();
//                    Info info3 = item.getFullEconomy();
//
//                    if (info1 != null || info2 != null || info3 != null) {
//
//                        info_id = getCabinClass(info1, ECONOMY, IMTInfo.SAVER, info_id, award, Integer.parseInt(seats), type);
//                        info_id = getCabinClass(info2, ECONOMY, IMTInfo.STANDART, info_id, award, Integer.parseInt(seats), type);
//                        info_id = getCabinClass(info3, ECONOMY, IMTInfo.FULL, info_id, award, Integer.parseInt(seats), type);
//                    }
//
//                } else if (flightClass.equals(BUSINESS)) {
//
//                    Info info1 = item.getSaverBusiness();
//                    Info info2 = item.getStandartBusiness();
//                    Info info3 = item.getFullBusiness();
//
//                    if (info1 != null || info2 != null || info3 != null) {
//
//                        info_id = getCabinClass(info1, BUSINESS, IMTInfo.SAVER, info_id, award, Integer.parseInt(seats), type);
//                        info_id = getCabinClass(info2, BUSINESS, IMTInfo.STANDART, info_id, award, Integer.parseInt(seats), type);
//                        info_id = getCabinClass(info3, BUSINESS, IMTInfo.FULL, info_id, award, Integer.parseInt(seats), type);
//                    }
//
//                } else if (flightClass.equals(FIRST)) {
//
//                    Info info1 = item.getSaverFirst();
//                    Info info2 = item.getStandartFirst();
//                    Info info3 = item.getFullFirst();
//
//                    if (info1 != null || info2 != null || info3 != null) {
//
//                        info_id = getCabinClass(info1, FIRST, IMTInfo.SAVER, info_id, award, Integer.parseInt(seats), type);
//                        info_id = getCabinClass(info2, FIRST, IMTInfo.STANDART, info_id, award, Integer.parseInt(seats), type);
//                        info_id = getCabinClass(info3, FIRST, IMTInfo.FULL, info_id, award, Integer.parseInt(seats), type);
//                    }
//
//                }

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

        System.out.println("SQ handler. Flight size after processing= [" + awardList.size() + "]");

        return awardList;
    }

    private Integer getCabinClass(Info info, String fCabin, String classDescription, int id, IMTAward award, int seats) {

        IMTInfo imti = new IMTInfo();

        if (info != null && info.getStatus() != Info.NA && info.getStatus() != Info.WAITLIST) {

            Integer mileage = Integer.parseInt(info.getMileage().replaceAll(",", "").trim());

            mileage = (mileage - (int) (mileage * 0.15));

            if (info.getTax() != null && info.getTax().trim().length() > 0) {

                Double tax = Double.parseDouble(info.getTax());

                imti.setTax(tax / seats + "");

            } else {

                System.out.println("Empty tax");
            }

//                if(SQParser.TYPE_RT.equals(type)){
//
//                       mileage = mileage / 2;
//                }

            imti.setMileage(mileage + "");
            imti.setName(fCabin);
            imti.setStatus(info.getStringStatus());

            imti.setCurrency(info.getCurrency());
            imti.setId(id);

            System.out.println(fCabin);
            System.out.println(imti.getTax());
            System.out.println(imti.getCurrency());

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