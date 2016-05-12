package com.guru.domain.service.cost;

import com.guru.domain.model.Trip;
import com.guru.domain.model.TripCost;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Никита on 03.05.2016.
 */
public class CalculateCost {

    /*public static TripCost calculate(Trip trip, MileCost mileCost) {
        Integer miles = Integer.valueOf(trip.getClasInfo().stream().filter(o -> o.getReduction() == trip.getClas())
                .findFirst().get().getMileage());
        double parserCost = miles / 100 * mileCost.getCost().doubleValue();//ещё сложить таксы
        return new TripCost(trip, miles, BigDecimal.ONE, BigDecimal.valueOf(parserCost),
                BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
                BigDecimal.ONE, new Date(), new Date());

    }*/

    public static List<TripCost> calc(List<Trip> trips) {
        return trips.stream().map(trip -> new TripCost(trip, trip.getMiles(), BigDecimal.ONE, trip.getCost(),
                BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
                BigDecimal.ONE, new Date(), new Date())).collect(Collectors.toList());
    }

}
