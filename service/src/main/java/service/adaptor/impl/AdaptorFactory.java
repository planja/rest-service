package service.adaptor.impl;

import parser.ParserType;
import service.adaptor.interf.Adaptor;

public class AdaptorFactory {
    public static Adaptor getAdaptor(ParserType parserType) {
        //ParserType parserType = ParserType.valueOf(type.toUpperCase());
        return parserType.getAdaptorService();
    }
}
