package com.example.ehrc.telemanas.Service;

import com.example.ehrc.telemanas.Utilities.VideoCallingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;


@Service
public class JWTTokenService {

    @Autowired
    private VideoCallingUtilities videoCallingUtilities;

    @Value("${jwt.secret}")
    private String appSecret;

    @Value("${jwt.appID}")
    private String appID;

    @Value("${jwt.jitsiDomain}")
    private String jitsiDomain;

    @Value("${jwt.expirationOffSet}")
    private int expirationOffset;

    @Value("${jwt.jitsiFullDomain}")
    private String jitsiFullDomain;


    public Map<String, Object> generateUserJWTTokenData(String doctorName, String doctorEmail, String doctorPhoneNumber, String patientName, String patientEmail, String patientPhoneNumber, String roomID) {

        String randomDoctorUserID = doctorName;//videoCallingUtilities.generateRandomString(10);
        String randomPatientUserID = patientName;//videoCallingUtilities.generateRandomString(10);
        String randomRoomID = roomID;//videoCallingUtilities.generateRandomString(10);
        String audienceID = appID + ":" + randomRoomID;

        System.out.println("generateUserJWTTokenData called");

        System.out.println("*********************Doctor Details************");
        System.out.println(doctorName);
        System.out.println(doctorEmail);
        System.out.println(doctorPhoneNumber);

        System.out.println("*********************Patient Details************");
        System.out.println(patientName);
        System.out.println(patientEmail);
        System.out.println(patientPhoneNumber);


        Map<String, Object> userTokenData = new HashMap<>();

        String doctorJWTToken = generateDoctorJWTToken(doctorName, doctorEmail, randomDoctorUserID, randomRoomID, audienceID);
        userTokenData.put("doctorJWTToken", doctorJWTToken);
        userTokenData.put("roomID", randomRoomID);
//        String doctorVideoConferencingURL = jitsiFullDomain + randomRoomID + "?jwt=" + doctorJWTToken + jitsiConfigSettings;

        String doctorVideoConferencingURL = jitsiFullDomain + randomRoomID + "?jwt=" + doctorJWTToken;
        userTokenData.put("doctorVideoConferencingURL", doctorVideoConferencingURL);


//        String shortUrl = tinyUrlService.shortenUrl(doctorVideoConferencingURL);
//        System.out.println("Shortent URL is : " + shortUrl);


        String patientJWTToken = generatePatientJWTToken(patientName, patientEmail, randomPatientUserID, randomRoomID, audienceID);
        userTokenData.put("patientJWTToken", patientJWTToken);

//        String patientVideoConferencingURL = jitsiFullDomain + randomRoomID + "?jwt=" + patientJWTToken + jitsiConfigSettings;
        String patientVideoConferencingURL = jitsiFullDomain + randomRoomID + "?jwt=" + patientJWTToken;
        userTokenData.put("patientVideoConferencingURL", patientVideoConferencingURL);

        //smsService.sendTestSms(patientPhoneNumber, patientVideoConferencingURL);

        return userTokenData;
    }


    public String generateDoctorJWTToken(String userName, String userEmailID, String userID, String roomID, String audienceID) {
        return generateJWTToken(userName, userEmailID, userID, roomID, audienceID, true);
    }

    public String generatePatientJWTToken(String userName, String userEmailID, String userID, String roomID, String audienceID) {
        return generateJWTToken(userName, userEmailID, userID, roomID, audienceID, false);
    }


    public String generateJWTToken(String userName, String userEmailID, String userID, String roomID, String audienceID, Boolean isModerator) {

        Map<String, Object> claims = getUserClaims(audienceID, roomID, isModerator);
        claims.put("context", createContext(userName, userID, userEmailID, isModerator));

        byte[] apiKeySecretBytes = appSecret.getBytes(StandardCharsets.UTF_8);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, "HMACSHA256");

        // Use JwtBuilder to construct a JWT token
        String token = Jwts.builder()
                .setClaims(claims).claim("role", "participant")
                .signWith(SignatureAlgorithm.HS256, signingKey).setHeaderParam("typ", "JWT")
                .compact();
        return token;
    }


    public Map<String, Object> getUserClaims(String audienceID, String roomID, boolean isModerator) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("aud", audienceID);
        claims.put("iss", appID);
        claims.put("sub", jitsiDomain);
        claims.put("room", roomID); // Room ID for the Jitsi conference
        claims.put("nbf", videoCallingUtilities.getCurrentTimeStamp()); // Not Before time in seconds
        claims.put("exp", videoCallingUtilities.getExpirationTimeStamp(expirationOffset)); // Expiry time in milliseconds (1 hour)
//        "moderator": true
        //claims.put("moderator", isModerator);
        //claims.put("role", isModerator ? "moderator" : "participant");
        return claims;
    }


    private Map<String, Object> createContext(String username, String userID, String userEmail, Boolean isModerator) {
        Map<String, Object> context = new HashMap<>();
        Map<String, Object> user = new HashMap<>();
        user.put("moderator", isModerator);
        user.put("name", username);
        user.put("id", userID);
        user.put("email", userEmail);
//        user.put("affiliation", isModerator ? "owner" : "member");
//        "affiliation": "owner"
        context.put("user", user);
//        context.put("group", isModerator ? "moderator" : "viewer");
//        roles: ['moderator']  // Assign the moderator role

//        ArrayList<String> roles = new ArrayList<>();
//        roles.add(isModerator ? "moderator" : "participant");
//
//        context.put("affiliation", isModerator ? "owner" : "member");
//        context.put("role", roles);
        return context;
    }


}
