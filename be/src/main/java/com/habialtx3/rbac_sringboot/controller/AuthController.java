package com.habialtx3.rbac_sringboot.controller;

import com.habialtx3.rbac_sringboot.entity.Role;
import com.habialtx3.rbac_sringboot.entity.User;
import com.habialtx3.rbac_sringboot.model.ERole;
import com.habialtx3.rbac_sringboot.model.WebResponse;
import com.habialtx3.rbac_sringboot.repository.RoleRepository;
import com.habialtx3.rbac_sringboot.repository.UserRepository;
import com.habialtx3.rbac_sringboot.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@CrossOrigin(
        origins = "http://localhost:3000",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JWTUtils jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<WebResponse<String>> registerUser(@RequestBody Map<String, String> request) {
        if (userRepository.existsByUsername(request.get("username"))) {
            WebResponse<String> response = WebResponse.<String>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .status("BAD REQUEST")
                    .errors("Username is already taken!")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        User user = new User(request.get("username"), encoder.encode(request.get("password")));
        Set<Role> roles = new HashSet<>();

        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);

        WebResponse<String> response = WebResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .data("User registered successfully!")
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<WebResponse<Map<String, String>>> authenticateUser(@RequestBody Map<String, String> request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.get("username"), request.get("password")));

        String jwt = jwtUtils.generateJwtToken(authentication);

        Map<String, String> tokenData = new HashMap<>();
        tokenData.put("token", jwt);

        WebResponse<Map<String, String>> response = WebResponse.<Map<String, String>>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .data(tokenData)
                .build();

        return ResponseEntity.ok(response);
    }
}
