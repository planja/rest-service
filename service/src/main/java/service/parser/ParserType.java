package service.parser;


import service.parser.impl.ParserAA;
import service.parser.impl.ParserUA;
import service.adaptor.impl.AdaptorServiceAA;
import service.adaptor.interf.Adaptor;

public enum ParserType {
    AA {
        @Override
        public Adaptor getAdaptorService() {
            return new AdaptorServiceAA();
        }

        public Class getParserClass() {
            return ParserAA.class;
        }

    },
    UA {
        @Override
        public Adaptor getAdaptorService() {
            return new AdaptorServiceAA();
        }

        public Class getParserClass() {
            return ParserUA.class;
        }
    };

    public abstract Adaptor getAdaptorService();

    public abstract Class getParserClass();

    public String getParserName() {
        return "service/parser" + this.name();
    }
}
