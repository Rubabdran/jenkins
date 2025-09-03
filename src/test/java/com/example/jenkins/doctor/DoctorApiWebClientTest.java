package com.example.jenkins.doctor;

import com.example.jenkins.JenkinsApplication;
import com.example.jenkins.doctor.dto.DoctorRequest;
import com.example.jenkins.doctor.dto.DoctorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = JenkinsApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class DoctorApiWebClientTest {

    @LocalServerPort
    int port;

    WebClient client;

    @BeforeEach
    void setUp() {
        this.client = WebClient.builder()
                .baseUrl("http://localhost:" + port + "/api/doctors")
                .build();
    }

    @Test
    @DisplayName("HTTP CRUD + search flow with WebClient")
    void httpCrudSearchFlow() {
        DoctorRequest createReq = new DoctorRequest(
                "Dr. Ruba", "Cardiology", "King Fahad Medical City", 5
        );

        DoctorResponse created = client.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createReq)
                .retrieve()
                .bodyToMono(DoctorResponse.class)
                .block();

        assertNotNull(created);
        Long id = created.id();

        DoctorResponse got = client.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(DoctorResponse.class)
                .block();
        assertNotNull(got);
        assertEquals("Cardiology", got.specialty());

        DoctorResponse[] byName = client.get()
                .uri(uri -> uri.queryParam("q", "ruba").build())
                .retrieve()
                .bodyToMono(DoctorResponse[].class)
                .block();
        assertTrue(java.util.Arrays.stream(byName).anyMatch(d -> d.id().equals(id)));

        DoctorRequest patchReq = new DoctorRequest(null, null, null, 4);
        DoctorResponse updated = client.patch()
                .uri("/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(patchReq)
                .retrieve()
                .bodyToMono(DoctorResponse.class)
                .block();
        assertEquals(4, updated.rating());

        client.delete()
                .uri("/{id}", id)
                .retrieve()
                .toBodilessEntity()
                .block();

        DoctorResponse[] all = client.get()
                .retrieve()
                .bodyToMono(DoctorResponse[].class)
                .block();
        boolean exists = java.util.Arrays.stream(all).anyMatch(d -> d.id().equals(id));
        assertFalse(exists);
    }
}
