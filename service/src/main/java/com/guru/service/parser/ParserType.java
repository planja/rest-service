package com.guru.service.parser;


import com.guru.service.adaptor.impl.*;
import com.guru.service.adaptor.interf.Adaptor;
import com.guru.service.parser.impl.*;

public enum ParserType {
    AA {
        @Override
        public Adaptor getAdaptorService() {
            //return new AdaptorServiceAA();
            return null;
        }

        @Override
        public Class getParserClass() {
            return ParserAA.class;
        }
    }, AC {
        @Override
        public Adaptor getAdaptorService() {
          //  return new AdaptorServiceAC();
            return null;
        }

        @Override
        public Class getParserClass() {
            return ParserAC.class;
        }
    }, AF {
        @Override
        public Adaptor getAdaptorService() {
          //  return new AdaptorServiceAF();
            return null;
        }

        @Override
        public Class getParserClass() {
            return ParserAF.class;
        }
    },
    AS {
        @Override
        public Adaptor getAdaptorService() {
            //return new AdaptorServiceAS();
            return null;
        }

        @Override
        public Class getParserClass() {
            return ParserAS.class;
        }
    },
    BA {
        @Override
        public Adaptor getAdaptorService() {
            //return new AdaptorServiceBA();
            return null;
        }

        @Override
        public Class getParserClass() {
            return ParserBA.class;
        }
    },
    CX {
        @Override
        public Adaptor getAdaptorService() {
          //  return new AdaptorServiceCX();
            return null;
        }

        @Override
        public Class getParserClass() {
            return ParserCX.class;
        }
    },
    DL {
        @Override
        public Adaptor getAdaptorService() {
         //   return new AdaptorServiceDL();
            return null;
        }

        @Override
        public Class getParserClass() {
            return ParserDL.class;
        }
    },
    EK {
        @Override
        public Adaptor getAdaptorService() {
           // return new AdaptorServiceEK();
            return null;
        }

        @Override
        public Class getParserClass() {
            return ParserEK.class;
        }
    },
    EY {
        @Override
        public Adaptor getAdaptorService() {
         //   return new AdaptorServiceEY();
            return null;
        }

        @Override
        public Class getParserClass() {
            return ParserEY.class;
        }
    },
    JL {
        @Override
        public Adaptor getAdaptorService() {
          //  return new AdaptorServiceJL();
            return null;
        }

        @Override
        public Class getParserClass() {
            return ParserJL.class;
        }
    },
    LH {
        @Override
        public Adaptor getAdaptorService() {
       //     return new AdaptorServiceLH();
            return null;
        }

        @Override
        public Class getParserClass() {
            return ParserLH.class;
        }
    },
    NH {
        @Override
        public Adaptor getAdaptorService() {
         //   return new AdaptorServiceNH();
            return null;
        }

        @Override
        public Class getParserClass() {
            return ParserNH.class;
        }
    },
    QF {
        @Override
        public Adaptor getAdaptorService() {
          //  return new AdaptorServiceQF();
            return null;
        }

        @Override
        public Class getParserClass() {
            return ParserQF.class;
        }
    },
    QR {
        @Override
        public Adaptor getAdaptorService() {
            //return new AdaptorServiceQR();
            return null;
        }

        @Override
        public Class getParserClass() {
            return ParserQR.class;
        }
    },
    SQ {
        @Override
        public Adaptor getAdaptorService() {
           // return new AdaptorServiceSQ();
            return null;
        }

        @Override
        public Class getParserClass() {
            return ParserSQ.class;
        }
    },
    VS {
        @Override
        public Adaptor getAdaptorService() {
         //   return new AdaptorServiceVS();
            return null;
        }

        @Override
        public Class getParserClass() {
            return ParserVS.class;
        }
    },
    UA {
        @Override
        public Adaptor getAdaptorService() {
           // return new AdaptorServiceAA();
            return null;
        }

        @Override
        public Class getParserClass() {
            return ParserUA.class;
        }

    },
    KE {
        @Override
        public Adaptor getAdaptorService() {
            //return new AdaptorServiceAA();
            return null;
        }

        @Override
        public Class getParserClass() {
            return ParserKE.class;
        }

    };

    public abstract Adaptor getAdaptorService();

    public abstract Class getParserClass();
}
