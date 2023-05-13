package com.usermanagement.services;

import com.openfeign.openfeign.user.UserInfo;
import com.usermanagement.dto.AuthenticationRequest;
import com.usermanagement.dto.AuthenticationResponse;
import com.usermanagement.dto.RegisterRequest;
import com.usermanagement.models.User;
import org.springframework.http.ResponseEntity;

public interface AuthenticateService {
    ResponseEntity<?> registerStudent(RegisterRequest request);

    ResponseEntity<?> authenticate(AuthenticationRequest request);
    ResponseEntity<?> authenticateAdmin(AuthenticationRequest request);

    UserInfo getCurrentUserInfo();

    ResponseEntity<?> confirmEmail(String confirmationToken);

    User registerProfessor(RegisterRequest request);
}
