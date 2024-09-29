package com.example.ehrc.telemanas.Model.NewStructuredModal;

import com.example.ehrc.telemanas.DTO.NewStructuredDTO.VerifyUserIdentityDTO;
import com.example.ehrc.telemanas.Model.UserIdentity;
import com.example.ehrc.telemanas.UserRepository.NewRepository.VideoConsultationIDProofRepository;
import com.example.ehrc.telemanas.UserRepository.NewRepository.VideoConsultationRoomRepository;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

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

        if (videoCallingUtilities.getRoomActivationCheckResponseMap(roomShortCode) != null)
            return videoCallingUtilities.getRoomActivationCheckResponseMap(roomShortCode);

        if (file.isEmpty()) {
            return videoCallingUtilities.getErrorResponseMessageEntity("File is empty.", HttpStatus.BAD_REQUEST);
        }
        try {

            VideoConsultationRoom requestedRoom = videoConsultationRoomRepository.findRoomDetailsWithActiveStatus(roomShortCode);

            if (requestedRoom.getVideoCallData().getVideoCallIdProof() != null)
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


    @Transactional
    public ResponseEntity<Map<String, Object>> verifyUserIdentity(VerifyUserIdentityDTO verifyUserIdentityDTO) {

        if (videoCallingUtilities.getRoomActivationCheckResponseMap(verifyUserIdentityDTO.getRoomShortCode()) != null)
            return videoCallingUtilities.getRoomActivationCheckResponseMap(verifyUserIdentityDTO.getRoomShortCode());

        VideoConsultationRoom requestedRoom = videoConsultationRoomRepository.findRoomDetailsWithActiveStatus(verifyUserIdentityDTO.getRoomShortCode());

        if (requestedRoom != null && requestedRoom.getVideoCallData().getVideoCallIdProof() == null)
            return videoCallingUtilities.getGlobalErrorResponseMessageEntity("No ID Proof exists for given room. Please try uploading the ID Proof first.");

        VideoConsultationCall videoCall = requestedRoom.getVideoCallData();
        videoCall.setIsIDProofStatus((verifyUserIdentityDTO.getIsVerified() == 1) ? VideoConsultationIDProof.VideoCallIDProofStatus.YES : VideoConsultationIDProof.VideoCallIDProofStatus.NO);

        videoConsultationRoomRepository.save(requestedRoom);

        return new ResponseEntity<>(videoCallingUtilities.getSuccessResponseMap(), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> fetchUserIdentityFlag(String roomShortCode) {

        VideoConsultationRoom requestedRoom = videoConsultationRoomRepository.findRoomDetailsWithActiveStatus(roomShortCode);

        Map<String, Object> responseMap = videoCallingUtilities.getSuccessResponseMap();
        if (requestedRoom != null && requestedRoom.getVideoCallData().getVideoCallIdProof() != null)
            responseMap.put("isUploaded", true);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> getFile(String roomShortCode) {

        System.out.println("API hit : " + roomShortCode);

        if (videoCallingUtilities.getRoomActivationCheckResponseMap(roomShortCode) != null)
            return videoCallingUtilities.getRoomActivationCheckResponseMap(roomShortCode);

        VideoConsultationRoom requestedRoom = videoConsultationRoomRepository.findRoomDetailsWithActiveStatus(roomShortCode);

        if (requestedRoom != null && requestedRoom.getVideoCallData().getVideoCallIdProof() == null)
            return videoCallingUtilities.getGlobalErrorResponseMessageEntity("No ID Proof exists for given room. Please try uploading the ID Proof first.");

        VideoConsultationCall callData = requestedRoom.getVideoCallData();
        VideoConsultationIDProof IDProofData = callData.getVideoCallIdProof();

        System.out.println("fileEntityOpt : " + IDProofData);
        if (IDProofData != null) {
            // Create a response map
            Map<String, Object> response = videoCallingUtilities.getSuccessResponseMap();
            response.put("fileName", IDProofData.getFileName());
            response.put("fileData", IDProofData.getFileData());
            // Assuming this is a byte array
            response.put("fileType", IDProofData.getFileType());
            response.put("message", "File retrieved successfully");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON) // Set content type to JSON
                    .body(response);

//            return ResponseEntity.ok()
//                    .header("Content-Disposition", "attachment; filename=\"" + IDProofData.getFileName() + "\"")
//                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                    .body(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
