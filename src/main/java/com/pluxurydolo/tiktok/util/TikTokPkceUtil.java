package com.pluxurydolo.tiktok.util;

import com.pluxurydolo.tiktok.exception.TikTokCodeChallengeException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TikTokPkceUtil {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public String generateCodeVerifier() {
        byte[] code = new byte[32];
        SECURE_RANDOM.nextBytes(code);

        return Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(code);
    }

    public String generateCodeChallenge(String codeVerifier) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] verifierBytes = codeVerifier.getBytes(UTF_8);
            byte[] hash = messageDigest.digest(verifierBytes);

            return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(hash);
        } catch (NoSuchAlgorithmException exception) {
            throw new TikTokCodeChallengeException(exception);
        }
    }
}
