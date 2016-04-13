package service.adaptor.interf;

import parser.PreparedData;
import service.RequestData;
import vo.view.IMTAward;

import java.util.List;

public interface Adaptor {
    List<IMTAward> adaptData(List<?> parserData);
}
