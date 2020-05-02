package ml.socshared.storage.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "test", url = "http://test:8888")
public interface TestFeignClient {

    @GetMapping(value = "/api/v1/test", produces = MediaType.APPLICATION_JSON_VALUE)
    String test();

}
