package com.example.ehrc.telemanas.Model.NewStructuredModal;

import com.example.ehrc.telemanas.UserRepository.NewRepository.VideoConsultationIDProofRepository;
import com.example.ehrc.telemanas.UserRepository.NewRepository.VideoConsultationRoomRepository;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

//import java.util.HashMap;
import java.util.Map;

@Service
public class NewUserIdentityService {

    @Autowired
    private VideoConsultationIDProofRepository videoConsultationIDProofRepository;

    @Autowired
    private VideoConsultationRoomRepository videoConsultationRoomRepository;

    @Autowired
    private VideoCallingUtilities videoCallingUtilities;


    @Transactional
    public ResponseEntity<Map<String, Object>> uploadFile(MultipartFile file, String roomShortCode) {

        if(videoCallingUtilities.getRoomActivationCheckResponseMap(roomShortCode) != null)
            return videoCallingUtilities.getRoomActivationCheckResponseMap(roomShortCode);

        if (file.isEmpty()) {
            return videoCallingUtilities.getErrorResponseMessageEntity("File is empty.", HttpStatus.BAD_REQUEST);
        }
        try {

            VideoConsultationRoom requestedRoom = videoConsultationRoomRepository.findRoomDetailsWithActiveStatus(roomShortCode);

            if(requestedRoom.getVideoCallData().getVideoCallIdProof() != null)
                return videoCallingUtilities.getErrorResponseMessageEntity("ID Proof for current room is already uploaded.", HttpStatus.BAD_REQUEST);

            VideoConsultationIDProof userIDProof = new VideoConsultationIDProof(file.getBytes(), file.getOriginalFilename(), file.getContentType());
            videoConsultationIDProofRepository.save(userIDProof);

            requestedRoom.getVideoCallData().setVideoCallIdProof(userIDProof);
            videoConsultationRoomRepository.save(requestedRoom);

            Map<String, Object> response = videoCallingUtilities.getSuccessResponseMap();
            response.put("message", "File uploaded successfully: " + file.getOriginalFilename());
            response.put("fileSize", String.valueOf(file.getSize())); // Adding the file size as an additional key
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return videoCallingUtilities.getErrorResponseMessageEntity("Failed to upload file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
