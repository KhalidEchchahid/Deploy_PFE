package com.studentpublication.dto;

import org.springframework.web.multipart.MultipartFile;

public record PublicationRequest(
        Long userId,
        String tags ,
        MultipartFile pdfFile,
        String title,
        String description
) {
}
