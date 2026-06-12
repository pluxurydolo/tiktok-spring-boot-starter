package com.pluxurydolo.tiktok.dto.response.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StatusData(

    @JsonProperty("publish_id")
    String publishId,

    @JsonProperty("status")
    String status,

    @JsonProperty("fail_reason")
    String failReason,

    @JsonProperty("video_id")
    String videoId
) {
}
