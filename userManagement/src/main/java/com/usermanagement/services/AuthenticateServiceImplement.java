package com.usermanagement.services;

import com.openfeign.openfeign.user.UserInfo;
import com.usermanagement.config.JwtService;
import com.usermanagement.dto.AuthenticationRequest;
import com.usermanagement.dto.AuthenticationResponse;
import com.usermanagement.dto.RegisterRequest;
import com.usermanagement.models.ConfirmationToken;
import com.usermanagement.models.Profile;
import com.usermanagement.models.Role;
import com.usermanagement.models.User;
import com.usermanagement.repositories.ConfirmationTokenRepository;
import com.usermanagement.repositories.ProfileRepository;
import com.usermanagement.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthenticateServiceImplement implements AuthenticateService {
    private final UserRepository repository;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    private final MailService mailService;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ProfileRepository profileRepository;
    @Override
    @Transactional
    public ResponseEntity<?> registerStudent(@NotNull RegisterRequest request) {

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .semester(request.getSemester())
                .email(request.getEmail())
                .email(request.getEmail())
                .password(passwordEncoder.encode((request.getPassword())))
                .role(Role.ROLE_STUDENT)
                .isMailVerified(false)
                .build();

        //Email verification
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
                +"http://localhost:8086/api/v1/auth/confirm-account?token="+confirmationToken.getConfirmationToken());

        mailService.sendMail(mailMessage);
        repository.save(user);
        Profile profile = new Profile();
        profile.setUser(user);
        profileRepository.save(profile);
        return ResponseEntity.ok("Please check your email to verify it.");
    }

    @Override
    public ResponseEntity<?> confirmEmail(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
            User user = repository.findByEmailIgnoreCase(token.getUserEntity().getEmail());
            user.setMailVerified(true);
            repository.save(user);
            return ResponseEntity.ok("" +
                    "<div style='background-color: #f2f2f2; padding: 20px; text-align: center;'>" +
                    "<h1 style='color: #4CAF50;'>Email verified successfully!</h1>" +
                    "<p style='font-size: 18px;'>Thank you for verifying your email address.</p>" +
                    "<button style='background-color: #4CAF50; color: white; padding: 10px 20px; border: none; border-radius: 4px; text-decoration: none; font-size: 16px; margin-top: 20px;'>" +
                    "<a style='color: white; text-decoration: none;' href='http://localhost:3000/auth'>Click Here</a>" +
                    "</button>" +
                    "</div>"
            );
        }
        return ResponseEntity.badRequest().body("Error: Couldn't verify email");
    }

    @Override
    @Transactional
    public User registerProfessor(@NotNull RegisterRequest request) {
        // Check if the admin is authorized to create a professor account
//        if (!admin.getRole().equals(Role.ROLE_ADMIN)) {
//            throw new AccessDeniedException("Only admins can create professor accounts.");
//        }
        // Set role to PROFESSOR
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .semester(request.getSemester())
                .email(request.getEmail())
                .email(request.getEmail())
                .password(passwordEncoder.encode((request.getPassword())))
                .isMailVerified(true)
                .role(Role.ROLE_PROFESSOR)
                .build();
        repository.save(user);
        Profile profile = new Profile();
        profile.setUser(user);
        profileRepository.save(profile);
      return user;
    }

    @Override
    public ResponseEntity<?> authenticate(AuthenticationRequest request) {
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();

        if(!user.isMailVerified()){
            return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.FORBIDDEN);
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var jwtToken = jwtService.generateToken(user);

         Profile profile = profileRepository.findByUser(user);

//         if( profile.getImage() != null ){
//             profile.setImage(FileUtils.decompressFile(profile.getImage()));
//         }

         return ResponseEntity.ok(AuthenticationResponse.builder()
                 .token(jwtToken)
                 .profile(profile)
                 .build());

    }

    @Override
    public ResponseEntity<?> authenticateAdmin(AuthenticationRequest request) {
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        System.out.println(user);
        if(user.getRole().equals(Role.ROLE_ADMIN)){
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            var jwtToken = jwtService.generateToken(user);

            Profile profile = profileRepository.findByUser(user);

            return ResponseEntity.ok(AuthenticationResponse.builder()
                    .token(jwtToken)
                    .profile(profile)
                    .build());
        }else {
             return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.FORBIDDEN);
        }

    }


    @Override
        public UserInfo getCurrentUserInfo() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return null;
            }

            User userDetails = (User) authentication.getPrincipal();
            return new UserInfo(userDetails.getId());
        }




}
