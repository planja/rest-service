package service.actor.processingresult;

import service.actor.messanger.Messenger;
import akka.actor.UntypedActor;
import vo.transfer.ParserResult;

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
