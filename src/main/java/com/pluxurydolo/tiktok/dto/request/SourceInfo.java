package com.pluxurydolo.tiktok.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SourceInfo(

    @JsonProperty("source")
    String source,

    @JsonProperty("video_size")
    long videoSize,

    @JsonProperty("chunk_size")
    long chunkSize,

    @JsonProperty("total_chunk_count")
    int totalChunkCount
) {
}
