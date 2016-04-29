package com.guru.service.parser.impl;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.guru.domain.model.Trip;
import com.guru.parser.impl.qfparser.QFParser;
import com.guru.service.actor.processingresult.ProcessingResultOfParserActor;
import com.guru.service.parser.interf.ParserActor;
import com.guru.vo.transfer.RequestData;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Никита on 18.04.2016.
 */
public class ParserQF extends UntypedActor implements ParserActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof RequestData) {
            RequestData requestData = (RequestData)message;
            SimpleDateFormat sdf_qr = new SimpleDateFormat("MM/dd/yyyy");

           /*
            QFParser qfParser = new QFParser();
            List dates = parser.test.Main.getDaysBetweenDates(sdf_qr.parse("12/10/2015"), sdf_qr.parse("12/15/2015"));
            List<Trip> trips = qfParser.getQantas(sdf_qr.parse("04/30/2016"), sdf_qr.parse("04/30/2016"), "LGW", "ADL", 1);
*/
            log.info("got it QF");
            List<Trip> flights =new QFParser().getQantas(sdf_qr.parse("04/30/2016"),
                    sdf_qr.parse("04/30/2016"), requestData.getOrigin(), requestData.getDestination(),
                    requestData.getSeats());
            ActorRef processingResultOfParserActor = context().system().actorOf(Props.create(ProcessingResultOfParserActor.class));
            processingResultOfParserActor.tell(flights, self());
        } else unhandled(message);
    }

    @Override
    public void postStop() {
        context().system().shutdown();
    }
}

