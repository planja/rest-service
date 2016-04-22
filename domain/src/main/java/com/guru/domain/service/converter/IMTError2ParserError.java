package com.guru.domain.service.converter;

import com.guru.domain.model.ParserError;
import com.guru.vo.view.IMTError;

import java.text.ParseException;

/**
 * Created by Никита on 14.04.2016.
 */
public class IMTError2ParserError implements Converter<IMTError,ParserError> {

    @Override
    public ParserError convert(IMTError imtError) throws ParseException {
        //return new ParserError(Long.valueOf("1"), null, null, imtError.getDescription());
        return null;
    }
}
