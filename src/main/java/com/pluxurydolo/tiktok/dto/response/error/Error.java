package com.pluxurydolo.tiktok.dto.response.error;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Error(
    @JsonProperty("code") String code,
    @JsonProperty("message") String message
) {
}
