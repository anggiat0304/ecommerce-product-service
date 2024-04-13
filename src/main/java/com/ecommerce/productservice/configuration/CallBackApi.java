package com.ecommerce.productservice.configuration;

import aj.org.objectweb.asm.TypeReference;
import com.ecommerce.productservice.generics.GenericResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class CallBackApi {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${krakend.ecommerce.url}")
    private String gatewayUrl;
    public boolean validateToken(String token, Long userId) throws Exception {
        try {
            // Set header 'Authorization' with the provided token
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            headers.set("User", String.valueOf(userId));

            // Create an HttpEntity with the headers
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            // Specify the URL for the API
            String apiUrl = gatewayUrl + "/auth/validateToken";

            // Send the request with the specified HttpMethod, requestEntity, and expected response type
            ResponseEntity<GenericResponse> responseEntity =
                    restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, GenericResponse.class);
            GenericResponse<Object> validateToken = responseEntity.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(validateToken);

            Map<String, Object> responseMap = objectMapper.readValue(jsonString, Map.class);

            Boolean success = (Boolean) responseMap.get("success");
            String message = (String) responseMap.get("message");

            if (!success){
                throw new Exception(message);
            }

            // Extract and return the body of the response
            return true;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
