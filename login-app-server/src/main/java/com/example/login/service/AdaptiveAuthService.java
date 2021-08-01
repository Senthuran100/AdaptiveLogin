package com.example.login.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdaptiveAuthService {

    Logger logger = LoggerFactory.getLogger(AdaptiveAuthService.class);

    @Autowired
    RestTemplate restTemplate;

    public String getUserRiskProfile() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String riskProfilePercentage = restTemplate.exchange("http://127.0.0.1:4000/", HttpMethod.GET, entity, String.class).getBody();
        return riskProfilePercentage;
    }

    public Double postUserDynamics(String mouseDynamics, String keyboardDynamics, String usernameOREmail) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        Map<String, String> map = new HashMap<>();
        map.put("mouseEvent", mouseDynamics);
        map.put("keyboardEvent", keyboardDynamics);
        map.put("usernameOREmail", usernameOREmail);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(map, headers);

        String riskProfileProbability = restTemplate.exchange("http://127.0.0.1:4000/userDynamics", HttpMethod.POST, entity, String.class).getBody();
        logger.info("=== userDynamics risk probability ===" + riskProfileProbability);
        Double userDynamicsProbability = Double.parseDouble(riskProfileProbability) * 100;
        return userDynamicsProbability;
    }

    public Double postUserBrowserInfo(String browser, String usernameOREmail) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        Map<String, String> map = new HashMap<>();
        map.put("browserInfo", browser);
        map.put("usernameOREmail", usernameOREmail);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(map, headers);

        String riskProfileProbability = restTemplate.exchange("http://127.0.0.1:4000/browserInfo", HttpMethod.POST, entity, String.class).getBody();
        logger.info("=== browser info risk probability ===" + riskProfileProbability);
        Double userBrowserProbability = Double.parseDouble(riskProfileProbability) * 100;
        return userBrowserProbability;
    }


}
