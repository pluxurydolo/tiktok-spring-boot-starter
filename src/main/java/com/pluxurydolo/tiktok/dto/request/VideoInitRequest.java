package com.pluxurydolo.tiktok.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VideoInitRequest(
    @JsonProperty("post_info") PostInfo postInfo,
    @JsonProperty("source_info") SourceInfo sourceInfo
) {
}
