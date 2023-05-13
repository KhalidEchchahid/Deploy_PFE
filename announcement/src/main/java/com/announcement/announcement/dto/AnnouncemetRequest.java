package com.announcement.announcement.dto;

import jakarta.annotation.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public record AnnouncemetRequest(
        String title,
        MultipartFile file,
        String description,
        Long userId,
        String creator,
        String semester
) {}
