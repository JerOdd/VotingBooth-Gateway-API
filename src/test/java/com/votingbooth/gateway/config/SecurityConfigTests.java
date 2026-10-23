package com.votingbooth.gateway.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

// The route forwards inside the gateway, so a request that gets past security ends in 404 instead of 401.
@SpringBootTest(properties = "services.mobile-bff.uri=forward:/")
@AutoConfigureWebTestClient
class SecurityConfigTests {

    @MockitoBean
    private ReactiveJwtDecoder jwtDecoder;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void lawsArePublic() {
        assertThat(status("/mobile/v1/laws")).isNotEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void representativesArePublic() {
        assertThat(status("/mobile/v1/representatives/123")).isNotEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void lawVotesArePublic() {
        assertThat(status("/mobile/v1/votes/f69f33df-111c-4975-8508-1a73cfa399d8")).isNotEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void votesListNeedsToken() {
        assertThat(status("/mobile/v1/votes")).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void votingNeedsToken() {
        HttpStatus status = HttpStatus.valueOf(webTestClient.post().uri("/mobile/v1/votes")
                .exchange().returnResult(Void.class).getStatus().value());
        assertThat(status).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void lawWritesNeedToken() {
        HttpStatus status = HttpStatus.valueOf(webTestClient.post().uri("/mobile/v1/laws")
                .exchange().returnResult(Void.class).getStatus().value());
        assertThat(status).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    private HttpStatus status(String path) {
        return HttpStatus.valueOf(webTestClient.get().uri(path)
                .exchange().returnResult(Void.class).getStatus().value());
    }
}
