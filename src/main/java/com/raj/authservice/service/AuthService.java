package com.raj.authservice.service;

import com.raj.authservice.dto.RegisterRequest;
import com.raj.authservice.dto.RegisterResponse;
import com.raj.authservice.entity.UserEntity;
import com.raj.authservice.enums.Role;
import com.raj.authservice.exception.EmailAlreadyExistException;
import com.raj.authservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepo, PasswordEncoder passwordEncoder){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
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
}
