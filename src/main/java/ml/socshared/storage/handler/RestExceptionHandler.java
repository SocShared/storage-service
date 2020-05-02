package ml.socshared.storage.handler;

import lombok.extern.slf4j.Slf4j;
import ml.socshared.storage.exception.SocsharedErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ml.socshared.storage.exception.AbstractRestHandleableException;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<RestApiError> buildErrorResponse(Throwable exc, HttpStatus httpStatus,
                                                            ServletWebRequest webRequest, SocsharedErrors errorCode) {
        return new ResponseEntity<>(new RestApiError(exc, httpStatus, webRequest, errorCode), httpStatus);
    }

    @ExceptionHandler(AbstractRestHandleableException.class)
    public ResponseEntity<RestApiError> handlePrintException(ServletWebRequest webRequest, AbstractRestHandleableException exc) {
        log.error(exc.getMessage());
        return buildErrorResponse(exc, exc.getHttpStatus(), webRequest, exc.getErrorCode());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<RestApiError> handlePrintException(ServletWebRequest webRequest, Throwable exc) {
        log.error(exc.getMessage());
        exc.printStackTrace();
        return buildErrorResponse(exc, HttpStatus.INTERNAL_SERVER_ERROR, webRequest, SocsharedErrors.INTERNAL);
    }
}

