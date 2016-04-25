package com.guru.service.adaptor.impl;

import com.guru.service.adaptor.interf.Adaptor;
import com.guru.service.parser.ParserType;

public class AdaptorFactory {
    public static Adaptor getAdaptor(String parserName) {
        ParserType parserType = ParserType.valueOf(parserName.toUpperCase());
        return parserType.getAdaptorService();
    }
}
