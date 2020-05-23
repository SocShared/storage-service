package ml.socshared.storage.exception.impl;

import ml.socshared.storage.exception.AbstractRestHandleableException;
import org.springframework.http.HttpStatus;

public class HttpUnavailableRequestException extends AbstractRestHandleableException {
    public HttpUnavailableRequestException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public HttpUnavailableRequestException(HttpStatus httpStatus) {
        super(httpStatus);
    }
}
