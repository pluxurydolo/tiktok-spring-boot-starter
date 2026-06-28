package com.pluxurydolo.tiktok.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SourceInfo(
    @JsonProperty("source") String source,
    @JsonProperty("video_size") Long videoSize,
    @JsonProperty("chunk_size") Long chunkSize,
    @JsonProperty("total_chunk_count") Integer totalChunkCount
) {
}
