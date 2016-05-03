package com.guru.vo.temp;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import com.guru.parser.dl.DLParser;

/**
 * Created by Anton on 29.04.2016.
 */
public class Utils {
    public static String responseToString(InputStream inputStream) throws IOException {
        return IOUtils.toString(inputStream);
    }

    public static String gzipResponseToString(InputStream inputStream) {

        StringBuffer buffer = new StringBuffer();

        try {

            GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);

            DataInputStream dataInputStream = new DataInputStream(gzipInputStream);

            InputStreamReader inputStreamReader = new InputStreamReader(dataInputStream);

            BufferedReader buff = new BufferedReader(inputStreamReader);

            String line;

            do {

                line = buff.readLine();

                if (line != null) {

                    buffer.append(line);
                }

            } while (line != null);

            inputStreamReader.close();
            inputStream.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return buffer.toString();
    }

    public static String getTotalTime(String totalTime, Object parser) throws ParseException {
        String regexp = "";
        if (parser instanceof DLParser) {

            regexp = "((\\d*)[h]\\s)?(\\d*)[m]";
//            regexp = "((\\d*)\\D+\\s?)?(\\d*)\\D+";

        }
        Pattern pattern = Pattern.compile(regexp);

        Matcher matcher = pattern.matcher(totalTime);

        if (matcher.find()) {

            String hours = matcher.group(2) == null ? "0" : matcher.group(2);
            String minutes = matcher.group(3) == null || matcher.group(3).trim().length() == 0 ? "0" : matcher.group(3);

            DecimalFormat acFormat = new DecimalFormat("##00");
            //         System.out.println(acFormat.format(Integer.parseInt(hours)) + ":" + acFormat.format(Integer.parseInt(minutes)));
            return acFormat.format(Integer.parseInt(hours)) + ":" + acFormat.format(Integer.parseInt(minutes));
        }

        return null;
    }

    public static int randInt(int max) {

        return (int) (Math.random() * max);
    }

}
