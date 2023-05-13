package com.professorarticle.dto;

import org.springframework.web.multipart.MultipartFile;

public record PublicationRequest(
        Long userId,
        String category ,
        MultipartFile imageFile,
        String title,
        String content
) {
}