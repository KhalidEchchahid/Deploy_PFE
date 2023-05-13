package com.usermanagement.services;

import com.usermanagement.dto.PasswordDTO;
import com.usermanagement.models.Role;
import com.usermanagement.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    ResponseEntity<?> getAllUsers(int page , Role role);

    ResponseEntity<?> getUserById(Long id);

    ResponseEntity<?> updatePassword(Long id, PasswordDTO newPassword);

    ResponseEntity<String> deleteUserById(Long id);

    ResponseEntity<?> getAllProfessorsFullNameAndId();
    List<String> getStudentsBySemester(String semester);

}
