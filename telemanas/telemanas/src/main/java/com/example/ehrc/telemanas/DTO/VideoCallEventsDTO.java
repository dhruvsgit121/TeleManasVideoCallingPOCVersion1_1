package com.example.ehrc.telemanas.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class VideoCallEventsDTO {

    @NotNull(message = "Event Description cannot be null. Please enter Event Description to proceed.")
    @NotBlank(message = "Event Description cannot be blank. Please enter Event Description to proceed.")
    private String eventDescription;

    @NotNull(message = "Room Short Code cannot be null. Please enter Room Short Code to proceed.")
    @NotBlank(message = "Room Short Code cannot be blank. Please enter Room Short Code to proceed.")
    private String roomShortCode;

    private int isMHP;

}