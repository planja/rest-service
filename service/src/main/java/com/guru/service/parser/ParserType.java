package com.guru.service.parser;


import com.guru.service.parser.impl.ParserAA;
import com.guru.service.parser.impl.ParserUA;
import com.guru.service.adaptor.impl.AdaptorServiceAA;
import com.guru.service.adaptor.interf.Adaptor;

public enum ParserType {
    AA {
        @Override
        public Adaptor getAdaptorService() {
            return new AdaptorServiceAA();
        }

        @Override
        public Class getParserClass() {
            return ParserAA.class;
        }
    },
    UA {
        @Override
        public Adaptor getAdaptorService() {
            return new AdaptorServiceAA();
        }

        @Override
        public Class getParserClass() {
            return ParserUA.class;
        }

    };

    public abstract Adaptor getAdaptorService();

    public abstract Class getParserClass();
}
