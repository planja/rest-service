package com.guru.domain.service.converter;

import com.guru.domain.model.Flight;
import com.guru.vo.view.IMTFlight;

import java.text.ParseException;

/**
 * Created by Никита on 14.04.2016.
 */
public class Flight2IMTFlightConverter implements Converter<Flight, IMTFlight> {
    @Override
    public IMTFlight convert(Flight flight) throws ParseException {
        return new IMTFlight(timeFormat.format(flight.getDepartTime()), dateFormat.format(flight.getDepartDate())
                , flight.getDepartPlace(), flight.getDepartCode(), timeFormat.format(flight.getArriveTime()),
                dateFormat.format(flight.getArriveDate()),
                flight.getArrivePlace(), flight.getArriveCode(), flight.getFlightDuration(), flight.getFlightNumber(),
                 /*???*/flight.getCarrierName(),
                flight.getAircraft(), "meal", flight.getCabin(), Integer.valueOf(flight.getId().toString()));
    }
}
