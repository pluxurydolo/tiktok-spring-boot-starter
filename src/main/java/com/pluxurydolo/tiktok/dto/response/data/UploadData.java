package com.pluxurydolo.tiktok.dto.response.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UploadData(
    @JsonProperty("publish_id") String publishId,
    @JsonProperty("status") String status
) {
}
