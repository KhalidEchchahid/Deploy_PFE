package com.studentpublication.dto;

import org.springframework.web.multipart.MultipartFile;

public record PublicationUpdate(Long userId,
                                String tags,
                                String title,
                                String description) {
}
