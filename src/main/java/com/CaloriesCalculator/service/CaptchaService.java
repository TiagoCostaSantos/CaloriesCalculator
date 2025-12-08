package com.CaloriesCalculator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class CaptchaService {

    private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    @Value("${google.recaptcha.secret}")
    private String secret;

    public boolean isCaptchaValid(String response) {
        RestTemplate rest = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", secret);
        params.add("response", response);

        ResponseEntity<Map> verificationResponse =
                rest.postForEntity(VERIFY_URL, params, Map.class);

        return (Boolean) verificationResponse.getBody().get("success");
    }
}