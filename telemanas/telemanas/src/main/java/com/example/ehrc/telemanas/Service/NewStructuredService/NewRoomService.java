package com.example.ehrc.telemanas.Service.NewStructuredService;


import com.example.ehrc.telemanas.CustomException.ValidationMessagesException;
import com.example.ehrc.telemanas.DTO.CallStartDTO;
import com.example.ehrc.telemanas.DTO.NewStructuredDTO.AuthenticateUserDTO;
import com.example.ehrc.telemanas.DTO.NewStructuredDTO.MHPRoomDetailsDTO;
import com.example.ehrc.telemanas.DTO.NewStructuredDTO.PatientRoomDetailsDTO;
import com.example.ehrc.telemanas.DTO.RoomCreationDataDTO;
import com.example.ehrc.telemanas.DTO.RoomDetailsRequestDTO;
import com.example.ehrc.telemanas.Model.NewStructuredModal.*;
import com.example.ehrc.telemanas.Model.NewStructuredModal.EYDataModel.MHPDataModal;
import com.example.ehrc.telemanas.Model.NewStructuredModal.EYDataModel.PatientDataModal;
//import com.example.ehrc.telemanas.Model.UpdatedModels.Room;
import com.example.ehrc.telemanas.Model.UpdatedModels.AuthenticatedUser;
import com.example.ehrc.telemanas.Model.UpdatedModels.Participant;
import com.example.ehrc.telemanas.Model.UpdatedModels.Room;
import com.example.ehrc.telemanas.Service.NewServices.TwilioSMSService;
import com.example.ehrc.telemanas.UserRepository.NewRepository.VideoConsultationCallRepository;
import com.example.ehrc.telemanas.UserRepository.NewRepository.VideoConsultationRoomRepository;
import com.example.ehrc.telemanas.UserRepository.NewRepository.VideoConsultationStatusMasterRepository;
import com.example.ehrc.telemanas.UserRepository.NewRepository.VideoConsultationUserRepository;
import com.example.ehrc.telemanas.Utilities.VideoCallingAPIConstants;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
//import jakarta.validation.constraints.Null;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NewRoomService {

    @Autowired
    private VideoCallingUtilities videoCallingUtilities;

    @Autowired
    private VideoConsultationRoomRepository videoConsultationRoomRepository;

    @Autowired
    private VideoConsultationUserRepository videoConsultationUserRepository;

    @Autowired
    private VideoConsultationCallRepository videoConsultationCallRepository;

    @Autowired
    private VideoConsultationStatusMasterRepository videoConsultationStatusMasterRepository;

    @Autowired
    private JWTTokenService jwtTokenService;

    @Autowired
    private TwilioSMSService twilioSMSService;

    @Autowired
    private VideoConsultationCallService videoConsultationCallService;

    @Value("${jwt.expirationOffSet}")
    private int expirationOffset;


    @Transactional
    public ResponseEntity<Map<String, Object>> createRoom(AuthenticateUserDTO userDTOData, PatientDataModal patientDataModal, MHPDataModal mhpDataModal) {

        System.out.println("roomShortCodesList : " + patientDataModal.getMobileNumber());

        //In case we don't have existing Room ID...
        //We will create and return the NEW ROOM CODE...
        String roomID = videoCallingUtilities.generateRandomString(20);
        String roomShortCode = videoCallingUtilities.generateRandomString(20);

        RoomCreationDataDTO roomCreationData = new RoomCreationDataDTO(roomID, roomShortCode);

        //Deactivating the Current Active Rooms for the Current User Pair (Patient & MHP)...
        deactivateActiveRoomsForCurrentUser(userDTOData.getMhpUserName(), patientDataModal.getTelemanasId());

        List<VideoConsultationParticipant> videoConsultationParticipantsList = saveVideoConsultationParticipantsData(roomCreationData, userDTOData, mhpDataModal, patientDataModal);
        System.out.println("videoConsultationParticipantsList" + videoConsultationParticipantsList);

        return saveNewRoomData(userDTOData, mhpDataModal, patientDataModal);
    }


    @Transactional
    public void deactivateActiveRoomsForCurrentUser(String mhpID, String patientID) {

        List<VideoConsultationRoom> activeRoomsList = videoConsultationRoomRepository.findAllActiveRoomLists();

        for (VideoConsultationRoom room : activeRoomsList) {
            System.out.println("deactivateActiveRoomsForCurrentUser ID's are : " + room.getId());
            ArrayList<VideoConsultationParticipant> participants = new ArrayList<>(room.getParticipants());
            if (participants.size() == 2) {

                //Extracting the Video Consultation Participants...
                VideoConsultationParticipant firstParticipant = participants.get(0);
                VideoConsultationParticipant secondParticipant = participants.get(1);

                //Extracting the Video Consultation Participants Participant ID...
                String firstParticipantUserID = firstParticipant.getParticipantID();
                String secondParticipantUserID = secondParticipant.getParticipantID();

                if ((firstParticipantUserID.equals(mhpID) && secondParticipantUserID.equals(patientID) || ((firstParticipantUserID.equals(patientID) && secondParticipantUserID.equals(mhpID))))) {
                    VideoConsultationRoom currentRoom = videoConsultationRoomRepository.getReferenceById(room.getId());
                    setRoomActivationDeactivationFlag(currentRoom, false);
                    videoConsultationRoomRepository.save(currentRoom);
                }
            }
        }
    }


    public List<VideoConsultationParticipant> saveVideoConsultationParticipantsData(RoomCreationDataDTO roomCreationData, AuthenticateUserDTO userDTOData, MHPDataModal mhpDataModal, PatientDataModal patientDataModal) {

        String doctorJWTToken = jwtTokenService.generateJWTToken(mhpDataModal.getMhpName(), userDTOData.getMhpUserName(), "dummyMHPemailid", roomCreationData.getRoomID(), true);
        String patientJWTToken = jwtTokenService.generateJWTToken(patientDataModal.getPatientName(), userDTOData.getTelemanasId(), "dummyPatientemailid", roomCreationData.getRoomID(), false);

        VideoConsultationParticipant mhpParticipant = new VideoConsultationParticipant(doctorJWTToken, userDTOData.getMhpUserName(), true);
        VideoConsultationParticipant patientParticipant = new VideoConsultationParticipant(patientJWTToken, userDTOData.getTelemanasId(), false);


        patientParticipant.setUser(savePatientUserData(patientDataModal));
        mhpParticipant.setUser(saveMHPUserData(mhpDataModal, userDTOData));


        //Adding the Participants...
        List<VideoConsultationParticipant> participantsList = new ArrayList<>();
        participantsList.add(mhpParticipant);
        participantsList.add(patientParticipant);

        return participantsList;
    }


    public VideoConsultationUser savePatientUserData(PatientDataModal patientDataModal) {

        VideoConsultationUser userData = videoConsultationUserRepository.findUswerWithUserID(patientDataModal.getTelemanasId());
        if (userData == null) {
            VideoConsultationUser patientUserData = new VideoConsultationUser(patientDataModal.getPatientName(), patientDataModal.getTelemanasId());
            return videoConsultationUserRepository.save(patientUserData);
        }
        return userData;
    }

    public VideoConsultationUser saveMHPUserData(MHPDataModal mhpDataModal, AuthenticateUserDTO userDTOData) {

        VideoConsultationUser userData = videoConsultationUserRepository.findUswerWithUserID(mhpDataModal.getMhpName());
        if (userData == null) {
            VideoConsultationUser patientUserData = new VideoConsultationUser(mhpDataModal.getMhpName(), userDTOData.getMhpUserName(), mhpDataModal.getRoleDisplayName(), "MHPDATAREGISTARATIONNUMBER");
            return videoConsultationUserRepository.save(patientUserData);
        }
        return userData;
    }


    public ResponseEntity<Map<String, Object>> saveNewRoomData(AuthenticateUserDTO userDTOData, MHPDataModal mhpDataModal, PatientDataModal patientDataModal) {

        LocalDateTime expiryDate = videoCallingUtilities.getDateTimeWithOffset(expirationOffset);

        //In case we don't have existing Room ID...
        //We will create and return the NEW ROOM CODE...
        String roomID = videoCallingUtilities.generateRandomString(20);
        String roomShortCode = videoCallingUtilities.generateRandomString(20);

        RoomCreationDataDTO roomCreationData = new RoomCreationDataDTO(roomID, roomShortCode);

        VideoConsultationRoom videoConsultationRoom = new VideoConsultationRoom(roomID, roomShortCode, videoCallingUtilities.getDateTimeWithOffset(0), expiryDate, null, true);

        //Parsing the Video Consultation Participants List...
        List<VideoConsultationParticipant> participantsList = saveVideoConsultationParticipantsData(roomCreationData, userDTOData, mhpDataModal, patientDataModal);
        for (VideoConsultationParticipant participant : participantsList) {
            videoConsultationRoom.addParticipant(participant);
        }

        //Search from status master...
        setRoomActivationDeactivationFlag(videoConsultationRoom, true);

        //Save Video Consultation Call Data...
        VideoConsultationCall callData = setVideoConsultationCallData();
        videoConsultationRoom.setVideoCallData(callData);

        //Save Video Room & Call Data...
        videoConsultationRoomRepository.save(videoConsultationRoom);

        Map<String, Object> responseMap = videoCallingUtilities.getSuccessResponseMap();
        responseMap.put("roomCode", videoConsultationRoom.getRoomShortCode());

        twilioSMSService.sendTestSms(patientDataModal.getMobileNumber(), roomShortCode);

        return new ResponseEntity(responseMap, HttpStatus.OK);
    }

    public VideoConsultationCall setVideoConsultationCallData() {
        String videoCallUUID = videoCallingUtilities.generateRandomString(20);
        VideoConsultationCall videoConsultationCall = new VideoConsultationCall(videoCallUUID);
        return videoConsultationCallRepository.save(videoConsultationCall);
    }


    public ResponseEntity<Map<String, Object>> deactivateRequestedRoom(String roomShortCode) {

        VideoConsultationRoom requestedRoom = videoConsultationRoomRepository.findRoomDetailsWithActiveStatus(roomShortCode);

        if (requestedRoom == null)
            return videoCallingUtilities.getErrorResponseMessageEntity(VideoCallingAPIConstants.ERROR_MESSAGE_ROOM_DOES_NOT_EXISTS, HttpStatus.OK);

        setRoomActivationDeactivationFlag(requestedRoom, false);
        videoConsultationRoomRepository.save(requestedRoom);

        return new ResponseEntity<>(videoCallingUtilities.getSuccessResponseMap(), HttpStatus.OK);
    }

    @Transactional
    private void setRoomActivationDeactivationFlag(VideoConsultationRoom videoConsultationRoom, boolean isActive) {
        VideoConsultationStatusMaster statusActiveFlagData = videoConsultationStatusMasterRepository.findStatusWithStatusName(isActive ? "active" : "inactive");
        videoConsultationRoom.setActive(isActive);
        videoConsultationRoom.setStatus(statusActiveFlagData);
    }


    public ResponseEntity<Map<String, Object>> joinRoom(CallStartDTO callStartDTO) {
        videoConsultationCallService.SetVideoCallStatusForUser(callStartDTO.getRoomShortCode(), (callStartDTO.getIsMHP() == 1));
        videoConsultationCallService.SetVideoCallJoiningTime(callStartDTO.getRoomShortCode());
        Map<String, Object> responseData = videoCallingUtilities.getSuccessResponseMap();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    public ResponseEntity<Map<String, Object>> joinPatientRoom(RoomDetailsRequestDTO roomDetailsRequest) {
        videoConsultationCallService.SetVideoCallStatus(roomDetailsRequest.getRoomShortCode(), VideoConsultationCallService.VideoCallStatus.PATIENTJOINED);//SetVideoCallStatusForUser(callStartDTO.getRoomShortCode(), false);
        Map<String, Object> responseData = videoCallingUtilities.getSuccessResponseMap();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<Map<String, Object>> getPatientRoomDetails(PatientRoomDetailsDTO patientRoomDetailsDTO) {

        if (videoCallingUtilities.getRoomActivationCheckResponseMap(patientRoomDetailsDTO.getRoomShortCode()) != null) {
            return videoCallingUtilities.getRoomActivationCheckResponseMap(patientRoomDetailsDTO.getRoomShortCode());
        }

        VideoConsultationRoom requestedRoom = videoConsultationRoomRepository.findRoomDetailsWithActiveStatus(patientRoomDetailsDTO.getRoomShortCode());

        List<VideoConsultationParticipant> participantList = requestedRoom.getParticipants();

        System.out.println("getPatientRoomDetails in participantList : " + participantList.size());

        VideoConsultationParticipant patientParticipant = getParticipant(participantList, false);
        VideoConsultationParticipant mhpParticipant = getParticipant(participantList, true);

        VideoConsultationCall videoCall = requestedRoom.getVideoCallData();
        videoCall.setPatientConsent1Given(patientRoomDetailsDTO.getIsFirstConsentProvided() == 1);
        videoCall.setPatientConsent2Given(patientRoomDetailsDTO.getIsSecondConsentProvided() == 1);

        videoConsultationRoomRepository.save(requestedRoom);

        Map<String, Object> responseData = videoCallingUtilities.getSuccessResponseMap();

        responseData.put("jwtToken", patientParticipant.getJwt_token());
        responseData.put("userName", patientParticipant.getUser().getUserName());
        responseData.put("participatingUserName", mhpParticipant.getUser().getUserName());
        responseData.put("userId", patientParticipant.getUser().getUserID());
        responseData.put("participatingUserId", mhpParticipant.getUser().getId());
        responseData.put("jwtURL", videoCallingUtilities.generateJWTURL(requestedRoom.getRoomId(), patientParticipant.getJwt_token()));

        responseData.put("clientID", mhpParticipant.getUser().getUserID());
        responseData.put("MHPRegistrationNumber", mhpParticipant.getUser().getUserRegistrationNumber());
        responseData.put("Designation", mhpParticipant.getUser().getUserDesignation());

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }


    public VideoConsultationParticipant getParticipant(List<VideoConsultationParticipant> participantList, boolean isMHP) {
        for (VideoConsultationParticipant participant : participantList) {
            if (participant.isOrganiser() && isMHP) {
                return participant;
            }
            if (!participant.isOrganiser() && !isMHP) {
                return participant;
            }
        }
        return null;
    }


    public ResponseEntity<Map<String, Object>> MHPExitRoom(CallStartDTO callStartDTO) {
        videoConsultationCallService.SetVideoCallStatus(callStartDTO.getRoomShortCode(), VideoConsultationCallService.VideoCallStatus.ENDED);
        videoConsultationCallService.SetVideoCallEndingTime(callStartDTO.getRoomShortCode());
        Map<String, Object> responseData = videoCallingUtilities.getSuccessResponseMap();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }


    public ResponseEntity<Map<String, Object>> getPatientJoinData(String roomShortCode) {

        VideoConsultationRoom roomData = videoConsultationRoomRepository.findRoomDetailsWithActiveStatus(roomShortCode);

        ResponseEntity<Map<String, Object>> responseMap = videoCallingUtilities.getRoomActivationCheckResponseMap(roomShortCode);
        if (responseMap != null)
            return responseMap;

        if (roomData.getVideoCallData().getCallStatus().equals(VideoConsultationCallService.VideoCallStatus.INPROGRESS))
            return videoCallingUtilities.getGlobalErrorResponseMessageEntity("Video call is in progress right now. Please try again later.");

        Map<String, Object> responseData = new HashMap<>();
        responseData.put(VideoCallingAPIConstants.isErrorFlagValue, false);
        if (roomData.getVideoCallData().getCallStatus().equals(VideoConsultationCallService.VideoCallStatus.PATIENTJOINED))
            responseData.put("joinedRoom", true);

        return new ResponseEntity(responseData, HttpStatus.OK);
    }


    public ResponseEntity<Map<String, Object>> getPatientRoomDetails(String roomShortCode) {

        VideoConsultationRoom roomData = videoConsultationRoomRepository.findRoomDetailsWithActiveStatus(roomShortCode);

        ResponseEntity<Map<String, Object>> responseMap = videoCallingUtilities.getRoomActivationCheckResponseMap(roomShortCode);
        if (responseMap != null)
            return responseMap;

        if (roomData.getVideoCallData().getCallStatus().equals(VideoConsultationCallService.VideoCallStatus.INPROGRESS))
            return videoCallingUtilities.getGlobalErrorResponseMessageEntity("Video call is in progress right now. Please try again later.");

        Map<String, Object> responseData = new HashMap<>();
        responseData.put(VideoCallingAPIConstants.isErrorFlagValue, false);
        if (roomData.getVideoCallData().getCallStatus().equals(VideoConsultationCallService.VideoCallStatus.PATIENTJOINED))
            responseData.put("joinedRoom", true);

        return new ResponseEntity(responseData, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> getMHPRoomDetails(MHPRoomDetailsDTO mhpRoomDetailsDTO) {

        if (videoCallingUtilities.getRoomActivationCheckResponseMap(mhpRoomDetailsDTO.getRoomShortCode()) != null) {
            return videoCallingUtilities.getRoomActivationCheckResponseMap(mhpRoomDetailsDTO.getRoomShortCode());
        }

        VideoConsultationRoom requestedRoom = videoConsultationRoomRepository.findRoomDetailsWithActiveStatus(mhpRoomDetailsDTO.getRoomShortCode());

        List<VideoConsultationParticipant> participantList = requestedRoom.getParticipants();

//        System.out.println("getPatientRoomDetails in participantList : " + participantList.size());

        VideoConsultationParticipant patientParticipant = getParticipant(participantList, false);
        VideoConsultationParticipant mhpParticipant = getParticipant(participantList, true);

        VideoConsultationCall videoCall = requestedRoom.getVideoCallData();
        videoCall.setMHPConsentGiven(true);

        videoConsultationRoomRepository.save(requestedRoom);

        Map<String, Object> responseData = videoCallingUtilities.getSuccessResponseMap();

        responseData.put("jwtToken", mhpParticipant.getJwt_token());
        responseData.put("userName", mhpParticipant.getUser().getUserName());
        responseData.put("participatingUserName", patientParticipant.getUser().getUserName());
        responseData.put("userId", mhpParticipant.getUser().getUserID());
        responseData.put("participatingUserId", patientParticipant.getUser().getUserID());
        responseData.put("jwtURL", videoCallingUtilities.generateJWTURL(requestedRoom.getRoomId(), mhpParticipant.getJwt_token()));

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }
}
