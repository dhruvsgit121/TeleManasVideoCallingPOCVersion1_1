package com.example.ehrc.telemanas.Utilities;

//import com.fasterxml.jackson.annotation.JsonProperty;

public class BitlyRequest {

    private String long_url;

    public BitlyRequest(String longUrl) {
        this.long_url = longUrl;
    }

    public String getLongUrl() {
        return long_url;
    }

    public void setLongUrl(String longUrl) {
        this.long_url = longUrl;
    }

//    private String long_url;
//
//    public BitlyRequest(String longUrl) {
//        this.long_url = longUrl;
//    }
//
//    // Getter and Setter
//    public String getLongUrl() {
//        return long_url;
//    }
//
//    public void setLongUrl(String longUrl) {
//        this.long_url = longUrl;
//    }
}
