package ml.socshared.storage.api.v1.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;

@Api(value = "Hello Controller")
public interface HelloApi {

    @ApiOperation(value = "print hello, world", notes = "print hello, world")
    HashMap<String, String> printHelloWorld();
}
