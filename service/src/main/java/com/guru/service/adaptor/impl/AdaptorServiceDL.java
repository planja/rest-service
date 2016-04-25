package service.adaptor.impl;

import parser.model.Award;
import parser.model.Flight;
import parser.model.Info;
import parser.model.Stop;
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
public class AdaptorServiceDL implements Adaptor {
    @Override
    public List<IMTAward> adaptData(List<?> parserData, String... args) {
        String flightClass = args[0];
        String seats = args[1];
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat date_format_res = new SimpleDateFormat("yyyy:MM:dd");
        SimpleDateFormat time_format_pac = new SimpleDateFormat("EEE MMM dd yyyy h:mma", Locale.US);
        SimpleDateFormat time_format_res = new SimpleDateFormat("HH:mm");

        SimpleDateFormat time_format = new SimpleDateFormat("yyyy:MM:dd HH:mm");
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
                    imtf.setArrivePlace(dm.getCityByCode(flight.getArriveAirport().trim()));
                    imtf.setArriveCode(flight.getArriveAirport());

                    Calendar c = new GregorianCalendar();
                    //проверить
                    c.setTime(sdf.parse(flight.getDepartDate()));

                    Date arrTime = time_format_pac.parse(flight.getArriveDate() + " " + c.get(Calendar.YEAR) + " " + flight.getArriveTime());

                    imtf.setArriveDate(date_format_res.format(arrTime));
                    imtf.setArriveTime(time_format_res.format(arrTime));

                    imtf.setDepartPlace(dm.getCityByCode(flight.getDepartAirport().trim()));
                    imtf.setDepartCode(flight.getDepartAirport());

                    Date depTime = time_format_pac.parse(flight.getDepartDate() + " " + c.get(Calendar.YEAR) + " " + flight.getDepartTime());

                    imtf.setDepartDate(date_format_res.format(depTime));
                    imtf.setDepartTime(time_format_res.format(depTime));
                    imtf.setFlightNumber(flight.getFlight());
                    imtf.setAirlineCompany(Utils.getAirCompany(flight.getFlight()));
                    imtf.setMeal(flight.getMeal());
                    imtf.setTravelTime(flight.getTravelTime());
                    imtf.setId(flight_id);

                    Date depDate = time_format.parse(imtf.getDepartDate() + " " + imtf.getDepartTime());

                    imtf.setLayoverTime(Utils.getHoursBetweenDays(previousDate, depDate));
                    previousDate = arrTime;

                    award.getFlightList().add(imtf);

                    flight_id++;
                } catch (ParseException e) {
                } catch (SQLException e) {
                } catch (Exception e) {
                }
            }

            int fId = 0;

            for (Flight flight : item.getFlights()) {

                for (Stop stop : flight.getHiddenStopList()) {

                    ExtraData extraData = new ExtraData();

                    extraData.setFieldName("hidden_stop");
                    extraData.setFieldType("string");
                    extraData.setFieldValue("(" + stop.getAirport() + ")" + " " + stop.getDuration());
                    extraData.setFieldLvl("flight_list");
                    extraData.setFieldId("flight_list_" + fId);

                    award.getExtraData().add(extraData);
                }

                fId++;
            }

            award.setTotalDuration(item.getTotalDuration());

            for (String cabinItem : Utils.getCabins(flightClass)) {
                try {
                    if (cabinItem.equals(ECONOMY)) {

                        Info info = item.getEconomy();

                        if (info != null) {

                            info_id = getCabinClass(info, ECONOMY, info_id, award, item.getFlights());
                        }

                    } else if (cabinItem.equals(BUSINESS)) {

                        Info info = item.getBusiness();

                        if (info != null) {

                            info_id = getCabinClass(info, BUSINESS, info_id, award, item.getFlights());
                        }

                    } else if (cabinItem.equals(FIRST)) {

                        Info info = item.getFirst();

                        if (info != null) {

                            info_id = getCabinClass(info, FIRST, info_id, award, item.getFlights());
                        }

                    }
                } catch (SQLException e) {
                }
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

            if (award.getClassList().size() > 0) {

                awardList.add(award);
            }
        }

        System.out.println("DL handler. Flight size after processing= [" + awardList.size() + "]");

        List<IMTAward> resultList = new LinkedList<>();

        for (IMTAward award : awardList) {

            if (award.isSplit()) {

                int i = 0;

                for (IMTInfo info : award.getClassList()) {

                    IMTAward imta = new IMTAward();

                    imta.getClassList().add(info);

                    for (ExtraData ed : award.getExtraData()) {

                        if (ed.getFieldId().contains("class_list_" + i)) {

                            ed.setFieldId("class_list_0");

                            imta.getExtraData().add(ed);

                        } else if (!ed.getFieldId().contains("class_list_")) {

                            imta.getExtraData().add(ed);
                        }
                    }

                    imta.getFlightList().addAll(award.getFlightList());
                    imta.setTotalDuration(award.getTotalDuration());
                    info.setId(0);
                    resultList.add(imta);

                    i++;
                }

            } else {

                resultList.add(award);
            }
        }

        return resultList;

    }

    private Integer getCabinClass(Info info, String fCabin, int id, IMTAward award, List<Flight> fList) throws SQLException {

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

            imti.setMileage(info.getMileage().replaceAll(" ", ""));
            imti.setStatus(info.getStringStatus());
            imti.setTax(info.getTax().replaceAll(",", ""));
            imti.setCurrency(info.getCurrency());
            imti.setId(id);

            int flight_id = 0;

            String currAward = IMTInfo.SAVER;

            for (String awardName : info.getAwardNames()) {

                currAward = Utils.getAward(awardName, currAward);

                ExtraData extraData = new ExtraData();

                extraData.setFieldName("class_description");
                extraData.setFieldType("string");
                extraData.setFieldValue(awardName);
                extraData.setFieldLvl("class_list");
                extraData.setFieldId("class_list_" + id);
                extraData.setFieldSubLvl("flight_list_" + flight_id);

                award.getExtraData().add(extraData);

                flight_id++;
            }

            ExtraData extraData = new ExtraData();

            extraData.setFieldName("award_description");
            extraData.setFieldType("string");
            extraData.setFieldValue(currAward);
            extraData.setFieldLvl("class_list");
            extraData.setFieldId("class_list_" + id);

            award.getExtraData().add(extraData);

            String currCabin = fCabin;

            if (info.isMixed()) {

                award.setSplit(true);

                flight_id = 0;

                for (String mixedCabins : info.getMixedCabins()) {

                    mixedCabins = Utils.getMixedCabinClass(mixedCabins);

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

                    extraData = new ExtraData();

                    extraData.setFieldName("mixed_cabins");
                    extraData.setFieldType("string");
                    extraData.setFieldValue(mixedCabins);
                    extraData.setFieldLvl("class_list");
                    extraData.setFieldId("class_list_" + id);
                    extraData.setFieldSubLvl("flight_list_" + flight_id);

                    award.getExtraData().add(extraData);

                    flight_id++;
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
}