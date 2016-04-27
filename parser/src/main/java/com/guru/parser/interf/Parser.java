package com.guru.parser.interf;

import com.guru.domain.model.Trip;
import com.guru.vo.transfer.RequestData;

import java.util.Collection;

public interface Parser {

    Collection<Trip> parse(RequestData requestData) throws Exception;
}
