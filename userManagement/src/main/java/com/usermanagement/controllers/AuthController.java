package com.usermanagement.controllers;


import com.usermanagement.dto.AuthenticationRequest;
import com.usermanagement.dto.AuthenticationResponse;
import com.usermanagement.dto.RegisterRequest;
import com.usermanagement.models.User;
import com.usermanagement.services.AuthenticateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticateService service;
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(service.registerStudent(request));
    }

    @PostMapping("/registerProfessor")
    public User registerProfessor(
            @RequestBody RegisterRequest request
            ){
        return service.registerProfessor(request);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(service.authenticate(request).getBody());
    }
    @PostMapping("/authenticateAdmin")
    public ResponseEntity<?> authenticateAdmin(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(service.authenticateAdmin(request).getBody());
    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> confirmAccount(@RequestParam("token")String confirmationToken) {
        return service.confirmEmail(confirmationToken);
    }



}
