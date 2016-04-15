package service.adaptor.interf;

import service.parser.PreparedData;
import vo.view.IMTAward;

import java.util.List;

public interface Adaptor {
    List<IMTAward> adaptData(List<?> parserData, String... args);
}
