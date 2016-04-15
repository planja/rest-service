package domain.service.converter;


import domain.model.Flight;
import vo.view.IMTFlight;

import java.text.ParseException;

/**
 * Created by Никита on 14.04.2016.
 */
public class IMTFlight2FlightConverter implements Converter<IMTFlight, Flight> {

    @Override
    public Flight convert(IMTFlight imtFlight) throws ParseException {
        return new Flight(Long.valueOf(imtFlight.getId()),
                null, null, "parser", "carrier code", imtFlight.getAirlineCompany(), "duration", imtFlight.getFlightCabin(),
                imtFlight.getDepartTime(), dateFormat.parse(imtFlight.getDepartDate()),
                imtFlight.getDepartPlace(), imtFlight.getDepartCode(),
                imtFlight.getArriveTime(), dateFormat.parse(imtFlight.getArriveDate()),
                imtFlight.getArrivePlace(), imtFlight.getArriveCode(), imtFlight.getFlightNumber(), "layover", imtFlight.getAircraft(),
                null, null);
    }
}
