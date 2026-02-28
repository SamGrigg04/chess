package service;

import dataaccess.DataAccessException;

public class NoGameException extends DataAccessException {
    public NoGameException(String message) {
        super(message);
    }
}
