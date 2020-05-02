package ml.socshared.storage.integration;

import lombok.Getter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static ml.socshared.storage.config.Constants.TEST_PROFILE;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles(TEST_PROFILE)
public abstract class AbstractIT {

    @Getter
    @LocalServerPort
    private int localServerPort;
}

