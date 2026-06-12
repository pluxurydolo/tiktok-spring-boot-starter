package com.pluxurydolo.tiktok.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pluxurydolo.tiktok.dto.response.data.StatusData;
import com.pluxurydolo.tiktok.dto.response.error.Error;

public record VideoStatusResponse(

    @JsonProperty("data")
    StatusData data,

    @JsonProperty("error")
    Error error
) {
    public boolean isSuccess() { return data != null && "SUCCESS".equals(data.status()); } // TODO удалить
}
