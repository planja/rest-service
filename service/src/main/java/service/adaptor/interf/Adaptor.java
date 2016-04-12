package service.adaptor.interf;

import parser.PreparedData;
import service.RequestData;

public interface Adaptor {
    PreparedData adaptData(RequestData requestData);
}
