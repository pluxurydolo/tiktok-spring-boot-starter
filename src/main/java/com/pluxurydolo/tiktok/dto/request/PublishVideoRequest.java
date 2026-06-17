package com.pluxurydolo.tiktok.dto.request;

public record PublishVideoRequest(
    byte[] video,
    String title
) {
}
