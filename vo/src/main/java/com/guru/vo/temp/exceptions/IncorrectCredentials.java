package com.guru.vo.temp.exceptions;

/**
 * Created by Anton on 02.05.2016.
 */
public class IncorrectCredentials extends Exception {

    public IncorrectCredentials() {
    }

    public IncorrectCredentials(String message) {

        super(message);
    }
}
