package ml.socshared.storage.exception;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@ApiModel(
        value = "AswErrors",
        description = "Enumeration of error codes")
@Getter
@RequiredArgsConstructor
public enum SocsharedErrors {

    @ApiModelProperty("Internal error")
    INTERNAL("Internal error"),

    @ApiModelProperty("Internal error")
    CLIENT_ALREADY_EXISTS("Client already exists"),

    @ApiModelProperty("Internal error")
    NOT_FOUND("Not found"),

    @ApiModelProperty("Internal error")
    UNAVAILABLE_REQUEST("Unavailable request"),

    @ApiModelProperty("Internal error")
    BAD_REQUEST("Bad request"),

    @ApiModelProperty("Internal error")
    BAD_GATEWAY("Bad gateway");

    private final String message;
}

