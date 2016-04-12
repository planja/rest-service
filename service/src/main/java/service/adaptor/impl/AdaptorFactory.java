package service.adaptor.impl;

import parser.ParserType;
import service.adaptor.interf.Adaptor;

public class AdaptorFactory {
    public static Adaptor getAdaptor(String type) {
        ParserType parserType = ParserType.valueOf(type.toUpperCase());
        return parserType.getAdaptorService();
    }
}
