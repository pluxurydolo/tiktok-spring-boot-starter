package com.pluxurydolo.tiktok.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostInfo(

    @JsonProperty("title")
    String title,

    @JsonProperty("privacy_level")
    String privacyLevel,

    @JsonProperty("disable_comment")
    boolean disableComment,

    @JsonProperty("disable_duet")
    boolean disableDuet,

    @JsonProperty("disable_stitch")
    boolean disableStitch
) {
}
