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
public class AdaptorServiceEY implements Adaptor {
    @Override
    public List<IMTAward> adaptData(List<?> parserData, String... args) {
        String flightClass = args[0];
        String seats = args[1];
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        SimpleDateFormat dt_format_or = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        SimpleDateFormat date_format_res = new SimpleDateFormat("yyyy:MM:dd");
        SimpleDateFormat time_format_res = new SimpleDateFormat("HH:mm");
        String result = "";
        List<IMTAward> awardList = new LinkedList<IMTAward>();

        DatabaseManager dm = new DatabaseManager();

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

                    Date arrDate = dt_format_or.parse(flight.getArriveDate().trim() + " " + flight.getArriveTime());

                    imtf.setArriveDate(date_format_res.format(arrDate));
                    imtf.setArriveTime(time_format_res.format(arrDate));

                    imtf.setArrivePlace(dm.getCityByCode(flight.getArrivePlace()));
                    imtf.setArriveCode(flight.getArrivePlace());
                    imtf.setDepartPlace(dm.getCityByCode(flight.getDepartPlace()));
                    imtf.setDepartCode(flight.getDepartPlace());

                    Date depDate = dt_format_or.parse(flight.getDepartDate().trim() + " " + flight.getDepartTime());

                    imtf.setDepartDate(date_format_res.format(depDate));
                    imtf.setDepartTime(time_format_res.format(depDate));

                    imtf.setFlightNumber(flight.getFlight());
                    imtf.setAirlineCompany(Utils.getAirCompany(flight.getFlight()));
                    imtf.setMeal(flight.getMeal());
                    imtf.setTravelTime(flight.getTravelTime());
                    imtf.setId(flight_id);

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

//                    if (flightClass.equals(ALL)) {

            Info infoE = item.getEconomy();
            Info infoSTE = item.getStandartEconomy();
            Info infoFE = item.getFullEconomy();
            Info infoSE = item.getSaverEconomy();
//
            if (infoE != null) {

                info_id = getCabinClass(infoE, ECONOMY, IMTInfo.SAVER, IMTInfo.SAVER, info_id, award);
            }

//                    if (infoSE != null) {
//
//                        info_id = getCabinClass(infoSE, ECONOMY, IMTInfo.SAVER,  IMTInfo.SAVER, info_id, award);
//                    }
//                    if (infoSTE != null) {
//
//                        info_id = getCabinClass(infoSTE, ECONOMY, IMTInfo.STANDART,  IMTInfo.STANDART, info_id, award);
//                    }
//
//                    if (infoFE != null) {
//
//                        info_id = getCabinClass(infoFE, ECONOMY, IMTInfo.FULL,  IMTInfo.FULL, info_id, award);
//                    }
            Info infoB = item.getBusiness();
            Info infoSB = item.getSaverBusiness();
            Info infoSTB = item.getStandartBusiness();
            Info infoFB = item.getFullBusiness();

            if (infoB != null) {

                info_id = getCabinClass(infoB, BUSINESS, IMTInfo.SAVER, IMTInfo.SAVER, info_id, award);
            }

//                        if (infoSB != null) {
//
//                            info_id = getCabinClass(infoSB, BUSINESS, IMTInfo.SAVER, IMTInfo.SAVER, info_id, award);
//                        }
////
//                    if (infoSTB != null) {
//
//                        info_id = getCabinClass(infoSTB, BUSINESS, IMTInfo.STANDART,  IMTInfo.STANDART, info_id, award);
//                    }
//
//                    if (infoFB != null) {
//
//                        info_id = getCabinClass(infoFB, BUSINESS, IMTInfo.FULL,  IMTInfo.FULL, info_id, award);
//                    }

            Info infoF = item.getFirst();
            Info infoSF = item.getSaverFirst();
            Info infoFF = item.getFullFirst();

            if (infoF != null) {

                info_id = getCabinClass(infoF, FIRST, IMTInfo.SAVER, IMTInfo.SAVER, info_id, award);
            }

//                    if (infoFF != null) {
//
//                        info_id = getCabinClass(infoFF, FIRST, IMTInfo.FULL, IMTInfo.FULL, info_id, award);
//                    }
//                    }

//                    else if (flightClass.equals(ECONOMY)) {
//
//                        Info infoE = item.getEconomy();
//                        Info infoSTE = item.getStandartEconomy();
//                        Info infoFE = item.getFullEconomy();
//                        Info infoSE = item.getSaverEconomy();
//
//                        if (infoE != null) {
//
//                            info_id = getCabinClass(infoE, ECONOMY, IMTInfo.SAVER, IMTInfo.SAVER, info_id, award);
//                        }
//
////                    if (infoSE != null) {
////
////                        info_id = getCabinClass(infoSE, ECONOMY, IMTInfo.SAVER, IMTInfo.SAVER, info_id, award);
////                    }
////                    if (infoSTE != null) {
////
////                        info_id = getCabinClass(infoSTE, ECONOMY, IMTInfo.STANDART, IMTInfo.STANDART, info_id, award);
////                    }
////
////                    if (infoFE != null) {
////
////                        info_id = getCabinClass(infoFE, ECONOMY, IMTInfo.FULL, IMTInfo.FULL, info_id, award);
////                    }
//                    } else if (flightClass.equals(BUSINESS)) {
//
//                        Info infoSB = item.getSaverBusiness();
//                        Info infoSTB = item.getStandartBusiness();
//                        Info infoFB = item.getFullBusiness();
//
//                        if (infoSB != null) {
//
//                            info_id = getCabinClass(infoSB, BUSINESS, IMTInfo.SAVER, IMTInfo.SAVER, info_id, award);
//                        }
////
////                    if (infoSTB != null) {
////
////                        info_id = getCabinClass(infoSTB, BUSINESS, IMTInfo.STANDART, IMTInfo.STANDART, info_id, award);
////                    }
////
////                    if (infoFB != null) {
////
////                        info_id = getCabinClass(infoFB, BUSINESS, IMTInfo.FULL, IMTInfo.FULL, info_id, award);
////                    }
//
//                    } else if (flightClass.equals(FIRST)) {
//
//                        Info infoF = item.getFirst();
//                        Info infoSF = item.getSaverFirst();
//                        Info infoFF = item.getFullFirst();
//
//                        if (infoF != null) {
//
//                            info_id = getCabinClass(infoF, FIRST, IMTInfo.SAVER, IMTInfo.SAVER, info_id, award);
//                        }
//
////                        if (infoSF != null) {
////
////                            info_id = getCabinClass(infoSF, FIRST, IMTInfo.SAVER, IMTInfo.SAVER, info_id, award);
////                        }
////
////                    if (infoFF != null) {
////
////                        info_id = getCabinClass(infoFF, FIRST, IMTInfo.FULL, IMTInfo.FULL, info_id, award);
////                    }
//
//                    }

//                int i = 0;
//
//                for (String conItem : item.getConnections()) {
//
//                    ExtraData extraData = new ExtraData();
//
//                    extraData.setField_name("connection");
//                    extraData.setField_type("string");
//                    extraData.setField_value(conItem);
//                    extraData.setField_lvl("flight_list");
//                    extraData.setField_id("flight_list_" + i);
//
//                    award.getExtra_data().add(extraData);
//
//                    i++;
//                }
            if (award.getClassList().size() > 0) {

                awardList.add(award);
            }
        }

