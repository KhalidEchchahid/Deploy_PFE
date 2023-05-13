package com.usermanagement.services;


import com.usermanagement.dto.PasswordDTO;
import com.usermanagement.dto.ProfessorDTO;
import com.usermanagement.models.Role;
import com.usermanagement.models.User;
import com.usermanagement.repositories.ConfirmationTokenRepository;
import com.usermanagement.repositories.ProfileRepository;
import com.usermanagement.repositories.UserRepository;
import com.usermanagement.util.FileUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository ;
    private final PasswordEncoder passwordEncoder ;

    private final ProfileRepository profileRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;


    @Override
    public ResponseEntity<?> getAllUsers(int page , Role role) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<User> users = userRepository.findByRole(role, pageable );
        return ResponseEntity.ok(users.getContent());
    }

    @Override
    public ResponseEntity<User> getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(Math.toIntExact(id));
        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<?> updatePassword(Long id, PasswordDTO newPassword) {
        Optional<User> existingUser = userRepository.findById(Math.toIntExact(id));
        if (existingUser.isPresent()) {
            User UserReq = existingUser.get();
            UserReq.setPassword(passwordEncoder.encode(newPassword.password()));
            userRepository.save(UserReq);
            return new ResponseEntity<>("User has been updated successfully", HttpStatus.ACCEPTED);
        } else {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(Math.toIntExact(id));
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            confirmationTokenRepository.deleteByUser(user);
            profileRepository.deleteByUser(user);
            userRepository.delete(user); // delete the user
            return new ResponseEntity<>("User deleted successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @Override
    public ResponseEntity<?> getAllProfessorsFullNameAndId() {
        List<ProfessorDTO> professors = userRepository.findProfessors();
        if(!professors.isEmpty()){
            return new ResponseEntity<>(professors, HttpStatus.OK);
        }else {
            return ResponseEntity.notFound().build();
        }

    }

    @Override
    public List<String> getStudentsBySemester(String semester) {
        if(semester == null){
            return null ;
        }
        List<String> emails = userRepository.findEmailsByRoleAndSemester(Role.ROLE_STUDENT, semester);

        return emails;
    }


}
