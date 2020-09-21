package com.space.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotValidDataException extends RuntimeException {

    public NotValidDataException() {
    }

    public NotValidDataException(String message) {
        super(message);
    }

    public NotValidDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotValidDataException(Throwable cause) {
        super(cause);
    }
}
