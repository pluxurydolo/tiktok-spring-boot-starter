package com.pluxurydolo.tiktok.dto.response.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VideoData(
    @JsonProperty("publish_id") String publishId,
    @JsonProperty("upload_url") String uploadUrl
) {
}