        System.out.println("EY handler. Flight size after processing= [" + awardList.size() + "]");

        return awardList;
    }

    private Integer getCabinClass(Info info, String fCabin, String classDescription, String currAward, int id, IMTAward award) {

        IMTInfo imti = new IMTInfo();

        if (info != null && info.getStatus() != Info.NA) {

            String[] taxArray = info.getTax().split(" ");

            imti.setMileage(info.getMileage());
            imti.setName(fCabin);
            imti.setStatus(info.getStringStatus());
            imti.setTax(taxArray[0]);
            imti.setCurrency(taxArray[1]);
            imti.setId(id);

            award.getClassList().add(imti);

            ExtraData extraData = new ExtraData();

            extraData.setFieldName("class_description");
            extraData.setFieldType("string");
            extraData.setFieldValue(classDescription);
            extraData.setFieldLvl("class_list");
            extraData.setFieldId("class_list_" + id);

            award.getExtraData().add(extraData);

            extraData = new ExtraData();

            extraData.setFieldName("award_description");
            extraData.setFieldType("string");
            extraData.setFieldValue(currAward);
            extraData.setFieldLvl("class_list");
            extraData.setFieldId("class_list_" + id);

            award.getExtraData().add(extraData);

            id++;
        }

        return id;
    }
}
