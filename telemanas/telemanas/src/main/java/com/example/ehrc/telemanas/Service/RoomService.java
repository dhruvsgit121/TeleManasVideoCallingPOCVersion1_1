package com.example.ehrc.telemanas.Service;

//import com.example.ehrc.telemanas.AuthenticateService.AuthenticateUserFactory;

import com.example.ehrc.telemanas.CustomException.ValidationMessagesException;
import com.example.ehrc.telemanas.DTO.AuthenticateUserDTO;
import com.example.ehrc.telemanas.DTO.RoomDetailsRequestDTO;
import com.example.ehrc.telemanas.Model.EYDataModel.MHPDataModal;
import com.example.ehrc.telemanas.Model.EYDataModel.PatientDataModal;
import com.example.ehrc.telemanas.Model.Participant;
import com.example.ehrc.telemanas.Model.Room;
import com.example.ehrc.telemanas.Model.UpdatedModels.UpdatedAuthenticatedUser;
import com.example.ehrc.telemanas.Model.UpdatedModels.UpdatedParticipant;
import com.example.ehrc.telemanas.Model.UpdatedModels.UpdatedRoom;
import com.example.ehrc.telemanas.UserRepository.RoomRepository;
import com.example.ehrc.telemanas.UserRepository.UpdatedAuthenticatedUserRepository;
import com.example.ehrc.telemanas.UserRepository.UpdatedParticipantRepository;
import com.example.ehrc.telemanas.UserRepository.UpdatedRoomRepository;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UpdatedRoomRepository updatedRoomRepository;

    @Autowired
    private UpdatedAuthenticatedUserRepository updatedAuthenticatedUserRepository;

    @Autowired
    private UpdatedParticipantRepository updatedParticipantRepository;

    @Autowired
    private SSEService sseService;

    private final JWTTokenService jwtTokenService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    public RoomService(JWTTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }


    @Value("${jwt.RoomJWTValidityOffSet}")
    private Long roomJWTValidityOffSet;

    @Value("${jwt.expirationOffSet}")
    private int expirationOffset;

    @Autowired
    private VideoCallingUtilities videoCallingUtilities;

    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    public Room getRoomDetailsWith(String shortCode) {
        return roomRepository.findRoomDetailsWith(shortCode);
    }


    public UpdatedRoom getUpdatedRoomDetailsWith(String shortCode) {
        return updatedRoomRepository.findRoomDetailsWith(shortCode);
    }


    public List<Room> getRoomListWithExpirationdate(LocalDateTime expirationDate) {
        return roomRepository.findRoomListWithExpirationDate(expirationDate);
    }

    //####################################### Methods for Room Creation #####################################

