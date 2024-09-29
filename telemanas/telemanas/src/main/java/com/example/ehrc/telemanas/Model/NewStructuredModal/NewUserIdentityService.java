package com.example.ehrc.telemanas.Model.NewStructuredModal;

import com.example.ehrc.telemanas.UserRepository.NewRepository.VideoConsultationIDProofRepository;
import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

//import java.util.HashMap;
import java.util.Map;

@Service
public class NewUserIdentityService {

    @Autowired
    private VideoConsultationIDProofRepository videoConsultationIDProofRepository;

    @Autowired
    private VideoCallingUtilities videoCallingUtilities;

    public ResponseEntity<Map<String, Object>> uploadFile(MultipartFile file, String roomShortCode) {

        if(videoCallingUtilities.getRoomActivationCheckResponseMap(roomShortCode) != null)
            return videoCallingUtilities.getRoomActivationCheckResponseMap(roomShortCode);

        if (file.isEmpty()) {
            return videoCallingUtilities.getErrorResponseMessageEntity("File is empty.", HttpStatus.BAD_REQUEST);
        }
        try {
            VideoConsultationIDProof userIDProof = new VideoConsultationIDProof(file.getBytes(), file.getOriginalFilename(), file.getContentType());
            videoConsultationIDProofRepository.save(userIDProof);

            Map<String, Object> response = videoCallingUtilities.getSuccessResponseMap();
            response.put("message", "File uploaded successfully: " + file.getOriginalFilename());
            response.put("fileSize", String.valueOf(file.getSize())); // Adding the file size as an additional key
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return videoCallingUtilities.getErrorResponseMessageEntity("Failed to upload file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
