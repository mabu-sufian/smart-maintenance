package com.example.maintenance.service;

import com.example.maintenance.dto.LoginRequestDTO;
import com.example.maintenance.dto.LoginResponseDTO;
import com.example.maintenance.dto.RegisterRequestDTO;
import com.example.maintenance.dto.RegisteredResponseDTO;
import com.example.maintenance.entity.Enum.Role;
import com.example.maintenance.entity.User;
import com.example.maintenance.exception.AlreadyRegisteredException;
import com.example.maintenance.exception.EmailNotExists;
import com.example.maintenance.exception.InvalidCredentialException;
import com.example.maintenance.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService)
    {
        this.passwordEncoder=passwordEncoder;
        this.userRepository=userRepository;
        this.jwtService=jwtService;
    }

    public RegisteredResponseDTO register(RegisterRequestDTO requestDTO)
    {
        if(userRepository.existsByEmail(requestDTO.getEmail())){
            throw new AlreadyRegisteredException("Email is already registered");
        }

        User user =new User();
        user.setName(requestDTO.getName());
        user.setEmail(requestDTO.getEmail());
        String hash=passwordEncoder.encode(requestDTO.getPassword());
        user.setPassword(hash);
        user.setRole(Role.USER);
        User savedUser=userRepository.save(user);

        return new RegisteredResponseDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole().name()
        );
    }


public LoginResponseDTO Login(LoginRequestDTO requestDTO)
{

    User user=userRepository.findByEmail(requestDTO.getEmail()).orElseThrow(()->new EmailNotExists("Email does not exist"));
    if(!passwordEncoder.matches(requestDTO.getPassword(), user.getPassword()))
    {
        throw new InvalidCredentialException("Invalid credential!");
    }

String Token=jwtService.generateToken(user.getId(),user.getRole().name());
    return new LoginResponseDTO(Token);
}


}
