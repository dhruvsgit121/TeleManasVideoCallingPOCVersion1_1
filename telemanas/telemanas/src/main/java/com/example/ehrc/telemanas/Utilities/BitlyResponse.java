package com.example.ehrc.telemanas.Utilities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BitlyResponse {

    @JsonProperty("link")
    private String link;

    public String getLink() {
        return link;
    }

//    @JsonProperty("link")
//    private String link;
//
//    // Getter
//    public String getLink() {
//        return link;
//    }


}
