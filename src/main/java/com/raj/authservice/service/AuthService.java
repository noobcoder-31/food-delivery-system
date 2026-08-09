package com.raj.authservice.service;

import com.raj.authservice.dto.LoginRequest;
import com.raj.authservice.dto.LoginResponse;
import com.raj.authservice.dto.RegisterRequest;
import com.raj.authservice.dto.RegisterResponse;
import com.raj.authservice.entity.UserEntity;
import com.raj.authservice.enums.Role;
import com.raj.authservice.exception.EmailAlreadyExistException;
import com.raj.authservice.exception.InvalidCredentialsException;
import com.raj.authservice.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepo, PasswordEncoder passwordEncoder,AuthenticationManager authenticationManager){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public RegisterResponse registerUser(RegisterRequest request){
        if(userRepo.existsByEmail(request.getEmail())){
            throw  new EmailAlreadyExistException("Email already exists");
        }

        String passHash = passwordEncoder.encode(request.getPassword());

        UserEntity user = new UserEntity(request.getName(), request.getEmail(), passHash, Role.USER);

        UserEntity u = userRepo.save(user);

        return new RegisterResponse(u.getId(),u.getName(),u.getEmail());

    }

    public LoginResponse loginUser(LoginRequest request){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        LoginResponse response = new LoginResponse();
        response.setMessage("Logged In Successfully!");

        return response;


        // Manual LOgging IN
//        UserEntity user = userRepo.findByEmail(request.getEmail()).orElseThrow(() ->
//                new UsernameNotFoundException("Invalid Email or Password."));
//
//        if(user==null){
//            throw new InvalidCredentialsException("Invalid Email or Password.");
//        }
//
//        boolean isPassMatch = passwordEncoder.matches(request.getPassword(),user.getPassword());
//
//        if(!isPassMatch){
//            throw new InvalidCredentialsException("Invalid Email or Password.");
//        }
//
//        LoginResponse response = new LoginResponse();
//        response.setMessage("Logged In Successfully!");
//
//        return response;
    }
}