//    public ResponseEntity<Map<String, Object>> decryptPatientMobileNumber(AuthenticateUserFactory authenticateUserFactory, AuthenticateUserDTO userDTOData, EYUserDataModal userDataModal) {
//
//        //Decrypting Mobile Number of The Patient...
//        ResponseEntity<Map<String, Object>> decryptMobileData = authenticateUserFactory.decryptUserPhoneNumber(userDTOData, userDataModal.getEncryptedMobileNumber());
//        if (decryptMobileData.getStatusCode() != HttpStatus.OK)
//            return decryptMobileData;
//
//        System.out.println("Data Recieved is in mobile decrypt is : " + decryptMobileData.getBody());
//
//        if (decryptMobileData.hasBody()) {
//            Map<String, Object> encryptMobileNumberResponseData = decryptMobileData.getBody();
//            userDataModal.setMobileNumber((String) encryptMobileNumberResponseData.get("responsePhoneNo"));
//        }
//
//        return null;
//    }


    public ResponseEntity<Map<String, Object>> startVideoCall(RoomDetailsRequestDTO roomDetailsRequest) {

        Map<String, Object> roomServiceRequestData = generateVideoRoomDetailsResponseEntity(roomDetailsRequest);
        System.out.println("roomServiceRequestData" + roomServiceRequestData);

        if (roomServiceRequestData.containsKey("clientID")) {
            //User is a patient, who is joining the call...
            String clientID = roomServiceRequestData.get("clientID").toString();
            sseService.sendCustomMessage(clientID + "", "Hello patient joined the call...");
        }

        return new ResponseEntity(roomServiceRequestData, HttpStatus.OK);
    }


    public ResponseEntity<Map<String, Object>> joinRoom(RoomDetailsRequestDTO roomDetailsRequest) {

        Map<String, Object> responseData = videoCallingUtilities.getSuccessResponseMap();//new HashMap<>();
        ArrayList<Long> participantsList = new ArrayList(participantService.getParticipantsListWith(roomDetailsRequest.getRoomShortCode()));
        System.out.println("Participant list is : " + participantsList);

        if (participantsList.size() != 2) {
            throw new ValidationMessagesException("Room you requested is not valid. Please try to join new room.");
        }

        //Setting the Join Date of the user...
        Participant requestedParticipant = videoCallingUtilities.getRequestedUserAsPerRequest(roomDetailsRequest, participantsList);
        requestedParticipant.setJoinDate(videoCallingUtilities.getDateTimeWithOffset(0));

        participantService.saveParticipant(requestedParticipant);

        responseData.put("message", "success");
        return new ResponseEntity(responseData, HttpStatus.OK);
    }


    public ResponseEntity<Map<String, Object>> exitRoom(RoomDetailsRequestDTO roomDetailsRequest) {

        Map<String, Object> responseData = videoCallingUtilities.getSuccessResponseMap();

        ArrayList<Long> participantsList = new ArrayList(participantService.getParticipantsListWith(roomDetailsRequest.getRoomShortCode()));

        if (participantsList.size() != 2) {
            throw new ValidationMessagesException("Room you requested is not valid. Please try to join new room.");
        }

        //Setting the Left Date of the user...
        Participant requestedParticipant = videoCallingUtilities.getRequestedUserAsPerRequest(roomDetailsRequest, participantsList);
        requestedParticipant.setLeftDate(videoCallingUtilities.getDateTimeWithOffset(0));

        participantService.saveParticipant(requestedParticipant);

        responseData.put("message", "success");
        return new ResponseEntity(responseData, HttpStatus.OK);
    }


    public ResponseEntity<Map<String, Object>> processAlreadyExistedRoom(ArrayList<String> roomShortCodesList) {

        //In case we have Already Room ID then we won't create it...
        //Will return the already existing ROOM CODE...
        if (roomShortCodesList.size() > 0) {
            Map<String, Object> responseMap = videoCallingUtilities.getSuccessResponseMap();
            String roomShortCode = roomShortCodesList.get(0);
            responseMap.put("roomCode", roomShortCode);
            System.out.println("Entered in existing room maps with room id " + roomShortCode);

            List<Long> participantsList = participantService.getParticipantsListWith(roomShortCode);
            System.out.println("Data out put is : " + participantsList);
            setJoinedRoomFlag(participantsList);
            return new ResponseEntity(responseMap, HttpStatus.OK);
        }
        return null;
    }


    private void setJoinedRoomFlag(List<Long> participantsList) {
        for (Long participantId : participantsList) {
            System.out.println("enter is  this room number with id :" + participantId);
            Participant participant = participantService.getParticipantByID(participantId);
            participant.setHasJoinedRoom(false);
            participantService.saveParticipant(participant);
        }
    }


    public ResponseEntity<Map<String, Object>> createRoom(AuthenticateUserDTO userDTOData, PatientDataModal patientDataModal, MHPDataModal mhpDataModal, RoomService roomService) {

        LocalDateTime expiryDate = videoCallingUtilities.getDateTimeWithOffset(roomJWTValidityOffSet);

        ArrayList<String> roomShortCodesList = new ArrayList<>(participantService.getRoomShortCodeWith(userDTOData.getMhpUserName(), userDTOData.getTelemanasId(), expiryDate));

        System.out.println("roomShortCodesList : " + roomShortCodesList);
        System.out.println("roomShortCodesList : " + patientDataModal.getMobileNumber());


        //Code for checking existing room code
//        ResponseEntity<Map<String, Object>> alreadyExistedRoomResponseData = processAlreadyExistedRoom(roomShortCodesList);
//        if (alreadyExistedRoomResponseData != null)
//            return alreadyExistedRoomResponseData;
        //Code for checking existing room code


        //In case we don't have existing Room ID...
        //We will create and return the NEW ROOM CODE...
        String roomID = videoCallingUtilities.generateRandomString(20);
        String roomShortCode = videoCallingUtilities.generateRandomString(20);
        String videoID = videoCallingUtilities.generateRandomString(20);

        return processNewlyCreatedRoom(userDTOData, patientDataModal, mhpDataModal, roomService, jwtTokenService, expirationOffset, roomShortCode, roomID, videoID);
    }


    public ResponseEntity<Map<String, Object>> processNewlyCreatedRoom(AuthenticateUserDTO userDTOData, PatientDataModal patientDataModal, MHPDataModal mhpDataModal, RoomService roomService, JWTTokenService jwtTokenService, long expirationOffset, String roomShortCode, String roomID, String videoID) {

        Room roomData = new Room(roomID, videoID, videoCallingUtilities.getDateTimeWithOffset(0), videoCallingUtilities.getDateTimeWithOffset(expirationOffset), true, roomShortCode);

        String doctorJWTToken = jwtTokenService.generateJWTToken(mhpDataModal.getMhpName(), userDTOData.getMhpUserName(), "dummyMHPemailid", roomID, true);
        String patientJWTToken = jwtTokenService.generateJWTToken(patientDataModal.getPatientName(), userDTOData.getTelemanasId(), "dummyPatientemailid", roomID, false);

        System.out.println("Data before saving : " + mhpDataModal.getMhpName());
        System.out.println("Data before saving : " + patientDataModal.getPatientName());

        Participant mhpUser = new Participant(null, null, doctorJWTToken, userDTOData.getMhpUserName(), true, Participant.UserRole.MHP, mhpDataModal.getMhpName());
        Participant patientUser = new Participant(null, null, patientJWTToken, userDTOData.getTelemanasId(), false, Participant.UserRole.PATIENT, patientDataModal.getPatientName());


//        Participant mhpUser = new Participant(null, null, doctorJWTToken, userDTOData.getMhpUserName(), true, Participant.UserRole.MHP);
//        Participant patientUser = new Participant(null, null, patientJWTToken, userDTOData.getTelemanasId(), false, Participant.UserRole.PATIENT);


        roomData.addParticipant(mhpUser);
        roomData.addParticipant(patientUser);

        Room savedRoomData = roomService.saveRoom(roomData);


        saveNewRoomData(roomShortCode, roomID, videoID, mhpUser, patientUser, patientDataModal);


        Map<String, Object> responseMap = videoCallingUtilities.getSuccessResponseMap();
        responseMap.put("roomCode", savedRoomData.getRoomShortCode());

        return new ResponseEntity(responseMap, HttpStatus.OK);
    }


    public void saveNewRoomData(String roomShortCode, String roomID, String videoID, Participant mhpUser, Participant patientUser, PatientDataModal patientDataModal) {

        UpdatedRoom updatedRoomData = new UpdatedRoom(roomID, videoID, videoCallingUtilities.getDateTimeWithOffset(0), videoCallingUtilities.getDateTimeWithOffset(expirationOffset), true, roomShortCode);

        UpdatedParticipant mhpParticipantUser = new UpdatedParticipant(mhpUser);//new UpdatedParticipant(mhpUser.getJoinDate(), mhpUser.getLeftDate(), mhpUser.getJwtToken(), mhpUser.getParticipantId(), mhpUser.isOrganiser(), mhpUser.isHasJoinedRoom(), mhpUser.getUserRole(), mhpUser.getUserName());
        UpdatedParticipant patientParticipantUser = new UpdatedParticipant(patientUser);//new UpdatedParticipant(patientUser.getJoinDate(), patientUser.getLeftDate(), patientUser.getJwtToken(), patientUser.getParticipantId(), patientUser.isOrganiser(), patientUser.isHasJoinedRoom(), patientUser.getUserRole(), patientUser.getUserName());


        //Created The Patient Authenticated User Data Modal...
        UpdatedAuthenticatedUser patientAuthenticatedUser = updatedAuthenticatedUserRepository.findAuthenticatedUser(patientUser.getParticipantId());
        if (patientAuthenticatedUser == null) {
            patientAuthenticatedUser = new UpdatedAuthenticatedUser(Participant.UserRole.PATIENT, patientUser.getUserName(), patientUser.getParticipantId(), patientDataModal.getEncryptedMobileNumber());
            updatedAuthenticatedUserRepository.save(patientAuthenticatedUser);
        }

        //Created The MHP Authenticated User Data Modal...
        UpdatedAuthenticatedUser mhpAuthenticatedUser = updatedAuthenticatedUserRepository.findAuthenticatedUser(mhpUser.getParticipantId());
        if (mhpAuthenticatedUser == null) {
            mhpAuthenticatedUser = new UpdatedAuthenticatedUser(Participant.UserRole.MHP, mhpUser.getUserName(), mhpUser.getParticipantId(), "");
            updatedAuthenticatedUserRepository.save(mhpAuthenticatedUser);
        }


        //Deactivate the Currently Active rooms for the Users....
        deactivateActiveRoomsForCurrentUser(mhpUser, patientUser);


        //Setting the Authenticated User Data for Patient & MHP...
        mhpParticipantUser.setAuthenticatedUser(mhpAuthenticatedUser);
        patientParticipantUser.setAuthenticatedUser(patientAuthenticatedUser);

        //Adding the Participants Data for Patient & MHP...
        updatedRoomData.addParticipant(mhpParticipantUser);
        updatedRoomData.addParticipant(patientParticipantUser);

        //Updating Room Data For the for Patient & MHP...
        updatedRoomRepository.save(updatedRoomData);

    }


    public void deactivateActiveRoomsForCurrentUser(Participant mhpUser, Participant patientUser) {

        List<UpdatedRoom> activeRoomsList = updatedRoomRepository.findAllActiveRoomLists();

        for (UpdatedRoom room : activeRoomsList) {

            ArrayList<UpdatedParticipant> participants = new ArrayList<>(room.getParticipants());

            if (participants.size() == 2) {

                UpdatedParticipant firstParticipant = participants.get(0);
                UpdatedParticipant secondParticipant = participants.get(1);

                String firstParticipantUserID = firstParticipant.getAuthenticatedUser().getParticipantId();
                String secondParticipantUserID = secondParticipant.getAuthenticatedUser().getParticipantId();

                if ((firstParticipantUserID.equals(mhpUser.getParticipantId()) && secondParticipantUserID.equals(patientUser.getParticipantId()) || ((firstParticipantUserID.equals(patientUser.getParticipantId()) && secondParticipantUserID.equals(mhpUser.getParticipantId()))))) {
                    UpdatedRoom currentRoom = updatedRoomRepository.getReferenceById(room.getSerialId());
                    currentRoom.setActive(false);
                    updatedRoomRepository.save(currentRoom);
                }
            }
        }
    }


    public Map<String, Object> generateVideoRoomDetailsResponseEntity(RoomDetailsRequestDTO roomDetailsRequest) {

//        Room roomDetails = this.getRoomDetailsWith(roomDetailsRequest.getRoomShortCode());

        UpdatedRoom updatedRoomDetails = this.getUpdatedRoomDetailsWith(roomDetailsRequest.getRoomShortCode());

        if (updatedRoomDetails == null || !updatedRoomDetails.isActive() || updatedRoomDetails.getParticipants().size() != 2) {
            throw new ValidationMessagesException("Room you requested is not valid. Please try to join new room.");
        }

        ArrayList<UpdatedParticipant> participantsList = new ArrayList<>(updatedRoomDetails.getParticipants());

        Map<String, Object> responseData = new HashMap<>();

        UpdatedParticipant firstParticipant = participantsList.get(0);
        UpdatedParticipant secondParticipant = participantsList.get(1);

        System.out.println("first participant data is :" + firstParticipant.getAuthenticatedUser().getParticipantId());
        System.out.println("Second participant data is :" + secondParticipant.getAuthenticatedUser().getParticipantId());

        responseData.put("roomID", updatedRoomDetails.getRoomId());

        processUpdatedParticipantsJWTTokenData(roomDetailsRequest, updatedRoomDetails, firstParticipant, secondParticipant, responseData);

        return responseData;
    }




    public void processUpdatedParticipantsJWTTokenData(RoomDetailsRequestDTO roomDetailsRequest, UpdatedRoom roomDetails, UpdatedParticipant firstParticipant, UpdatedParticipant secondParticipant, Map<String, Object> responseData) {

        if ((roomDetailsRequest.getIsMHP() == 1 && firstParticipant.getAuthenticatedUser().getUserRole().equals(Participant.UserRole.MHP)) || (roomDetailsRequest.getIsMHP() != 1 && firstParticipant.getAuthenticatedUser().getUserRole().equals(Participant.UserRole.PATIENT))) {
            responseData.put("jwtToken", firstParticipant.getJwtToken());
            responseData.put("userName", firstParticipant.getAuthenticatedUser().getUserName());
            responseData.put("participatingUserName", secondParticipant.getAuthenticatedUser().getUserName());
            responseData.put("userId", firstParticipant.getAuthenticatedUser().getParticipantId());
            responseData.put("participatingUserId", secondParticipant.getAuthenticatedUser().getParticipantId());
            responseData.put("jwtURL", videoCallingUtilities.generateJWTURL(roomDetails.getRoomId(), firstParticipant.getJwtToken()));
        } else {
            responseData.put("jwtToken", secondParticipant.getJwtToken());
            responseData.put("userName", secondParticipant.getAuthenticatedUser().getUserName());
            responseData.put("participatingUserName", firstParticipant.getAuthenticatedUser().getUserName());
            responseData.put("userId", secondParticipant.getAuthenticatedUser().getParticipantId());
            responseData.put("participatingUserId", firstParticipant.getAuthenticatedUser().getParticipantId());
            responseData.put("jwtURL", videoCallingUtilities.generateJWTURL(roomDetails.getRoomId(), secondParticipant.getJwtToken()));
        }

        if (roomDetailsRequest.getIsMHP() != 1) {
            String clientID = firstParticipant.getAuthenticatedUser().getUserRole().equals(Participant.UserRole.MHP) ? firstParticipant.getAuthenticatedUser().getParticipantId() : secondParticipant.getAuthenticatedUser().getParticipantId();
            responseData.put("clientID", clientID);
            responseData.put("MHPRegistrationNumber", "MHP1234NHNOPA");
        }
    }




