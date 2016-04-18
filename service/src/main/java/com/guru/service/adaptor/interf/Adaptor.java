package com.guru.service.adaptor.interf;

import com.guru.vo.view.IMTAward;

import java.util.List;

public interface Adaptor {
    List<IMTAward> adaptData(List<?> parserData, String... args);
}
