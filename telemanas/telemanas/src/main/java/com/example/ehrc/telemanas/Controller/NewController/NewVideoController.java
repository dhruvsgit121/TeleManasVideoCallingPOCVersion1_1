package com.example.ehrc.telemanas.Controller.NewController;

import com.example.ehrc.telemanas.DTO.CallStartDTO;
import com.example.ehrc.telemanas.DTO.NewStructuredDTO.AuthenticateUserDTO;
//import com.example.ehrc.telemanas.Service.NewServices.VideoCallService;
import com.example.ehrc.telemanas.DTO.NewStructuredDTO.ResendVideoCallLinkDTO;
import com.example.ehrc.telemanas.DTO.NewStructuredDTO.SendPrescriptionDTO;
import com.example.ehrc.telemanas.DTO.NewStructuredDTO.VerifyUserIdentityDTO;
import com.example.ehrc.telemanas.DTO.RoomDetailsRequestDTO;
import com.example.ehrc.telemanas.DTO.VideoCallEventsDTO;
import com.example.ehrc.telemanas.Service.NewStructuredService.NewVideoService;
import com.example.ehrc.telemanas.Service.NewStructuredService.RoomManagerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

//import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/room")
public class NewVideoController {

    @Autowired
    private RoomManagerService roomManagerService;

    @Autowired
    private NewVideoService videoService;


    //###################### ROOM RELATED API's ######################

    @RequestMapping("/createroom")
    public ResponseEntity<Map<String, Object>> createRoom(@Valid @RequestBody AuthenticateUserDTO userAuthorisationDataDTO,
                                                          @RequestHeader("Authorization") String bearerToken,
                                                          @RequestHeader(value = "Loggedin") String loggedIn
    ) {
        setAuthorizationData(userAuthorisationDataDTO, bearerToken, loggedIn);
        return videoService.createMeetingLink(userAuthorisationDataDTO);
    }


    //###################### VIDEO CALL RELATED API's ######################

    @PostMapping("/joinroom")
    public ResponseEntity<Map<String, Object>> setJoinRoomTime(@Valid @RequestBody CallStartDTO callStartDTO, @RequestHeader("Authorization") String bearerToken,
                                                               @RequestHeader(value = "Loggedin") String loggedIn) {
        setAuthorizationData(callStartDTO, bearerToken, loggedIn);
        return videoService.JoinVideoCall(callStartDTO);
    }

    @PostMapping("/patientjoinroom")
    public ResponseEntity<Map<String, Object>> setJoinRoomTime(@Valid @RequestBody RoomDetailsRequestDTO roomDetailsRequest) {
        return videoService.JoinPatientVideoCall(roomDetailsRequest);//videoCallService.JoinVideoCall(roomDetailsRequest);
    }

    @PostMapping("/exitroom")
    public ResponseEntity<Map<String, Object>> leaveVideoCall(@Valid @RequestBody CallStartDTO callStartDTO, @RequestHeader("Authorization") String bearerToken,
                                                              @RequestHeader(value = "Loggedin") String loggedIn) {
        setAuthorizationData(callStartDTO, bearerToken, loggedIn);
        return videoService.LeaveMHPVideoCall(callStartDTO);
    }


    @GetMapping("/getpatientjoinflag")
    public ResponseEntity<Map<String, Object>> getPatientRoomJoinDetails(@RequestParam String roomShortCode) {
        return videoService.PatientJoinVideoCall(roomShortCode);
    }


    //###################### VIDEO CALL RELATED API's ######################


    @PostMapping("/saveeventsdata")
    public ResponseEntity<Map<String, Object>> saveVideoCallEventsData(@Valid @RequestBody VideoCallEventsDTO videoCallEventsDTO) {
        return videoService.saveVideoCallEvents(videoCallEventsDTO);
    }


    @GetMapping("/deactivateroom")
    public ResponseEntity<Map<String, Object>> deactivatedRequestedRoom(@RequestParam String roomShortCode) {
        System.out.println("deactivateroom requetsed code is : " + roomShortCode);
        return videoService.deactivateRequestedRoom(roomShortCode);
    }

    @GetMapping("/files/{roomShortCode}")
    public ResponseEntity<Map<String, Object>> getFile(@PathVariable String roomShortCode) {
        return videoService.getFile(roomShortCode);
    }


    @GetMapping("/fetchuseridentityuploadflag")
    public ResponseEntity<Map<String, Object>> fetchUserIdentityUploadFlag(@RequestParam String roomShortCode) {
        return videoService.fetchUserIdentityUploadFlag(roomShortCode);
    }


    @RequestMapping("/send_prescription")
    public ResponseEntity<Map<String, Object>> sendPrescriptionLink(@Valid @RequestBody SendPrescriptionDTO sendPrescriptionData,
                                                                 @RequestHeader("Authorization") String bearerToken,
                                                                 @RequestHeader(value = "Loggedin") String loggedIn) {

        AuthenticateUserDTO userDataDTO = new AuthenticateUserDTO(sendPrescriptionData);
        setAuthorizationData(userDataDTO, bearerToken, loggedIn);
        return videoService.resendPrescriptionLink(userDataDTO, sendPrescriptionData);
    }



    @PostMapping("/verifyuseridentity")
    public ResponseEntity<Map<String, Object>> verifyUserIdentity(@Valid @RequestBody VerifyUserIdentityDTO verifyUserIdentityDTO) {
        return videoService.verifyUserIdentity(verifyUserIdentityDTO);
    }


    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("roomShortCode") String roomShortCode) {
        return videoService.uploadFile(file, roomShortCode);
    }


    @GetMapping("/get_prescription")
    public ResponseEntity<Map<String, Object>> getPrescriptionDetails(@RequestParam String roomShortCode) {
        return videoService.getPrescriptionDetails(roomShortCode);
    }


    @RequestMapping("/resendlink")
    public ResponseEntity<Map<String, Object>> resendMeetingLink(@Valid @RequestBody ResendVideoCallLinkDTO resendLinkData,
                                                                 @RequestHeader("Authorization") String bearerToken,
                                                                 @RequestHeader(value = "Loggedin") String loggedIn) {

        AuthenticateUserDTO userDataDTO = new AuthenticateUserDTO(resendLinkData);


        setAuthorizationData(userDataDTO, bearerToken, loggedIn);
        return videoService.resendVideoCallLink(userDataDTO, resendLinkData.getEncryptedPhoneNumber());
    }




    private void setAuthorizationData(AuthenticateUserDTO userDTOData, String bearerToken, String loggedIn) {
        String token = bearerToken.substring(7);

        //Adjusting The UserDTOData...
        userDTOData.setBearerToken(token);
        userDTOData.setLoggedInId(loggedIn);
    }

//    private void setAuthorizationData(ResendVideoCallLinkDTO resendLinkData, String bearerToken, String loggedIn) {
//        String token = bearerToken.substring(7);
//
//        //Adjusting The UserDTOData...
//        resendLinkData.setBearerToken(token);
//        resendLinkData.setLoggedInId(loggedIn);
//    }

    private void setAuthorizationData(CallStartDTO callStartDTO, String bearerToken, String loggedIn) {
        String token = bearerToken.substring(7);

        //Adjusting The UserDTOData...
        callStartDTO.setBearerToken(token);
        callStartDTO.setLoggedInId(loggedIn);
    }
}
