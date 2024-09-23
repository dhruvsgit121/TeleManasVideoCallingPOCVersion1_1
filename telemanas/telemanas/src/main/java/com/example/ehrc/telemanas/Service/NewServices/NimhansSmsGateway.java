package com.example.ehrc.telemanas.Service.NewServices;

import lombok.extern.slf4j.Slf4j;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

//public class NimhansSmsGateway {
//}


//package com.ehrc.namanbackend.utility;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NimhansSmsGateway {

    public static boolean sendSms(String toNumber, String messageContent,String templateId) {
        boolean response = false;
        URL url;
        try {

            url = new URL("https://msgn.mtalkz.com/api");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Accept-Language", "en-US");
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");

            JSONObject input = new JSONObject();

            input.put("apikey", "BtwpIyRRgJJxLmiO");
            input.put("senderid", "NIMHNS");
            input.put("number", toNumber);
            input.put("message", messageContent);
            input.put("format", "json");
            input.put("template_id", templateId);

            log.info("Sms API Payload " + input);

            OutputStream os = conn.getOutputStream();
            os.write(input.toString().getBytes());
            os.flush();

            if (conn.getResponseCode() != 200) {

                log.info("Connection Failed : HTTP error code : " + conn.getResponseCode());
                response = false;
            } else {
                Scanner sc = new Scanner(conn.getInputStream());
                String output = "";
                while (sc.hasNext()) {
                    output += sc.nextLine();
                }

                JSONObject jObject = new JSONObject(output);

                log.info("Send Sms Response " + jObject);

                conn.disconnect();
                sc.close();

                response = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return response;

    }

}
