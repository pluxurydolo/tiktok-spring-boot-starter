package com.pluxurydolo.tiktok.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pluxurydolo.tiktok.dto.response.data.UploadData;
import com.pluxurydolo.tiktok.dto.response.error.Error;

public record VideoUploadResponse(

    @JsonProperty("data")
    UploadData data,

    @JsonProperty("error")
    Error error
) {
}
