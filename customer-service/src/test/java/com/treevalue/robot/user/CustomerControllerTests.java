package com.treevalue.robot.user;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class CustomerControllerTests {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{\"key1\": \"value1\", \"key2\": \"value2\"}";

        String url = "http://example.com/api";
        String getUrl = "http://example.com/api?param1=value1&param2=value2";

        String response = restTemplate.getForObject(getUrl, String.class);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        String postUrl = "http://example.com/api";
        String postResponse = restTemplate.exchange(postUrl, HttpMethod.POST, requestEntity, String.class).getBody();
    }
}
