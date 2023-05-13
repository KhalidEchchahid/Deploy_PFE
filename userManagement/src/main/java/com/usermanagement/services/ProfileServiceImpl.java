package com.usermanagement.services;

import com.usermanagement.models.Profile;
import com.usermanagement.models.User;
import com.usermanagement.repositories.ProfileRepository;
import com.usermanagement.repositories.UserRepository;
import com.usermanagement.util.FileUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService{

    private final UserRepository userRepository ;
    private final ProfileRepository profileRepository;
    @Override
    @Transactional
    public ResponseEntity<?> uploadProfileImage(Long userId, MultipartFile file) throws IOException {


        User user = userRepository.findById(Math.toIntExact(userId)).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        Profile profile =  profileRepository.findByUser(user);

        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("File cannot be empty");
        }

        profile.setImage(file.getBytes());
        profileRepository.saveAndFlush(profile);

        return ResponseEntity.ok(profile);
    }

    @Override
    public ResponseEntity<?> getProfileById(Long id) {
        Optional<Profile> profile = profileRepository.findById(id);
        if(profile.isPresent()){

            return ResponseEntity.ok(profile.get());
        }else {
            return ResponseEntity.ok("laaaaaa");
        }

    }


}
