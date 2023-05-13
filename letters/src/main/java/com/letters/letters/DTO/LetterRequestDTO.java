package com.letters.letters.DTO;

import org.springframework.web.multipart.MultipartFile;

public record LetterRequestDTO(

         Long studentId,


         MultipartFile reportCard,

        Long professorId ,

        String letterType,

       String cause
) {
}
