package com.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class FileController {

    @Value("${flask.url}")
    private String flaskUrl;

    private final RestTemplate restTemplate;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        return forwardToFlask(file, "/upload");
    }

    @PostMapping("/upload_augmented")
    public ResponseEntity<?> uploadAugmented(@RequestParam("file") MultipartFile file) {
        return forwardToFlask(file, "/upload");
    }

    private ResponseEntity<?> forwardToFlask(MultipartFile file, String flaskRoute) {
        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response =
                    restTemplate.postForEntity(flaskUrl + flaskRoute, request, String.class);

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error forwarding to Flask: " + e.getMessage());
        }
    }
}
