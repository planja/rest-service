package com.guru.service.parser;


import com.guru.service.adaptor.impl.*;
import com.guru.service.adaptor.interf.Adaptor;
import com.guru.service.parser.impl.*;

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
    }, AC {
        @Override
        public Adaptor getAdaptorService() {
            return new AdaptorServiceAC();
        }

        @Override
        public Class getParserClass() {
            return ParserAC.class;
        }
    }, AF {
        @Override
        public Adaptor getAdaptorService() {
            return new AdaptorServiceAF();
        }

        @Override
        public Class getParserClass() {
            return ParserAF.class;
        }
    },
    AS {
        @Override
        public Adaptor getAdaptorService() {
            return new AdaptorServiceAS();
        }

        @Override
        public Class getParserClass() {
            return ParserAS.class;
        }
    },
    BA {
        @Override
        public Adaptor getAdaptorService() {
            return new AdaptorServiceBA();
        }

        @Override
        public Class getParserClass() {
            return ParserBA.class;
        }
    },
    CX {
        @Override
        public Adaptor getAdaptorService() {
            return new AdaptorServiceCX();
        }

        @Override
        public Class getParserClass() {
            return ParserCX.class;
        }
    },
    DL {
        @Override
        public Adaptor getAdaptorService() {
            return new AdaptorServiceDL();
        }

        @Override
        public Class getParserClass() {
            return ParserDL.class;
        }
    },
    EK {
        @Override
        public Adaptor getAdaptorService() {
            return new AdaptorServiceEK();
        }

        @Override
        public Class getParserClass() {
            return ParserEK.class;
        }
    },
    EY {
        @Override
        public Adaptor getAdaptorService() {
            return new AdaptorServiceEY();
        }

        @Override
        public Class getParserClass() {
            return ParserEY.class;
        }
    },
    JL {
        @Override
        public Adaptor getAdaptorService() {
            return new AdaptorServiceJL();
        }

        @Override
        public Class getParserClass() {
            return ParserJL.class;
        }
    },
    LH {
        @Override
        public Adaptor getAdaptorService() {
            return new AdaptorServiceLH();
        }

        @Override
        public Class getParserClass() {
            return ParserLH.class;
        }
    },
    NH {
        @Override
        public Adaptor getAdaptorService() {
            return new AdaptorServiceNH();
        }

        @Override
        public Class getParserClass() {
            return ParserNH.class;
        }
    },
    QF {
        @Override
        public Adaptor getAdaptorService() {
            return new AdaptorServiceQF();
        }

        @Override
        public Class getParserClass() {
            return ParserQF.class;
        }
    },
    QR {
        @Override
        public Adaptor getAdaptorService() {
            return new AdaptorServiceQR();
        }

        @Override
        public Class getParserClass() {
            return ParserQR.class;
        }
    },
    SQ {
        @Override
        public Adaptor getAdaptorService() {
            return new AdaptorServiceSQ();
        }

        @Override
        public Class getParserClass() {
            return ParserSQ.class;
        }
    },
    VS {
        @Override
        public Adaptor getAdaptorService() {
            return new AdaptorServiceVS();
        }

        @Override
        public Class getParserClass() {
            return ParserVS.class;
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
