package com.letters.letters.DTO;

import org.springframework.web.multipart.MultipartFile;

public record LetterDTO(
        String message ,
        MultipartFile pdfFile
) {
}
