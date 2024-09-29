package com.example.ehrc.telemanas.Controller;


import com.example.ehrc.telemanas.AuthenticateService.AuthenticateUserFactory;
import com.example.ehrc.telemanas.DTO.NewStructuredDTO.AuthenticateUserDTO;
import com.example.ehrc.telemanas.DTO.CallStartDTO;
import com.example.ehrc.telemanas.DTO.RoomDetailsRequestDTO;
import com.example.ehrc.telemanas.DTO.VideoCallEventsDTO;
import com.example.ehrc.telemanas.Service.NewServices.VideoCallService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


@RestController
@RequestMapping("/rooms")
public class VideoController {

    @Autowired
    private AuthenticateUserFactory authenticateUserFactory;

    @Autowired
    private VideoCallService videoCallService;

//    @Autowired
//    private SSEService sseService;


    //    @GetMapping("/files/{id}")
    @GetMapping("/files/{roomShortCode}")
    public ResponseEntity<byte[]> getFile(@PathVariable String roomShortCode) {
        return videoCallService.getFile(roomShortCode);
    }


    //    @GetMapping("/files/{id}")
    @GetMapping("/verifyuseridentity/{roomShortCode}")
    public ResponseEntity<String> verifyUserIdentity(@PathVariable String roomShortCode) {
        return videoCallService.verifyUserIdentity(roomShortCode);
    }



//    @PostMapping("/saveeventsdata")
//    public ResponseEntity<Map<String, Object>> saveVideoCallEventsData(@Valid @RequestBody VideoCallEventsDTO videoCallEventsDTO) {
//        return videoCallService.saveVideoCallEvents(videoCallEventsDTO);
//    }

//    @GetMapping("/helloworld")
//    public String getVideoRoomDetails() {
//        return "videoCallService.startVideoCall(roomDetailsRequest)!!!!!!";
//    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("roomShortCode") String roomShortCode) {
        return videoCallService.uploadFile(file, roomShortCode);
    }

    @PostMapping("/getroomdetails")
    public ResponseEntity<Map<String, Object>> getVideoRoomDetails(@Valid @RequestBody RoomDetailsRequestDTO roomDetailsRequest) {

        System.out.println("getVideoRoomDetails called");

        System.out.println("API Hitted with data : " + roomDetailsRequest.getRoomShortCode());
        System.out.println("API Hitted with data : " + roomDetailsRequest.getIsMHP());

        //Setting the "hasJoined" Flag for Patient to TRUE...
        //if (roomDetailsRequest.getIsMHP() == 0)
        videoCallService.saveIsActiveRoomOnJoinVideoCall(roomDetailsRequest);

        if (roomDetailsRequest.getIsMHP() == 0)
            videoCallService.saveConsentForPatientOnJoinVideoCall(roomDetailsRequest);

        if (roomDetailsRequest.getIsMHP() == 0) {

            System.out.println("entered in videocontrolle rgetVideoRoomDetails ");
            ResponseEntity<Map<String, Object>> responseEntity = videoCallService.checkValidityOfSMSLinkForPatient(roomDetailsRequest.getRoomShortCode());

            if (responseEntity != null && responseEntity.hasBody() && responseEntity.getStatusCode() != HttpStatus.OK) {
                System.out.println("responseEntity != null && responseEntity.hasBody() && ");
                return responseEntity;
            }
        }

//        ResponseEntity<Map<String, Object>> joinUserFlag = setJoinRoomTime(roomDetailsRequest);
//
//        if (joinUserFlag != null && joinUserFlag.hasBody() && joinUserFlag.getStatusCode() != HttpStatus.OK) {
//            return joinUserFlag;
//        }

        return videoCallService.startVideoCall(roomDetailsRequest);
    }

//    @GetMapping("/getpatientjoinflag")
//    public ResponseEntity<Map<String, Object>> getPatientRoomJoinDetails(@RequestParam String roomShortCode) {
//        return videoCallService.getPatientJoinRoomStatusData(roomShortCode);
//    }
//
//    @GetMapping("/deactivateroom")
//    public ResponseEntity<Map<String, Object>> deactivatedRequestedRoom(@RequestParam String roomShortCode) {
//        return videoCallService.deactivateRequestedRoom(roomShortCode);
//    }

//    @PostMapping("/patientjoinroom")
//    public ResponseEntity<Map<String, Object>> setJoinRoomTime(@Valid @RequestBody RoomDetailsRequestDTO roomDetailsRequest) {
//        return videoCallService.JoinVideoCall(roomDetailsRequest);
//    }


    @RequestMapping("/resendlink")
    public ResponseEntity<Map<String, Object>> resendMeetingLink(@Valid @RequestBody AuthenticateUserDTO userDTOData,
                                                                 @RequestHeader("Authorization") String bearerToken,
                                                                 @RequestHeader(value = "Loggedin") String loggedIn) {
        setAuthorizationData(userDTOData, bearerToken, loggedIn);
        return videoCallService.decryptPatientMobileNumberForResendSMS(userDTOData, authenticateUserFactory, userDTOData.getRoomShortCode());
    }


//    @PostMapping("/joinroom")
//    public ResponseEntity<Map<String, Object>> setJoinRoomTime(@Valid @RequestBody CallStartDTO callStartDTO, @RequestHeader("Authorization") String bearerToken,
//                                                               @RequestHeader(value = "Loggedin") String loggedIn) {
//        setAuthorizationData(callStartDTO, bearerToken, loggedIn);
//        return videoCallService.JoinVideoCall(callStartDTO);
//    }


//    @PostMapping("/patientexitroom")
//    public ResponseEntity<Map<String, Object>> leaveVideoCall(@Valid @RequestBody RoomDetailsRequestDTO roomDetailsRequest) {
//        return videoCallService.leaveVideoCall(roomDetailsRequest);
//    }
//
//
//    @PostMapping("/exitroom")
//    public ResponseEntity<Map<String, Object>> leaveVideoCall(@Valid @RequestBody CallStartDTO callStartDTO, @RequestHeader("Authorization") String bearerToken,
//                                                              @RequestHeader(value = "Loggedin") String loggedIn) {
//        setAuthorizationData(callStartDTO, bearerToken, loggedIn);
//        return videoCallService.leaveVideoCall(callStartDTO);
//    }


//    private void setAuthorizationData(CallStartDTO callStartDTO, String bearerToken, String loggedIn) {
//
//        String token = bearerToken.substring(7);
//
//        //Adjusting The UserDTOData...
//        callStartDTO.setBearerToken(token);
//        callStartDTO.setLoggedInId(loggedIn);
//    }

    private void setAuthorizationData(AuthenticateUserDTO userDTOData, String bearerToken, String loggedIn) {

        String token = bearerToken.substring(7);

        //Adjusting The UserDTOData...
        userDTOData.setBearerToken(token);
        userDTOData.setLoggedInId(loggedIn);
    }

//    @RequestMapping("/createroom")
//    public ResponseEntity<Map<String, Object>> createRoom(@Valid @RequestBody AuthenticateUserDTO userDTOData,
//                                                          @RequestHeader("Authorization") String bearerToken,
//                                                          @RequestHeader(value = "Loggedin") String loggedIn
//    ) {
//        setAuthorizationData(userDTOData, bearerToken, loggedIn);
//        return videoCallService.createMeetingLink(userDTOData, authenticateUserFactory);
//    }
}