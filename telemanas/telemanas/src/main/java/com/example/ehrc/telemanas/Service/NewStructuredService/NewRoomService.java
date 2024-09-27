package com.example.ehrc.telemanas.Service.NewStructuredService;


import com.example.ehrc.telemanas.DTO.NewStructuredDTO.AuthenticateUserDTO;
import com.example.ehrc.telemanas.DTO.RoomCreationDataDTO;
import com.example.ehrc.telemanas.Model.NewStructuredModal.EYDataModel.MHPDataModal;
import com.example.ehrc.telemanas.Model.NewStructuredModal.EYDataModel.PatientDataModal;
import com.example.ehrc.telemanas.Model.NewStructuredModal.VideoConsultationParticipant;
import com.example.ehrc.telemanas.Model.NewStructuredModal.VideoConsultationRoom;
import com.example.ehrc.telemanas.Model.NewStructuredModal.VideoConsultationStatusMaster;
import com.example.ehrc.telemanas.UserRepository.NewRepository.VideoConsultationRoomRepository;
import com.example.ehrc.telemanas.UserRepository.NewRepository.VideoConsultationStatusMasterRepository;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class NewRoomService {

    @Autowired
    private VideoCallingUtilities videoCallingUtilities;

    @Autowired
    private VideoConsultationRoomRepository videoConsultationRoomRepository;

    @Autowired
    private VideoConsultationStatusMasterRepository videoConsultationStatusMasterRepository;

    @Autowired
    private JWTTokenService jwtTokenService;

    @Value("${jwt.expirationOffSet}")
    private int expirationOffset;

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
                    currentRoom.setActive(false);
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

        //Adding the Participants...
        List<VideoConsultationParticipant> participantsList = new ArrayList<>();
        participantsList.add(mhpParticipant);
        participantsList.add(patientParticipant);

        return participantsList;
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

        videoConsultationRoomRepository.save(videoConsultationRoom);

        Map<String, Object> responseMap = videoCallingUtilities.getSuccessResponseMap();
        responseMap.put("roomCode", videoConsultationRoom.getRoomShortCode());

        return new ResponseEntity(responseMap, HttpStatus.OK);
    }


    public void setRoomActivationDeactivationFlag(VideoConsultationRoom videoConsultationRoom, boolean isActive){
        VideoConsultationStatusMaster statusActiveFlagData = videoConsultationStatusMasterRepository.findStatusWithStatusName(isActive ? "active" : "inactive");
        videoConsultationRoom.setActive(isActive);
        videoConsultationRoom.setStatus(statusActiveFlagData);
    }
}
