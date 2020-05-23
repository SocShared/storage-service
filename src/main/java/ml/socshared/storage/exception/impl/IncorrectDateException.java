package ml.socshared.storage.exception.impl;

import org.springframework.http.HttpStatus;

public class IncorrectDateException extends HttpBadRequestException {

    public IncorrectDateException() {
        super();
    }

    public IncorrectDateException(HttpStatus httpStatus) {
        super(httpStatus);
    }

    public IncorrectDateException(String message) {
        super(message);
    }

}
