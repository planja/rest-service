package com.guru.service.adaptor.impl;

import com.guru.service.adaptor.interf.Adaptor;
import com.guru.vo.view.IMTAward;

import java.util.List;

/**
 * Created by Никита on 18.04.2016.
 */
public class AdaptorServiceAF implements Adaptor {

    @Override
    public List<IMTAward> adaptData(List<?> parserData, String... args) {
        System.out.println("");
        return null;
    }
}