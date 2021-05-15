package com.example.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class AdaptiveAuthService {
    @Autowired
    RestTemplate restTemplate;

    public String getUserRiskProfile() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String riskProfile = restTemplate.exchange("http://127.0.0.1:3000/", HttpMethod.GET, entity, String.class).getBody();
        return riskProfile;
    }
}
