package com.votingbooth.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(properties = "services.vote.uri=http://localhost:8082")
class GatewayApplicationTests {

    // No issuer-uri in tests, so provide the decoder SecurityConfig needs.
    @MockitoBean
    private ReactiveJwtDecoder jwtDecoder;

    @Test
    void contextLoads() {
    }

}
