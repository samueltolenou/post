package com.formation.controller;


import com.formation.models.Role;
import com.formation.models.User;
import com.formation.payload.ResetPassword;
import com.formation.payload.UserIdentityAvailability;
import com.formation.payload.response.ApiResponse;
import com.formation.repository.RoleRepository;
import com.formation.repository.UserRepository;
//import com.formation.security.CurrentUser;
//import com.formation.security.UserPrincipal;
import com.formation.security.CurrentUser;
import com.formation.security.UserPrincipal;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
@Api(value = "Users", description = "Users controllers details.", tags = {"Users"})
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController extends BaseController{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;


    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') ")
    @ApiOperation(value = "This ressource is used to get connected user details. ADMIN or USER account is necessary to master this operation..")
    public User getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        //UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
        return userRepository.findById(currentUser.getId()).get();
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') ")
    @ApiOperation(value = "This ressource is used to retrieve user details based on his id. ADMIN or USER account is necessary to master this operation.")
    public User getCurrentUser(@PathVariable Long id) {
        if (userRepository.findById(id).isPresent()) {
            return userRepository.findById(id).get();
        } else {
            return null;
        }
    }

    @GetMapping("/checkUsernameAvailability")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "This ressource is used to check username avalability. ADMIN account is necessary to master this operation.")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/checkEmailAvailability")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "This ressource is used to check email avalability. ADMIN account is necessary to master this operation.")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }


    @PostMapping("/update-user")
    @ApiOperation(value = "This ressource is used to update user account. ADMIN account is necessary to master this operation.")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user) {

        User result = null;
        if (userRepository.existsById(user.getId())) {

            User r = userRepository.findById(user.getId()).get();
            if (user.getPassword() != "") {
                //String pwd = passwordEncoder.encode(user.getPassword());
                user.setPassword(r.getPassword());
                result = userRepository.save(user);
            } else {
                return new ResponseEntity(new ApiResponse(false, "Le mot de passe ne peut pas être vide."),
                        HttpStatus.BAD_REQUEST);
            }
        }
        return ResponseEntity.ok(result);
    }


    @ApiOperation(value = "This ressource is used to reset user password. ADMIN or USER account is necessary to master this operation.")
    @PostMapping("/reset-password")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') ")
    public ResponseEntity<?> resetPassword(@CurrentUser UserPrincipal userPrincipal, @RequestBody ResetPassword resetPassword) {

        Optional<User> updateUser = userRepository.findById(userPrincipal.getId());

        updateUser.get().setPassword(passwordEncoder.encode(resetPassword.getNewPassword()));

        User userSaved = userRepository.save(updateUser.get());

        return new ResponseEntity(new ApiResponse(true, "Modification du mot de passe effectué avec succès."),
                HttpStatus.OK);
    }

    @PutMapping("/{id}/add-role/{roleId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> revokeAllRoleAndAddNewOne(@PathVariable(value = "id") Long id, @PathVariable(value = "roleId") Long roleId) {
        Optional<User> user = userRepository.findById(id);
        Optional<Role> role = roleRepository.findById(roleId);
        if (user.isPresent() && role.isPresent()) {
            User user1 = user.get();
            Set<Role> roles = new HashSet<>();
            roles.add(role.get());
            user1.setRoles(roles);
            userRepository.save(user1);
            return ResponseEntity.ok(new ApiResponse(true, "Role bien assigné", HttpStatus.OK));

        } else {
            return ResponseEntity.ok(new ApiResponse(false, "Utilisateur non trouvé", HttpStatus.NOT_FOUND));
        }

    }

    @GetMapping("/roles")
    public ResponseEntity<ApiResponse> getAllRole() {
        return ResponseEntity.ok(new ApiResponse(true, "List des roles", HttpStatus.OK, roleRepository.findAll()));
    }

}
