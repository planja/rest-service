package com.guru.parser.utils;

import com.guru.parser.impl.qfparser.QFParser;
import parser.ac.ACParser;
import parser.af.AFParser;
import parser.ana.ANAParser;
import parser.as.ASParser;
import parser.ba.BAMParser;
import parser.ba.BAParser;
import parser.dl.DLParser;
import parser.ek.EKParser;
import parser.ey.EYParser;
import parser.jl.JLParser;
import parser.mm.MMParser;
import parser.qr.QRParser;
import parser.sq.SQParser;
import parser.ua.UAParser;
import parser.vs.VSParser;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Никита on 27.04.2016.
 */
public class ParserUtils {

    public static String getTotalTime(String totalTime, Object parser) throws ParseException {
        System.out.println(totalTime);
        String regexp = "";
        if(parser instanceof ACParser) {
            regexp = "((\\d*)[h]\\s)?(\\d*)[min]";
        } else if(parser instanceof AFParser) {
            regexp = "((\\d*)[h])?(\\d*)[m]";
        } else if(parser instanceof DLParser) {
            regexp = "((\\d*)[h]\\s)?(\\d*)[m]";
        } else if(parser instanceof EYParser) {
            regexp = "((\\d*)hr\\s)?(\\d*)min";
        } else if(parser instanceof JLParser) {
            regexp = "((\\d*)\\D+\\s?)?(\\d*)\\D+";
        } else if(parser instanceof SQParser) {
            regexp = "((\\d*)\\D+\\s?)?(\\d*)\\D+";
        } else if(parser instanceof MMParser) {
            regexp = "((\\d*)\\D+)?(\\d*)";
        } else if(parser instanceof QFParser) {
            regexp = "((\\d*)h\\s)?(\\d*)m";
        } else if(parser instanceof QRParser) {
            regexp = "((\\d*):)?(\\d*)\\s\\D+";
        } else if(parser instanceof VSParser) {
            regexp = "((\\d*)h\\D*)(\\d*)\\D+";
        } else if(parser instanceof UAParser) {
            regexp = "Travel Time:\\s((\\d*)\\D+\\s+)?(\\d*)\\D+";
        } else if(parser instanceof ASParser) {
            regexp = "((\\d*)\\D+\\s?)?(\\d*)\\D+";
        } else if(parser instanceof EKParser) {
            if(totalTime.contains("Duration")) {
                totalTime = totalTime.replaceAll("Duration", "").trim();
            }

            System.out.println("EK Parser");
            regexp = "((\\d*)\\D+\\s?)?(\\d*)\\D+";
        } else if(parser instanceof ANAParser) {
            regexp = "time\\s((\\d*)h)?(\\d*)min";
        } else if(parser instanceof BAParser) {
            regexp = "((\\d*)\\D+\\s?)?(\\d*)\\D+";
        } else if(parser instanceof BAMParser) {
            regexp = "((\\d*)\\D+\\s?)?(\\d*)\\D+";
        }

        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(totalTime);
        if(matcher.find()) {
            String hours = matcher.group(2) == null?"0":matcher.group(2);
            String minutes = matcher.group(3) != null && matcher.group(3).trim().length() != 0?matcher.group(3):"0";
            DecimalFormat acFormat = new DecimalFormat("##00");
            return acFormat.format((long)Integer.parseInt(hours)) + ":" + acFormat.format((long)Integer.parseInt(minutes));
        } else {
            return null;
        }
    }

}
