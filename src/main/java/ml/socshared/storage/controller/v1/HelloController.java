package ml.socshared.storage.controller.v1;

import ml.socshared.storage.service.GroupService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import ml.socshared.storage.api.v1.rest.HelloApi;

import java.util.HashMap;

@RestController
@RequestMapping(value = "/api/v1")
public class HelloController implements HelloApi {

    private GroupService service;

    public HelloController(GroupService service) {
        this.service = service;
    }

    @Override
    @GetMapping(value = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, String> printHelloWorld() {

        return new HashMap<>() {
            {
                put("text", "Hello, World");
            }
        };
    }

    @GetMapping(value = "/feign", produces = MediaType.APPLICATION_JSON_VALUE)
    public String testFeign() {
        return service.test();
    }

}