//    public void processParticipantsJWTTokenData(RoomDetailsRequestDTO roomDetailsRequest, Room roomDetails, Participant firstParticipant, Participant secondParticipant, Map<String, Object> responseData) {
//
//        if ((roomDetailsRequest.getIsMHP() == 1 && firstParticipant.getUserRole().equals(Participant.UserRole.MHP)) || (roomDetailsRequest.getIsMHP() != 1 && firstParticipant.getUserRole().equals(Participant.UserRole.PATIENT))) {
//            responseData.put("jwtToken", firstParticipant.getJwtToken());
//            responseData.put("userName", firstParticipant.getUserName());
//            responseData.put("participatingUserName", secondParticipant.getUserName());
//            responseData.put("userId", firstParticipant.getParticipantId());
//            responseData.put("participatingUserId", secondParticipant.getParticipantId());
//            responseData.put("jwtURL", videoCallingUtilities.generateJWTURL(roomDetails.getRoomId(), firstParticipant.getJwtToken()));
//        } else {
//            responseData.put("jwtToken", secondParticipant.getJwtToken());
//            responseData.put("userName", secondParticipant.getUserName());
//            responseData.put("participatingUserName", firstParticipant.getUserName());
//            responseData.put("userId", secondParticipant.getParticipantId());
//            responseData.put("participatingUserId", firstParticipant.getParticipantId());
//            responseData.put("jwtURL", videoCallingUtilities.generateJWTURL(roomDetails.getRoomId(), secondParticipant.getJwtToken()));
//        }
//
//        if (roomDetailsRequest.getIsMHP() != 1) {
//            String clientID = firstParticipant.getUserRole().equals(Participant.UserRole.MHP) ? firstParticipant.getParticipantId() : secondParticipant.getParticipantId();
//            responseData.put("clientID", clientID);
//            responseData.put("MHPRegistrationNumber", "MHP1234NHNOPA");
//        }
//    }
}
