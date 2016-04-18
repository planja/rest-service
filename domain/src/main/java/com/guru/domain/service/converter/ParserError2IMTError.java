package com.guru.domain.service.converter;

import com.guru.domain.model.ParserError;
import com.guru.vo.view.IMTError;

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
