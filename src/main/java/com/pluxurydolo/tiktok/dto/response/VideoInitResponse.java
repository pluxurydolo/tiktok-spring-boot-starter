package com.pluxurydolo.tiktok.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pluxurydolo.tiktok.dto.response.data.VideoData;
import com.pluxurydolo.tiktok.dto.response.error.Error;

public record VideoInitResponse(

    @JsonProperty("data")
    VideoData data,

    @JsonProperty("error")
    Error error
) {
}
