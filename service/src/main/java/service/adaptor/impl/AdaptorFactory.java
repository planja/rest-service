package service.adaptor.impl;

import service.adaptor.interf.Adaptor;
import service.parser.ParserType;

public class AdaptorFactory {
    public static Adaptor getAdaptor(ParserType parserType) {
        //ParserType parserType = ParserType.valueOf(type.toUpperCase());
        return parserType.getAdaptorService();
    }
}
