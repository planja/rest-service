package com.guru.domain.service.converter;


import com.guru.domain.model.Flight;
import com.guru.domain.model.ParserError;
import com.guru.vo.view.IMTError;
import com.guru.vo.view.IMTFlight;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Никита on 13.04.2016.
 */
public class ConvertModel {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");


    public static IMTFlight convert2IMTFlight(Flight flight) {
        return new IMTFlight(timeFormat.format(flight.getDepartTime()), dateFormat.format(flight.getDepartDate())
                , flight.getDepartPlace(), flight.getDepartCode(), timeFormat.format(flight.getArriveTime()),
                dateFormat.format(flight.getArriveDate()),
                flight.getArrivePlace(), flight.getArriveCode(), flight.getFlightDuration(), flight.getFlightNumber(),
                 /*???*/flight.getCarrierName(),
                flight.getAircraft(), "meal", flight.getCabin(), Integer.valueOf(flight.getId().toString()));
    }

    public static IMTError convert2IMTError(ParserError error) {
        return new IMTError(/*????*/404, error.getErrorText());
    }

/*    public static IMTInfo convert2IMTInfo(Request request, Mile mile) {
        return new IMTInfo("name", String.valueOf(request.getStatus()), "mileage", String.valueOf(mile.getTax()),
                "currency", 1);
    }*/

    public static Flight convert2Flight(IMTFlight imtFlight) throws ParseException {
        return new Flight(Long.valueOf(imtFlight.getId()),
                null, null, "parser", "carrier code", imtFlight.getAirlineCompany(), "duration", imtFlight.getFlightCabin(),
                imtFlight.getDepartTime(), dateFormat.parse(imtFlight.getDepartDate()),
                imtFlight.getDepartPlace(), imtFlight.getDepartCode(),
                imtFlight.getArriveTime(), dateFormat.parse(imtFlight.getArriveDate()),
                imtFlight.getArrivePlace(), imtFlight.getArriveCode(), imtFlight.getFlightNumber(), "layover", imtFlight.getAircraft(),
                null, null);
    }

    public static ParserError convert2ParserError(IMTError imtError) {
        //return new ParserError(Long.valueOf("1"), null, null, imtError.getDescription());
        return null;
    }

}
