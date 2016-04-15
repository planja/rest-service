package domain.service.converter;

import domain.model.ParserError;
import vo.view.IMTError;

import java.text.ParseException;

/**
 * Created by Никита on 14.04.2016.
 */
public class ParserError2IMTError implements Converter<ParserError, IMTError> {

    @Override
    public IMTError convert(ParserError parserError) throws ParseException {
        return new IMTError(/*????*/404, parserError.getErrorText());
    }
}
