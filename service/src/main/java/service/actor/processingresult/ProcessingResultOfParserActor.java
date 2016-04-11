package service.actor.processingresult;

import service.actor.messanger.Messenger;
import akka.actor.UntypedActor;
import domain.ParserResult;

public class ProcessingResultOfParserActor extends UntypedActor {

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof ParserResult) {
            System.out.println("Processing result of parser");
            Messenger.sendMessage();
            getContext().system().shutdown();

        } else
            unhandled(msg);
    }
}

// send отправка в другое приложение. запись в базу данных. другому актору который запишет в базу
