package ml.socshared.storage.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class AbstractRestHandleableException extends RuntimeException implements HttpStatusCodeContainer {
    private static final long serialVersionUID = -3416823984750319182L;

    private final HttpStatus httpStatus;

    public AbstractRestHandleableException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public AbstractRestHandleableException(HttpStatus httpStatus) {
        this("", httpStatus);
    }

    public AbstractRestHandleableException(Throwable throwable, HttpStatus httpStatus) {
        super(throwable);
        this.httpStatus = httpStatus;
    }
}
