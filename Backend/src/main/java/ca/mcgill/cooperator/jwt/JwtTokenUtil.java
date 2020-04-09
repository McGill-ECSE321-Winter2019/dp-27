package ca.mcgill.cooperator.jwt;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

    // 3 hours
    public static final long JWT_TOKEN_VALIDITY = 3 * 60 * 60;

    /**
     * Retrieves the subject of a token
     *
     * @param token
     * @return subject
     */
    public String getSubjectFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Retrieves expiration date from a token
     *
     * @param token
     * @return expiration date as Date
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Retrieves a specific Claim from a token using the passed-in function claimsResolver
     *
     * @param <T>
     * @param token
     * @param claimsResolver
     * @return claims
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Retrieves all Claims from a token. Requires the secret that the token was signed with.
     *
     * @param token
     * @return all Claims
     */
    private Claims getAllClaimsFromToken(String token) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        String secret = dotenv.get("JWT_SECRET");
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * Checks if a token has expired
     *
     * @param token
     * @return true if token has expired
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Generates a token for the given user
     *
     * @param userDetails
     * @return encrypted token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    /**
     * while creating the token - 1. Define claims of the token, like Issuer, Expiration, Subject,
     * and the ID 2. Sign the JWT using the HS512 algorithm and secret key 3. According to JWS
     * Compact
     * Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
     * compaction of the JWT to a URL-safe string
     *
     * @param claims
     * @param subject
     * @return encrypted token
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        String secret = dotenv.get("JWT_SECRET");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * Validates the given token against the given user information
     *
     * @param token
     * @param userDetails
     * @return true if token is valid
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String email = getSubjectFromToken(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
