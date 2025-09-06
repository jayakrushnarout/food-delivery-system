package com.springcloud.auth_service.controller;

import com.springcloud.auth_service.dto.LoginRequest;
import com.springcloud.auth_service.dto.RegistrationRequest;
import com.springcloud.auth_service.entity.Role;
import com.springcloud.auth_service.entity.User;
import com.springcloud.auth_service.repository.UserRepository;
import com.springcloud.auth_service.service.EmailService;
import com.springcloud.auth_service.service.JwtUtilService;
import com.springcloud.auth_service.service.OtpUtil;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/auth")
public class AuthController
{


    @Autowired
    private JwtUtilService jwtUtilService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    private Map<String, Map<String, Object>> tempUserMap = new ConcurrentHashMap<>();

    // Step 1: Register user and send OTP
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
        }

        String username= request.getUsername();
        // Generate OTP
        String otp = OtpUtil.generateOtp();

        // Store temporary registration info
        Map<String, Object> tempData = new HashMap<>();
        tempData.put("request", request);
        tempData.put("otp", otp);
        tempData.put("expiry", LocalDateTime.now().plusMinutes(5)); // OTP valid for 5 minutes

        tempUserMap.put(username, tempData);

        // Send OTP via email
        emailService.sendOtp(username, otp);

        return new ResponseEntity<>("OTP sent to your email. Verify to complete registration.", HttpStatus.CREATED);
    }

    // Step 2: Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String username, @RequestParam String otp) {

        Map<String, Object> tempData = tempUserMap.get(username);

        if (tempData == null) {
            return new ResponseEntity<>("No registration request found", HttpStatus.BAD_REQUEST);
        }

        String storedOtp = (String) tempData.get("otp");
        LocalDateTime expiry = (LocalDateTime) tempData.get("expiry");

        // Check OTP validity
        if (!storedOtp.equals(otp)) {
            return new ResponseEntity<>("Invalid OTP", HttpStatus.BAD_REQUEST);
        }

        if (expiry.isBefore(LocalDateTime.now())) {
            tempUserMap.remove(username);
            return new ResponseEntity<>("OTP expired", HttpStatus.BAD_REQUEST);
        }

        // OTP valid → save user permanently
        RegistrationRequest request = (RegistrationRequest) tempData.get("request");
        User user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        if (user.getRole() == null) user.setRole(Role.ROLE_CUSTOMER);
        user.setVerified(true);

        userRepository.save(user);

        // Clean up temporary storage
        tempUserMap.remove(username);

        return new ResponseEntity<>("Registration verified successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest user)
    {
        Authentication authentication;

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        try {
            authentication = authenticationManager.authenticate(token);
        } catch (AuthenticationException e)
        {


            // TODO: handle exception
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userdetails=(UserDetails)authentication.getPrincipal();
        String jwtToken= jwtUtilService.generateJwtToken(userdetails);

        return new ResponseEntity<String>(jwtToken,HttpStatus.OK);
    }



}
