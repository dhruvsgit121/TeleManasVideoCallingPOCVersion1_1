package com.example.ehrc.telemanas.Controller;

import com.example.ehrc.telemanas.AuthenticateService.AuthenticateUserFactory;
import com.example.ehrc.telemanas.DTO.AuthenticateUserDTO;
import com.example.ehrc.telemanas.GlobalRequestHandler.VideoCallingAPIRequestHandler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    private VideoCallingAPIRequestHandler videoCallingAPIRequestHandler;

    @Autowired
    private AuthenticateUserFactory authenticateUserFactory;

    @RequestMapping("/gethelloworld")
    public ResponseEntity<Map<String, Object>> getHelloWorld(@Valid @RequestBody AuthenticateUserDTO userDTOData,
                                             @RequestHeader(value = "BearerToken", required = true) String bearerToken,
                                             @RequestHeader(value = "loggedin", required = true) String loggedIn
                                             ) {

        userDTOData.setBearerToken(bearerToken);
        userDTOData.setLoggedInId(loggedIn);

        System.out.println("User data to be sent to api is : " + userDTOData );


        ResponseEntity<Map<String, Object>> response = authenticateUserFactory.authenticateUser("patient", userDTOData);

        System.out.println("REquest data is : " + response);


//        System.out.println("userDTOData data is User UUID is : " + userDTOData.getUserUuid());
//
//        System.out.println("userDTOData data telemanas is : " + userDTOData.getTelemanasId());

//        if(userDTOData.getUserUuid().equals("null"))
//            System.out.println("enetered here getUserUuid");
//
//        if(userDTOData.getTelemanasId().equals("null"))
//            System.out.println("enetered here getTelemanasId");

//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

//        ResponseEntity<Map<String, Object>> response = postRequestService.sendData();


//        ResponseEntity<Map<String, Object>> response = authenticateUserFactory.authenticateUser("patient");
//
////        ResponseEntity<Map<String, Object>> response = videoCallingAPIRequestHandler.makePostRequest("","", "", "");
//
//        Map<String, Object> responseBody = response.getBody();
//        System.out.println("Extracted user data is " + responseBody.get("payload"));
//
//        ArrayList responseData = (ArrayList) responseBody.get("payload");

//        Map<String, Object> map = new HashMap<>();

        return response;

        //new ResponseEntity<>(map, HttpStatus.OK);


//        return (Map<String, Object>) responseData.get(0);

    }
}
