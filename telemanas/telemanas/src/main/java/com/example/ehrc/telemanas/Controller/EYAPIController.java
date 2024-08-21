package com.example.ehrc.telemanas.Controller;

import com.example.ehrc.telemanas.AuthenticateService.AuthenticateUserFactory;
import com.example.ehrc.telemanas.DTO.AuthenticateUserDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestHeader;


import java.util.Map;

@RestController
@RequestMapping("/eyi")
@Validated

public class EYAPIController {

    @Autowired
    private AuthenticateUserFactory authenticateUserFactory;

    @RequestMapping("/gethelloworld")
    public ResponseEntity<Map<String, Object>> getHelloWorld(@Valid @RequestBody AuthenticateUserDTO userDTOData,
                                             @RequestHeader(value = "BearerToken") String bearerToken,
                                             @RequestHeader(value = "loggedin") String loggedIn
                                             ) {

        userDTOData.setBearerToken(bearerToken);
        userDTOData.setLoggedInId(loggedIn);

        System.out.println("User data to be sent to api is : " + userDTOData );

        //Checking for Authentication of Patient Data...
        ResponseEntity<Map<String, Object>> response = authenticateUserFactory.authenticateUser("patient", userDTOData);
        if(response.getStatusCode() != HttpStatus.OK)
            return response;

        //Checking for Authentication of MHP...
        ResponseEntity<Map<String, Object>> MHPResponseData = authenticateUserFactory.authenticateUser("mhp", userDTOData);
        if(MHPResponseData.getStatusCode() != HttpStatus.OK)
            return response;


        return MHPResponseData;
    }
}
