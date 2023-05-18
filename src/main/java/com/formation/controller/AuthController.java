package com.formation.controller;


import com.formation.enums.RoleName;
import com.formation.enums.StatusMessage;
import com.formation.exceptions.AppException;
import com.formation.models.PasswordResetToken;
import com.formation.models.Role;
import com.formation.models.User;
import com.formation.payload.request.LoginRequest;
import com.formation.payload.request.SignUpRequest;
import com.formation.payload.request.UpdateUserPasswordRequest;
import com.formation.payload.response.ApiResponse;
import com.formation.payload.response.JwtAuthenticationResponse;
import com.formation.repository.PasswordResetTokenRepository;
import com.formation.repository.RoleRepository;
import com.formation.repository.UserRepository;
import com.formation.security.JwtTokenProvider;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Calendar;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController extends BaseController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

//    @Autowired
//    SendMailService sendMailService;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    /**
     * @param loginRequest
     * @return
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsernameOrEmail();
        Optional<User> user = userRepository.findByUsernameOrEmail(
                username,
                username
        );
        if (user.isPresent()) {
            username = user.get().getUsername();
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse(jwt);
        if (user.get().isHasDefaultPassword()) {
            jwtAuthenticationResponse.setHasDefaultPassword(true);
            jwtAuthenticationResponse.setChangePasswordToken(createPasswordResetTokenForUser(user.get()));
        }
        return ResponseEntity.ok(new ApiResponse(true, "Authentification réussie", HttpStatus.OK, jwtAuthenticationResponse));
    }

    /**
     * @param signUpRequest
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        try {


            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                return new ResponseEntity(new ApiResponse(false, StatusMessage.USERNAME_EXIST.getReasonPhrase(), StatusMessage.USERNAME_EXIST.getValue(), "Username is already taken!"),
                        HttpStatus.BAD_REQUEST);
            }

            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return new ResponseEntity(new ApiResponse(false, StatusMessage.EMAIL_EXIST.getReasonPhrase(), StatusMessage.EMAIL_EXIST.getValue(), "Email Address already in use!"),
                        HttpStatus.BAD_REQUEST);
            }

            // Creating user's account
            User user = new User(signUpRequest.getName(), signUpRequest.getUsername() == null || signUpRequest.getUsername().isEmpty() ? signUpRequest.getEmail() : signUpRequest.getEmail(),
                    signUpRequest.getEmail(), "");

            user.setPassword(passwordEncoder.encode(user.getEmail() + "#2021"));

            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new AppException("User Role not set."));

            user.setRoles(Collections.singleton(userRole));
            user.setHasDefaultPassword(true);
            User result = userRepository.save(user);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/api/users/{username}")
                    .buildAndExpand(result.getUsername()).toUri();

            return ResponseEntity.created(location).body(new ApiResponse(true, StatusMessage.USER_REGISTERED.getReasonPhrase(), StatusMessage.USER_REGISTERED.getValue(), "User registered successfully"));

        } catch (Exception e) {
            return new ResponseEntity(new ApiResponse(false, e.getMessage(), StatusMessage.INTERNAL_SERVER_ERROR.getValue(), e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(value = "/reset-password/{email}")
    @ResponseBody
    @ApiOperation(value = "Permet de réinitialiser le mot de passe d'un utilisateur à partir de l'email (Mot de passe oublié)!")
    public ResponseEntity<ApiResponse> resetPassword(
            @PathVariable("email") String userEmail) {

        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isPresent()) {
            String token = createPasswordResetTokenForUser(user.get());
            try {
//                sendMailService.sendMail(user.get().getEmail(), "RESET PASSWORD", token);
                return ResponseEntity.ok(new ApiResponse(true, "token bien générer et mail envoyé!", HttpStatus.OK, null));

            } catch (Exception e) {
                //e.printStackTrace();
                return ResponseEntity.ok(new ApiResponse(true, "Tout s'est bien passé mais nous ne pouvons garantir de l'envoi du mail contenant le token!", HttpStatus.OK, null));

            }


        } else {
            return ResponseEntity.ok(new ApiResponse(false, "Aucun utilisateur ne correspond à cet email!", HttpStatus.NOT_FOUND, null));


        }


    }


    private String createPasswordResetTokenForUser(User user) {
        String token = UUID.randomUUID().toString();
        System.out.println("token = " + token);
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
        passwordResetToken.setExpiryDate();
        //     System.out.println("passwordResetToken = " + passwordResetToken);
        passwordResetTokenRepository.save(passwordResetToken);
        return token;
    }


    private User validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> passwordResetTokenOptional =
                passwordResetTokenRepository.findByToken(token);

        if (passwordResetTokenOptional.isPresent()) {

            Calendar cal = Calendar.getInstance();
            if ((passwordResetTokenOptional.get().getExpiryDate()
                    .getTime() - cal.getTime()
                    .getTime()) <= 0) {
                return null;
            } else {
                return passwordResetTokenOptional.get().getUser();
            }

        } else {
            return null;
        }

    }


    @PostMapping("/update-password")
    @ApiOperation(value = "Permet de mettre à jour un mot de passe")
    public ResponseEntity<?> updateUserPassword(@Valid @RequestBody UpdateUserPasswordRequest updateUserPasswordRequest) {
        if (updateUserPasswordRequest.getNewPassword().equals(updateUserPasswordRequest.getConfirmPassword())) {
            User user = validatePasswordResetToken(updateUserPasswordRequest.getToken());
            if (user != null) {
                // System.out.println("updateUserPasswordRequest = " + updateUserPasswordRequest);
                user.setPassword(passwordEncoder.encode(updateUserPasswordRequest.getConfirmPassword()));
                user.setHasDefaultPassword(false);
                return ResponseEntity.ok(new ApiResponse(true, "Mot de passe bien mise  jour", HttpStatus.OK, userRepository.save(user)));
            } else {
                return ResponseEntity.ok(new ApiResponse(false, "Le token est expiré ou n'est pas correct", HttpStatus.NOT_FOUND, null));
            }
        } else {
            return ResponseEntity.ok(new ApiResponse(false, "Les deux mots de passe ne  sont pas identiques", HttpStatus.CONFLICT, null));
        }

    }

}
