package com.usermanagement.controllers;

import com.usermanagement.dto.PasswordDTO;
import com.usermanagement.models.Role;
import com.usermanagement.models.User;
import com.usermanagement.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    // @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping
//    public ResponseEntity<?> getAllUsers() {
//        return userService.getAllUsers();
//    }
    @GetMapping
    public ResponseEntity<?> getAllUsersByRole(@RequestParam(defaultValue = "0") int  page ,@RequestParam Role role) {
        return userService.getAllUsers(page , role);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserById(@PathVariable Long id, @RequestBody PasswordDTO password ) {
        return  userService.updatePassword(id, password);
    }

    @GetMapping("/professors")
    public ResponseEntity<?> getAllProfessorsFullNameAndId(){
        return userService.getAllProfessorsFullNameAndId();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        return userService.deleteUserById(id);
    }



}