package com.guru.vo.temp;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Anton on 29.04.2016.
 */
public class Utils {
    public static String responseToString(InputStream inputStream) throws IOException {
        return IOUtils.toString(inputStream);
    }
}
