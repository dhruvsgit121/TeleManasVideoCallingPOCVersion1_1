package com.example.ehrc.telemanas.GlobalRequestHandler;

//import com.twilio.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Service
public class VideoCallingAPIRequestHandler {

    @Value("${videocalling.api.eybase.url}")
    private String EYAPIBaseURl = "https://telemanas-test.iiitb.ac.in/v3/";

    @Autowired
    private GlobalRequestHandler globalRequestHandler;

    public ResponseEntity<Map<String, Object>> makePostRequest(String apiURlPath, String payload, String loggedInID, String jwtToken){

        if(globalRequestHandler == null){
            globalRequestHandler = new GlobalRequestHandler();
        }

        System.out.println("apiURlPath " + apiURlPath);

        String requestURL = EYAPIBaseURl + apiURlPath;

        System.out.println("GlobalRequestHandler URL is : " + requestURL);
        //"https://telemanas-test.iiitb.ac.in/v3/patients/get-patient-details";

//        String payload = "{\"telemanasId\":\"829858441784\",\"userUuid\":\"f9387d87-3667-4dfe-88c2-bd97ebdaaca4\"}";

//        String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJEdHR3RWktU3hIUHcyZThWNW9sdlZuZjBPVkVCdVJIc1lqNFFqY2VBeVdnIn0.eyJleHAiOjIwMzk0OTg0MjMsImlhdCI6MTcyNDEzODQyMywianRpIjoiNGRkODQyMGEtNWM3OS00YTNiLThiYjMtZGNjY2VlNWY2MGUyIiwiaXNzIjoiaHR0cHM6Ly90ZWxlbWFuYXMtaWFtLmlpaXRiLmFjLmluL2F1dGgvcmVhbG1zL2lpaXRiIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjExNTJkZTBiLTZhZmQtNDhhNS1iNDgwLTFjYzk5MTNjODI3ZCIsInR5cCI6IkJlYXJlciIsImF6cCI6ImlpaXRid2ViZmx1eCIsInNlc3Npb25fc3RhdGUiOiI5NDRiNDI2MC05NzhlLTRiZGUtYTE2ZC1mOTg4NGU1NDNkY2YiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6OTA5MCIsImh0dHA6Ly9sb2NhbGhvc3Q6NDIwMCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsImRlZmF1bHQtcm9sZXMtaWlpdGIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJzaWQiOiI5NDRiNDI2MC05NzhlLTRiZGUtYTE2ZC1mOTg4NGU1NDNkY2YiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJkZW1vLXVzZXIiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJkZW1vLXVzZXIiLCJnaXZlbl9uYW1lIjoiZGVtby11c2VyIn0.PXmQHFjtthinn3mEypD7s03NeSdfmepdlghPexeWyZSL56eUl17nsxIIBrUpl1jP8jX8Pae4v-BcnL1fx9-so5WZGkI2ZVUISLlrTcdkNJCt03tLm6XsCQTtmCF1hk3tmEPg0NM0PAC-QnDYKFDGliDgl1A06naUT1VJyC7K81LwHHRFjDn4ssJhh8M_nai3Ghssnr4o95e4W-Bvsg3FOFsKklFbcIAA4FflpPUTJBoTgrxbF2UR1LLpMu3D_CNiX-QSJihytx3H-3agLEov4GKrc0pLBPkgf9bVvzfxBWpOddnSbF4auj4G6UGH8oCxzAO_9BxzXFIt6FGrhJQOFg"; // Replace with your actual Bearer token

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
//        headers.set("loggedin", "70b497ac5c606a67b013ceb2d90eb45b");
        headers.set("loggedin", loggedInID);
        // Optionally set authorization or other headers
        headers.set("Authorization", "Bearer " + jwtToken);
        // Create the request entity
        HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);

        // Make the POST request
        ResponseEntity<Map<String, Object>> response = globalRequestHandler.makePostRequest(requestURL, requestEntity);
        //restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);

        System.out.println("Response from parent is : " + response);
        // Return the response

        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }






}
