package ml.socshared.storage.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ApiModel(
        value = "RestApiError",
        description = "An object that stores error information"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestApiError {
    private Integer status;
    private HttpStatus error;
    private String path;
    private String message;
    private LocalDateTime timestamp;

    public RestApiError(Throwable exc, HttpStatus status, ServletWebRequest webRequest) {
        this.status = status.value();
        this.error = status;
        this.message = exc.getMessage();
        this.timestamp = LocalDateTime.now();
        this.path = webRequest.getRequest().getRequestURI();
    }
}

