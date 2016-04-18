package com.guru.service.adaptor.impl;

import com.guru.service.RequestData;
import com.guru.service.adaptor.interf.Adaptor;
import com.guru.service.parser.ParserType;

public class AdaptorFactory {
    public static Adaptor getAdaptor(RequestData requestData) {
        ParserType parserType = ParserType.valueOf(requestData.getParser().toUpperCase());
        return parserType.getAdaptorService();
    }
}
