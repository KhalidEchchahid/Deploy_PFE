package com.usermanagement.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileService {
     ResponseEntity<?> uploadProfileImage(Long userId, MultipartFile file) throws IOException ;
     ResponseEntity<?> getProfileById(Long id);
}
