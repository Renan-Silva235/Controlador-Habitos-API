package com.habito_controle.Security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.habito_controle.User.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;
import java.util.Optional;
import java.util.UUID;

@Service
public class JwtService {
    private final Algorithm algorithm;
    private final long accessExpirationMinutes;
    private final long refreshExpirationDays;

    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.access-expiration-minutes}") long accessExpirationMinutes,
                      @Value("${jwt.refresh-expiration-days}") long refreshExpirationDays){
        this.algorithm = Algorithm.HMAC256(secret);
        this.accessExpirationMinutes = accessExpirationMinutes;
        this.refreshExpirationDays = refreshExpirationDays;
    }

    public String generateAccessToken(User user){
        Instant now = Instant.now();

        return JWT.create()
                .withSubject(user.getId().toString())
                .withClaim("type", "access")
                .withJWTId(UUID.randomUUID().toString())
                .withIssuedAt(now)
                .withExpiresAt(now.plus(accessExpirationMinutes, ChronoUnit.MINUTES))
                .sign(algorithm);
    }

    public String generateRefreshToken(User user) {
        Instant now = Instant.now();

        return JWT.create()
                .withSubject(user.getId().toString())
                .withClaim("type", "refresh")
                .withJWTId(UUID.randomUUID().toString())
                .withIssuedAt(now)
                .withExpiresAt(now.plus(refreshExpirationDays, ChronoUnit.DAYS))
                .sign(algorithm);
    }

    public Optional<UUID> validateAccessToken(String token){
        try {
            DecodedJWT decoded = JWT.require(algorithm)
                    .withClaim("type", "access")
                    .build()
                    .verify(token);

            return Optional.of(UUID.fromString(decoded.getSubject()));
        }catch (JWTVerificationException | IllegalArgumentException error){
            return Optional.empty();
        }
    }

    public String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algoritmo SHA-256 indisponível.", e);
        }
    }
}
