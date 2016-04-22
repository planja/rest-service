package com.guru.domain.exception;

import javax.persistence.PersistenceException;

public class DomainException extends PersistenceException {

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }

    public DomainException(String message) {
        super(message);
    }

    public DomainException(Throwable cause) {
        super(cause);
    }
}
