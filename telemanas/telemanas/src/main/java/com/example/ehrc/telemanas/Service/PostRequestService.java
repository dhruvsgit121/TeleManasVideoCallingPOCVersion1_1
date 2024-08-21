package com.example.ehrc.telemanas.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

//import java.net.http.HttpHeaders;
import java.util.Map;

@Service
public class PostRequestService {

    @Autowired
//    @Autowired
    private RestTemplate restTemplate;


//    @PostMapping("/send-data")
    public ResponseEntity<Map<String, Object>> sendData() {
        String url = "https://telemanas-test.iiitb.ac.in/v3/patients/get-patient-details";

//        {
//            "telemanasId": "829858441784",
//                "userUuid": "f9387d87-3667-4dfe-88c2-bd97ebdaaca4"
//        }

        // Create the request payload
//        String payload = "{ \"key\": \"value\" }";
        String payload = "{\"telemanasId\":\"829858441784\",\"userUuid\":\"f9387d87-3667-4dfe-88c2-bd97ebdaaca4\"}";

        String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJEdHR3RWktU3hIUHcyZThWNW9sdlZuZjBPVkVCdVJIc1lqNFFqY2VBeVdnIn0.eyJleHAiOjIwMzk0OTg0MjMsImlhdCI6MTcyNDEzODQyMywianRpIjoiNGRkODQyMGEtNWM3OS00YTNiLThiYjMtZGNjY2VlNWY2MGUyIiwiaXNzIjoiaHR0cHM6Ly90ZWxlbWFuYXMtaWFtLmlpaXRiLmFjLmluL2F1dGgvcmVhbG1zL2lpaXRiIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjExNTJkZTBiLTZhZmQtNDhhNS1iNDgwLTFjYzk5MTNjODI3ZCIsInR5cCI6IkJlYXJlciIsImF6cCI6ImlpaXRid2ViZmx1eCIsInNlc3Npb25fc3RhdGUiOiI5NDRiNDI2MC05NzhlLTRiZGUtYTE2ZC1mOTg4NGU1NDNkY2YiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6OTA5MCIsImh0dHA6Ly9sb2NhbGhvc3Q6NDIwMCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsImRlZmF1bHQtcm9sZXMtaWlpdGIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJzaWQiOiI5NDRiNDI2MC05NzhlLTRiZGUtYTE2ZC1mOTg4NGU1NDNkY2YiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJkZW1vLXVzZXIiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJkZW1vLXVzZXIiLCJnaXZlbl9uYW1lIjoiZGVtby11c2VyIn0.PXmQHFjtthinn3mEypD7s03NeSdfmepdlghPexeWyZSL56eUl17nsxIIBrUpl1jP8jX8Pae4v-BcnL1fx9-so5WZGkI2ZVUISLlrTcdkNJCt03tLm6XsCQTtmCF1hk3tmEPg0NM0PAC-QnDYKFDGliDgl1A06naUT1VJyC7K81LwHHRFjDn4ssJhh8M_nai3Ghssnr4o95e4W-Bvsg3FOFsKklFbcIAA4FflpPUTJBoTgrxbF2UR1LLpMu3D_CNiX-QSJihytx3H-3agLEov4GKrc0pLBPkgf9bVvzfxBWpOddnSbF4auj4G6UGH8oCxzAO_9BxzXFIt6FGrhJQOFg"; // Replace with your actual Bearer token


        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("loggedin", "70b497ac5c606a67b013ceb2d90eb45b");
        // Optionally set authorization or other headers
        headers.set("Authorization", "Bearer " + token);

        // Create the request entity
        HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);


        // Make the POST request
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);


        System.out.println("REspomsnse from parent is : " + response);
        // Return the response
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

//    public String triggerPost() {
//        String url = "https://telemanas-test.iiitb.ac.in/v3/patients/get-patient-details";
//
//        // Create the WebClient instance
////        WebClient webClient = webClientBuilder.build();
////
////        String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJEdHR3RWktU3hIUHcyZThWNW9sdlZuZjBPVkVCdVJIc1lqNFFqY2VBeVdnIn0.eyJleHAiOjIwMzk0OTg0MjMsImlhdCI6MTcyNDEzODQyMywianRpIjoiNGRkODQyMGEtNWM3OS00YTNiLThiYjMtZGNjY2VlNWY2MGUyIiwiaXNzIjoiaHR0cHM6Ly90ZWxlbWFuYXMtaWFtLmlpaXRiLmFjLmluL2F1dGgvcmVhbG1zL2lpaXRiIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjExNTJkZTBiLTZhZmQtNDhhNS1iNDgwLTFjYzk5MTNjODI3ZCIsInR5cCI6IkJlYXJlciIsImF6cCI6ImlpaXRid2ViZmx1eCIsInNlc3Npb25fc3RhdGUiOiI5NDRiNDI2MC05NzhlLTRiZGUtYTE2ZC1mOTg4NGU1NDNkY2YiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6OTA5MCIsImh0dHA6Ly9sb2NhbGhvc3Q6NDIwMCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsImRlZmF1bHQtcm9sZXMtaWlpdGIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJzaWQiOiI5NDRiNDI2MC05NzhlLTRiZGUtYTE2ZC1mOTg4NGU1NDNkY2YiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJkZW1vLXVzZXIiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJkZW1vLXVzZXIiLCJnaXZlbl9uYW1lIjoiZGVtby11c2VyIn0.PXmQHFjtthinn3mEypD7s03NeSdfmepdlghPexeWyZSL56eUl17nsxIIBrUpl1jP8jX8Pae4v-BcnL1fx9-so5WZGkI2ZVUISLlrTcdkNJCt03tLm6XsCQTtmCF1hk3tmEPg0NM0PAC-QnDYKFDGliDgl1A06naUT1VJyC7K81LwHHRFjDn4ssJhh8M_nai3Ghssnr4o95e4W-Bvsg3FOFsKklFbcIAA4FflpPUTJBoTgrxbF2UR1LLpMu3D_CNiX-QSJihytx3H-3agLEov4GKrc0pLBPkgf9bVvzfxBWpOddnSbF4auj4G6UGH8oCxzAO_9BxzXFIt6FGrhJQOFg"; // Replace with your actual Bearer token
////
////
////        // Create the request payload
////        String payload = "{\"telemanasId\":\"829858441784\",\"userUuid\":\"f9387d87-3667-4dfe-88c2-bd97ebdaaca4\"}";
////
////        Mono<Map> response = webClient.post()
////                .uri(url)
////                .header("Authorization", "Bearer " + token)// Set the Bearer token in the header
////                .header("loggedin", "70b497ac5c606a67b013ceb2d90eb45b")
////                .contentType(MediaType.APPLICATION_JSON)
////                .bodyValue(payload)
////                .retrieve()
////                .bodyToMono(Map.class);
//
////        System.out.println("REsponse data is : " + response);
//
//        // Make the POST request
//        return url;
//    }
}
