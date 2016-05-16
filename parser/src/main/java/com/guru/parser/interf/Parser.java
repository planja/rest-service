package com.guru.parser.interf;

import com.guru.domain.model.Trip;
import com.guru.vo.transfer.RequestData;

import java.util.Collection;

public interface Parser {

    Collection<Trip> parse(RequestData requestData) throws Exception;

    String ALL = "ALL";
    String ECONOMY = "E";
    String BUSINESS = "B";
    String FIRST = "F";
    String PREMIUM_ECONOMY = "P";
    String UPPER_CLASS = "B";
//    public static final String PREMIUM_ECONOMY = "P";


    String ECONOMY_BUSINESS = "MB";
    String BUSINESS_FIRST = "MF";


}
