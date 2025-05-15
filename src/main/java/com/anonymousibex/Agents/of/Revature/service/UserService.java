package com.anonymousibex.Agents.of.Revature.service;

import com.anonymousibex.Agents.of.Revature.dto.UserDto;
import com.anonymousibex.Agents.of.Revature.exception.*;
import com.anonymousibex.Agents.of.Revature.model.Role;
import com.anonymousibex.Agents.of.Revature.model.User;
import com.anonymousibex.Agents.of.Revature.repository.UserRepository;
import com.anonymousibex.Agents.of.Revature.util.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserDto createUser(User user){
        String username = user.getUsername().toLowerCase();
        String password = user.getPassword();

        if(userRepository.findByUsername(username).isPresent()){
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        if(username == null || username.length() < 5 || username.length() > 20){
            throw new InvalidUsernameException("Username must be between 5 and 20 characters. Please try again.");
        }

        if(!UserUtils.isValidPassword(password)){
            throw new InvalidPasswordException("Password must be between 8 and 30 characters. Please be sure to include a lowercase, an uppercase, a number, and a special character.");
        }

        user.setPassword(encoder.encode(password));
        user.setRole(Role.USER);
        user.setUsername(username);
        return UserUtils.toUserDto(userRepository.save(user));
    }

    public User authenticate(String username, String rawPassword) {
        Optional<User> optionalUser = userRepository.findByUsername(username.toLowerCase());
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (encoder.matches(rawPassword, user.getPassword())) {
                return user;
            }
        }
        throw new InvalidCredentialsException("Invalid credentials. Please try again.");
    }

    public Optional<User> findById(Long userId){
        return userRepository.findById(userId);
    }

    public User getCurrentUserBySession(HttpServletRequest request){
        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("userId") == null){
            throw new NoActiveSessionException("No active session found.");
        }

        Long userId = (Long) session.getAttribute("userId");
        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        return user;
    }

    public void ensureNoActiveSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            throw new SessionAlreadyExistsException("A user is already logged in from this session.");
        }
    }

    public void ensureActiveSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            throw new NoActiveSessionException("No user is currently logged in.");
        }
    }

}

