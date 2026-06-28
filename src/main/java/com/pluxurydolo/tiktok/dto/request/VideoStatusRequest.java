package com.pluxurydolo.tiktok.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VideoStatusRequest(
    @JsonProperty("publish_id") String publishId
) {
}
