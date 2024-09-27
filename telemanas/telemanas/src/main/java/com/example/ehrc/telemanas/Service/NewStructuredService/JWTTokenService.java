package com.example.ehrc.telemanas.Service.NewStructuredService;

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


    public String generateJWTToken(String userName, String userID, String userEmailID, String roomID, boolean isModerator) {
        String audienceID = appID + ":" + roomID;
        return generateJWTToken(userName, userEmailID, userID, roomID, audienceID, isModerator);
    }

    private String generateJWTToken(String userName, String userEmailID, String userID, String roomID, String audienceID, Boolean isModerator) {

        Map<String, Object> claims = getUserClaims(audienceID, roomID, isModerator);
        claims.put("context", createContext(userName, userID, userEmailID, isModerator));

        byte[] apiKeySecretBytes = appSecret.getBytes(StandardCharsets.UTF_8);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, "HMACSHA256");

        // Use JwtBuilder to construct a JWT token
        String token = Jwts.builder()
                .setClaims(claims)
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
        return claims;
    }

    private Map<String, Object> createContext(String username, String userID, String userEmail, Boolean isModerator) {
        Map<String, Object> context = new HashMap<>();
        Map<String, Object> user = new HashMap<>();
        user.put("name", username);
        user.put("id", userID);
        user.put("email", userEmail);
        user.put("affiliation", isModerator ? "owner" : "member");
        context.put("user", user);
        return context;
    }
}
