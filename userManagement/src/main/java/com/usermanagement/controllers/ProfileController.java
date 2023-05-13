package com.usermanagement.controllers;

import com.usermanagement.services.ProfileService;
import com.usermanagement.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth/profiles")
public class ProfileController {
    private final ProfileService profileService;
    private final UserService userService;
    @PutMapping("/{id}")
    public ResponseEntity<?> uploadImageProfile(@PathVariable Long id, @ModelAttribute MultipartFile Image) throws IOException {
        return profileService.uploadProfileImage(id, Image);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return profileService.getProfileById(id);
    }
    @GetMapping("/students")
    public List<String> getStudentBySemester(@RequestParam String semester){
        return userService.getStudentsBySemester(semester);
    }

}
