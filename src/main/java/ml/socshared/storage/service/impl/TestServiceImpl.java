package ml.socshared.storage.service.impl;

import ml.socshared.storage.client.TestFeignClient;
import ml.socshared.storage.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

    private TestFeignClient client;

    @Autowired
    public TestServiceImpl(TestFeignClient feignClient) {
        this.client = feignClient;
    }

    @Override
    public String test() {
        return client.test();
    }
}
