package ru.bezfy.ed_helper_api.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Repository
public class DeepGramRepository {
    @Value("${deepgram.url}")
    private String DEEPGRAMURL;
    @Value("${deepgram.key}")
    private String DEEPGRAMKEY;

    public String transcribeAudio(MultipartFile file) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Token " + DEEPGRAMKEY);
        headers.setContentType(MediaType.valueOf("audio/wav"));

        Resource audioResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        HttpEntity<Resource> requestEntity = new HttpEntity<>(audioResource, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                DEEPGRAMURL, HttpMethod.POST, requestEntity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<?, ?> responseBody = response.getBody();
            return (String) ((Map<?, ?>) ((Map<?, ?>) ((Map<?, ?>) responseBody.get("results")).get("channels")).get(0)).get("transcript");
        } else {
            throw new RuntimeException("Failed to transcribe audio: " + response.getStatusCode());
        }
    }

}
