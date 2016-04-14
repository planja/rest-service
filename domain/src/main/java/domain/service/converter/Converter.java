package domain.service.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Никита on 14.04.2016.
 */
public interface Converter<S,T> {
    T convert(S s)throws ParseException;
     SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
     SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
}
