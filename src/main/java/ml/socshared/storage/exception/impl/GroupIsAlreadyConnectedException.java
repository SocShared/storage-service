package ml.socshared.storage.exception.impl;

import ml.socshared.storage.exception.AbstractRestHandleableException;
import org.springframework.http.HttpStatus;

public class GroupIsAlreadyConnectedException extends HttpBadRequestException {

    public GroupIsAlreadyConnectedException() {
        super();
    }

    public GroupIsAlreadyConnectedException(HttpStatus httpStatus) {
        super(httpStatus);
    }

    public GroupIsAlreadyConnectedException(String message) {
        super(message);
    }

}
