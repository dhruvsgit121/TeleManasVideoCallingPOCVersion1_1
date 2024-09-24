package com.example.ehrc.telemanas.Service;

import com.example.ehrc.telemanas.CustomException.ValidationMessagesException;
import com.example.ehrc.telemanas.DTO.*;
import com.example.ehrc.telemanas.Model.EYDataModel.MHPDataModal;
import com.example.ehrc.telemanas.Model.EYDataModel.PatientDataModal;
import com.example.ehrc.telemanas.Model.UpdatedModels.AuthenticatedUser;
import com.example.ehrc.telemanas.Model.UpdatedModels.Participant;
import com.example.ehrc.telemanas.Model.UpdatedModels.Room;
import com.example.ehrc.telemanas.Service.NewServices.UpdatedParticipantService;
import com.example.ehrc.telemanas.UserRepository.AuthenticatedUserRepository;
import com.example.ehrc.telemanas.UserRepository.ParticipantRepository;
import com.example.ehrc.telemanas.UserRepository.RoomRepository;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AuthenticatedUserRepository authenticatedUserRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private SSEService sseService;

    @Autowired
    private UpdatedParticipantService participantService;

    private final JWTTokenService jwtTokenService;

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


    @Autowired
    private EntityManager entityManager;

    @Transactional
    public boolean markConsentForPatient(long serialID) {
        try {
            Room room = entityManager.find(Room.class, serialID);
            if (room != null) {
                room.setPatientConsentProvided(true);
                entityManager.merge(room);
                entityManager.flush();
                return true; // Update successful
            }
        } catch (Exception e) {
            System.out.println("Error updating participant with serial ID : " + serialID + " : " + e.getMessage());
        }
        return false; // Participant not found or error occurred
    }


    public Room saveUpdatedRoom(Room room) {
        return roomRepository.save(room);
    }


    public ResponseEntity<Map<String, Object>> getRoomValidationResponseEntity() {
        return videoCallingUtilities.getErrorResponseMessageEntity("The room you requested is either deactivated or doesn't exist. Please try again with another room short code.", HttpStatus.OK);
    }

    public Room getUpdatedRoomDetailsWith(String shortCode) {
        return roomRepository.findRoomDetailsWith(shortCode);
    }

    public Room getUpdatedActiveRoomDetailsWith(String shortCode) {
        return roomRepository.findRoomDetailsWithActiveStatus(shortCode);
    }

    public List<Room> getUpdatedRoomListWithExpirationdate(LocalDateTime expirationDate) {
        return roomRepository.findRoomListWithExpirationDate(expirationDate);
    }


    public void savePatientConsentOnJoinVideoCall(RoomDetailsRequestDTO roomDetailsRequest) {
        Room room = roomRepository.findRoomDetailsWith(roomDetailsRequest.getRoomShortCode());

        boolean isConsentProvidedForPatient = (roomDetailsRequest.getIsPatientConsentProvided() == 1);

        if (isConsentProvidedForPatient) {
            boolean isUpdated = markConsentForPatient(room.getSerialId());
            System.out.println("savePatientConsentOnJoinVideoCall called with response : " + isUpdated);
        }
    }


    public void saveIsActiveRoomOnJoinVideoCall(RoomDetailsRequestDTO roomDetailsRequest) {

//        System.out.println("saveIsActiveRoomOnJoinVideoCall called");
        Room room = roomRepository.findRoomDetailsWith(roomDetailsRequest.getRoomShortCode());//participantService.getPatientParticipant(roomDetailsRequest.getRoomShortCode());

        if (room == null) {
            System.out.println("room requested is not valid");
            return;
        }

        System.out.println("saveIsActiveRoomOnJoinVideoCall called in room service");

        System.out.println(roomDetailsRequest.getRoomShortCode());
        System.out.println(roomDetailsRequest.getIsMHP());

        if (room != null && room.getParticipants().size() == 2 && roomDetailsRequest.getIsMHP() != 1) {

            System.out.println("saveIsActiveRoomOnJoinVideoCall called with number of participants " + room.getParticipants().size());
            System.out.println("User is : " + roomDetailsRequest.getIsMHP());

            System.out.println("Entered in if block");

            Participant firstParticipant = room.getParticipants().get(0);
            Participant secondParticipant = room.getParticipants().get(1);

            System.out.println("firstParticipant : " + firstParticipant.getSerialId() + " secondParticipant : " + secondParticipant.getSerialId());

            //Update the Has Joined Room Flag for Patient to TRUE...
            Participant patientParticipantData = firstParticipant.getAuthenticatedUser().getUserRole().equals(AuthenticatedUser.UserRole.PATIENT) ? firstParticipant : secondParticipant;

            //int rowsAffected = participantRepository.setParticipantHasJoinedRoomFlag(patientParticipantData.getSerialId());

//            System.out.println("rowsAffected : " + rowsAffected);

            boolean isUpdated = participantService.markParticipantAsJoined(patientParticipantData.getSerialId());

            System.out.println("isUpdated flag is : " + isUpdated);

            patientParticipantData = participantRepository.getReferenceById(patientParticipantData.getSerialId());
//
//            patientModifiedUser.setHasJoinedRoom(true);
//            participantRepository.save(patientParticipantData);
//
//            roomRepository.save(room);

            System.out.println("****************************Data after modification****************************");
            System.out.println("data after modification is : " + patientParticipantData.getSerialId() + ", has joined the room : " + patientParticipantData.isHasJoinedRoom());
        }
    }


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

        //RoomDetailsRequestDTO roomDetailsRequest = new  RoomDetailsRequestDTO(callStartDTO);

        //System.out.println("authorsation data" + callStartDTO.getBearerToken() + callStartDTO.getLoggedInId());

        Map<String, Object> responseData = videoCallingUtilities.getSuccessResponseMap();

        Room roomData = roomRepository.findRoomDetailsWith(roomDetailsRequest.getRoomShortCode());

        if (roomData == null || roomData.getParticipants().size() != 2) {
            throw new ValidationMessagesException("Room you requested is not valid. Please try to join new room.");
        }

        //Setting the Join Date of the user...
        Participant requestedParticipant = videoCallingUtilities.getRequestedUpdatedUserAsPerRequest(roomData, roomDetailsRequest);
        requestedParticipant.setJoinDate(videoCallingUtilities.getDateTimeWithOffset(0));
        participantRepository.save(requestedParticipant);

        responseData.put("message", "success");
        return new ResponseEntity(responseData, HttpStatus.OK);
    }


    public ResponseEntity<Map<String, Object>> joinRoom(CallStartDTO callStartDTO) {//RoomDetailsRequestDTO roomDetailsRequest) {

        RoomDetailsRequestDTO roomDetailsRequest = new RoomDetailsRequestDTO(callStartDTO);

        System.out.println("authorsation data" + callStartDTO.getBearerToken() + callStartDTO.getLoggedInId());

        Map<String, Object> responseData = videoCallingUtilities.getSuccessResponseMap();

        Room roomData = roomRepository.findRoomDetailsWith(roomDetailsRequest.getRoomShortCode());

        if (roomData == null || roomData.getParticipants().size() != 2) {
            throw new ValidationMessagesException("Room you requested is not valid. Please try to join new room.");
        }

        //Setting the Join Date of the user...
        Participant requestedParticipant = videoCallingUtilities.getRequestedUpdatedUserAsPerRequest(roomData, roomDetailsRequest);
        requestedParticipant.setJoinDate(videoCallingUtilities.getDateTimeWithOffset(0));
        participantRepository.save(requestedParticipant);

        responseData.put("message", "success");
        return new ResponseEntity(responseData, HttpStatus.OK);
    }


    public ResponseEntity<Map<String, Object>> deactivateRequestedRoom(String roomShortCode) {

        Room requestedRoom = roomRepository.findRoomDetailsWith(roomShortCode);

        if (requestedRoom == null)
            return videoCallingUtilities.getErrorResponseMessageEntity("Room you requested does not exists.", HttpStatus.SEE_OTHER);

        if (!requestedRoom.isActive())
            return videoCallingUtilities.getErrorResponseMessageEntity("Room you requested is already expired.", HttpStatus.SEE_OTHER);

        requestedRoom.setActive(false);
        roomRepository.save(requestedRoom);

        return new ResponseEntity<>(videoCallingUtilities.getSuccessResponseMap(), HttpStatus.OK);
    }


    public ResponseEntity<Map<String, Object>> exitRoom(RoomDetailsRequestDTO roomDetailsRequest) {
//        public ResponseEntity<Map<String, Object>> exitRoom(CallStartDTO callStartDTO){

        Map<String, Object> responseData = videoCallingUtilities.getSuccessResponseMap();

        //RoomDetailsRequestDTO roomDetailsRequest = new  RoomDetailsRequestDTO(callStartDTO);


        Room roomData = roomRepository.findRoomDetailsWith(roomDetailsRequest.getRoomShortCode());


//        ArrayList<Long> participantsList = new ArrayList(participantService.getParticipantsListWith(roomDetailsRequest.getRoomShortCode()));

        if (roomData == null || roomData.getParticipants().size() != 2) {
            throw new ValidationMessagesException("Room you requested is not valid. Please try to join new room.");
        }


        //Setting the Join Date of the user...
        Participant requestedParticipant = videoCallingUtilities.getRequestedUpdatedUserAsPerRequest(roomData, roomDetailsRequest);
        requestedParticipant.setLeftDate(videoCallingUtilities.getDateTimeWithOffset(0));
        participantRepository.save(requestedParticipant);

        responseData.put("message", "success");
        return new ResponseEntity(responseData, HttpStatus.OK);


    }


    public ResponseEntity<Map<String, Object>> exitRoom(CallStartDTO callStartDTO) {

        Map<String, Object> responseData = videoCallingUtilities.getSuccessResponseMap();

        RoomDetailsRequestDTO roomDetailsRequest = new RoomDetailsRequestDTO(callStartDTO);


        Room roomData = roomRepository.findRoomDetailsWith(callStartDTO.getRoomShortCode());


//        ArrayList<Long> participantsList = new ArrayList(participantService.getParticipantsListWith(roomDetailsRequest.getRoomShortCode()));

        if (roomData == null || roomData.getParticipants().size() != 2) {
            throw new ValidationMessagesException("Room you requested is not valid. Please try to join new room.");
        }


        //Setting the Join Date of the user...
        Participant requestedParticipant = videoCallingUtilities.getRequestedUpdatedUserAsPerRequest(roomData, roomDetailsRequest);
        requestedParticipant.setLeftDate(videoCallingUtilities.getDateTimeWithOffset(0));
        participantRepository.save(requestedParticipant);

        responseData.put("message", "success");
        return new ResponseEntity(responseData, HttpStatus.OK);
    }


    public ResponseEntity<Map<String, Object>> createRoom(AuthenticateUserDTO userDTOData, PatientDataModal patientDataModal, MHPDataModal mhpDataModal, RoomService roomService) {

        LocalDateTime expiryDate = videoCallingUtilities.getDateTimeWithOffset(roomJWTValidityOffSet);

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

        RoomCreationDataDTO roomCreationData = new RoomCreationDataDTO(roomID, roomShortCode, videoID);

        return processNewlyCreatedRoom(userDTOData, patientDataModal, mhpDataModal, jwtTokenService, roomCreationData);
    }


    public ResponseEntity<Map<String, Object>> processNewlyCreatedRoom(AuthenticateUserDTO userDTOData, PatientDataModal patientDataModal, MHPDataModal mhpDataModal, JWTTokenService jwtTokenService, RoomCreationDataDTO roomCreationData) {

        String doctorJWTToken = jwtTokenService.generateJWTToken(mhpDataModal.getMhpName(), userDTOData.getMhpUserName(), "dummyMHPemailid", roomCreationData.getRoomID(), true);
        String patientJWTToken = jwtTokenService.generateJWTToken(patientDataModal.getPatientName(), userDTOData.getTelemanasId(), "dummyPatientemailid", roomCreationData.getRoomID(), false);

        System.out.println("Data before saving : " + mhpDataModal.getMhpName());
        System.out.println("Data before saving : " + patientDataModal.getPatientName());


        RoomCreationUserDTO roomCreationPatientData = new RoomCreationUserDTO(userDTOData.getTelemanasId(), patientDataModal.getPatientName(), patientJWTToken, false, AuthenticatedUser.UserRole.PATIENT, "");
        RoomCreationUserDTO roomCreationMHPData = new RoomCreationUserDTO(userDTOData.getMhpUserName(), mhpDataModal.getMhpName(), doctorJWTToken, true, AuthenticatedUser.UserRole.MHP, mhpDataModal.getRoleDisplayName());

        boolean isDoctorConsentProvided = (userDTOData.getIsDoctorConsentProvided() == 1);

        Room savedRoomData = saveNewRoomData(roomCreationData, roomCreationMHPData, roomCreationPatientData, patientDataModal, isDoctorConsentProvided);

        Map<String, Object> responseMap = videoCallingUtilities.getSuccessResponseMap();
        responseMap.put("roomCode", savedRoomData.getRoomShortCode());

        return new ResponseEntity(responseMap, HttpStatus.OK);
    }


    public Room saveNewRoomData(RoomCreationDataDTO roomCreationData, RoomCreationUserDTO roomCreationMHPData, RoomCreationUserDTO roomCreationPatientData, PatientDataModal patientDataModal, boolean isDoctorConsentProvided) {

        Room roomData = new Room(roomCreationData.getRoomID(), roomCreationData.getVideoID(), videoCallingUtilities.getDateTimeWithOffset(0), videoCallingUtilities.getDateTimeWithOffset(expirationOffset), true, roomCreationData.getRoomShortCode(), isDoctorConsentProvided);

        Participant mhpParticipantUser = new Participant(null, null, roomCreationMHPData.getJwtToken(), roomCreationMHPData.getIsOrganiser(), false);
        Participant patientParticipantUser = new Participant(null, null, roomCreationPatientData.getJwtToken(), roomCreationPatientData.getIsOrganiser(), false);

        //Created The Patient Authenticated User Data Modal...
        AuthenticatedUser patientAuthenticatedUser = authenticatedUserRepository.findAuthenticatedUser(roomCreationPatientData.getParticipantID());
        if (patientAuthenticatedUser == null) {
            patientAuthenticatedUser = new AuthenticatedUser(AuthenticatedUser.UserRole.PATIENT, roomCreationPatientData.getUserName(), roomCreationPatientData.getParticipantID(), patientDataModal.getEncryptedMobileNumber(), roomCreationPatientData.getRoleDisplayName());
            authenticatedUserRepository.save(patientAuthenticatedUser);
        }

        //Created The MHP Authenticated User Data Modal...
        AuthenticatedUser mhpAuthenticatedUser = authenticatedUserRepository.findAuthenticatedUser(roomCreationMHPData.getParticipantID());
        if (mhpAuthenticatedUser == null) {
            mhpAuthenticatedUser = new AuthenticatedUser(AuthenticatedUser.UserRole.MHP, roomCreationMHPData.getUserName(), roomCreationMHPData.getParticipantID(), "", roomCreationMHPData.getRoleDisplayName());
            authenticatedUserRepository.save(mhpAuthenticatedUser);
        }

        //Deactivate the Currently Active rooms for the Users....
        deactivateActiveRoomsForCurrentUser(mhpAuthenticatedUser, patientAuthenticatedUser);

        //Setting the Authenticated User Data for Patient & MHP...
        mhpParticipantUser.setAuthenticatedUser(mhpAuthenticatedUser);
        patientParticipantUser.setAuthenticatedUser(patientAuthenticatedUser);

        //Adding the Participants Data for Patient & MHP...
        roomData.addParticipant(mhpParticipantUser);
        roomData.addParticipant(patientParticipantUser);

        //Updating Room Data For the for Patient & MHP...
        return roomRepository.save(roomData);
    }


    public void deactivateActiveRoomsForCurrentUser(AuthenticatedUser mhpUser, AuthenticatedUser patientUser) {

        List<Room> activeRoomsList = roomRepository.findAllActiveRoomLists();

        for (Room room : activeRoomsList) {

            ArrayList<Participant> participants = new ArrayList<>(room.getParticipants());

            if (participants.size() == 2) {

                Participant firstParticipant = participants.get(0);
                Participant secondParticipant = participants.get(1);

                String firstParticipantUserID = firstParticipant.getAuthenticatedUser().getParticipantId();
                String secondParticipantUserID = secondParticipant.getAuthenticatedUser().getParticipantId();

                if ((firstParticipantUserID.equals(mhpUser.getParticipantId()) && secondParticipantUserID.equals(patientUser.getParticipantId()) || ((firstParticipantUserID.equals(patientUser.getParticipantId()) && secondParticipantUserID.equals(mhpUser.getParticipantId()))))) {
                    Room currentRoom = roomRepository.getReferenceById(room.getSerialId());
                    currentRoom.setActive(false);
                    roomRepository.save(currentRoom);
                }
            }
        }
    }


    public Map<String, Object> generateVideoRoomDetailsResponseEntity(RoomDetailsRequestDTO roomDetailsRequest) {

        Room roomDetails = this.getUpdatedRoomDetailsWith(roomDetailsRequest.getRoomShortCode());

        if (roomDetails == null || !roomDetails.isActive() || roomDetails.getParticipants().size() != 2) {
            throw new ValidationMessagesException("Room you requested is not valid. Please try to join new room.");
        }

        ArrayList<Participant> participantsList = new ArrayList<>(roomDetails.getParticipants());

        Map<String, Object> responseData = new HashMap<>();

        Participant firstParticipant = participantsList.get(0);
        Participant secondParticipant = participantsList.get(1);

        System.out.println("first participant data is :" + firstParticipant.getAuthenticatedUser().getParticipantId());
        System.out.println("Second participant data is :" + secondParticipant.getAuthenticatedUser().getParticipantId());

        responseData.put("roomID", roomDetails.getRoomId());

        processUpdatedParticipantsJWTTokenData(roomDetailsRequest, roomDetails, firstParticipant, secondParticipant, responseData);

        return responseData;
    }


    public ResponseEntity<Map<String, Object>> getValidityOfSMSLinkForPatient(String roomShortCode) {

        Room requestedRoom = roomRepository.findRoomDetailsWithActiveStatus(roomShortCode);

        System.out.println("getValidityOfSMSLinkForPatient");

        if (requestedRoom != null) {
            Participant patientParticipant = null;
            for (Participant participant : requestedRoom.getParticipants()) {
                if (participant.isOrganiser() == false) {
                    System.out.println("initalise the patient");
                    patientParticipant = participant;
                    break;
                }
            }

            System.out.println("Participant data is : " + patientParticipant.getJoinDate() + patientParticipant.getLeftDate());

            if (patientParticipant != null && patientParticipant.isHasJoinedRoom() && patientParticipant.getJoinDate() != null && patientParticipant.getLeftDate() != null)
                return videoCallingUtilities.getErrorResponseMessageEntity("The current video call is ended. Please try another room code.", HttpStatus.SEE_OTHER);


            if (patientParticipant != null && patientParticipant.isHasJoinedRoom() && patientParticipant.getJoinDate() != null && patientParticipant.getLeftDate() == null)
                return videoCallingUtilities.getErrorResponseMessageEntity("There is another session going on right now for the current patient. Please end that video call first and then proceed again.", HttpStatus.SEE_OTHER);

        }
        return null;
    }


    public void processUpdatedParticipantsJWTTokenData(RoomDetailsRequestDTO roomDetailsRequest, Room roomDetails, Participant firstParticipant, Participant secondParticipant, Map<String, Object> responseData) {

        Participant mainUserData, participatingUserData;

        if ((roomDetailsRequest.getIsMHP() == 1 && firstParticipant.getAuthenticatedUser().getUserRole().equals(AuthenticatedUser.UserRole.MHP)) || (roomDetailsRequest.getIsMHP() != 1 && firstParticipant.getAuthenticatedUser().getUserRole().equals(AuthenticatedUser.UserRole.PATIENT))) {
            mainUserData = firstParticipant;
            participatingUserData = secondParticipant;
        } else {
            mainUserData = secondParticipant;
            participatingUserData = firstParticipant;
        }

        requestedRoomDetailData(roomDetailsRequest, roomDetails, mainUserData, participatingUserData, responseData);
    }


    public void requestedRoomDetailData(RoomDetailsRequestDTO roomDetailsRequest, Room roomDetails, Participant firstParticipant, Participant secondParticipant, Map<String, Object> responseData) {

        responseData.put("jwtToken", firstParticipant.getJwtToken());
        responseData.put("userName", firstParticipant.getAuthenticatedUser().getUserName());
        responseData.put("participatingUserName", secondParticipant.getAuthenticatedUser().getUserName());
        responseData.put("userId", firstParticipant.getAuthenticatedUser().getParticipantId());
        responseData.put("participatingUserId", secondParticipant.getAuthenticatedUser().getParticipantId());
        responseData.put("jwtURL", videoCallingUtilities.generateJWTURL(roomDetails.getRoomId(), firstParticipant.getJwtToken()));

        if (roomDetailsRequest.getIsMHP() != 1) {

            AuthenticatedUser MHPUser = firstParticipant.getAuthenticatedUser().getUserRole().equals(AuthenticatedUser.UserRole.MHP) ? firstParticipant.getAuthenticatedUser() : secondParticipant.getAuthenticatedUser();

            String clientID = MHPUser.getParticipantId();
            String roleDisplayName = MHPUser.getRoleDisplayName();
            //firstParticipant.getAuthenticatedUser().getUserRole().equals(AuthenticatedUser.UserRole.MHP) ? firstParticipant.getAuthenticatedUser().getParticipantId() : secondParticipant.getAuthenticatedUser().getParticipantId();
            responseData.put("clientID", clientID);
            responseData.put("MHPRegistrationNumber", "MHP1234NHNOPA");
            responseData.put("Designation", roleDisplayName);
        }
    }
}
