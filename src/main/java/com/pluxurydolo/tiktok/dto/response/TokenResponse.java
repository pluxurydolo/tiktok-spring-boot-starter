package com.pluxurydolo.tiktok.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponse(
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("expires_in") Integer expiresIn,
    @JsonProperty("refresh_token") String refreshToken,
    @JsonProperty("refresh_expires_in") Integer refreshExpiresIn,
    @JsonProperty("open_id") String openId,
    @JsonProperty("scope") String scope,
    @JsonProperty("token_type") String tokenType,
    @JsonProperty("error") String error,
    @JsonProperty("error_description") String errorDescription
) {
}
