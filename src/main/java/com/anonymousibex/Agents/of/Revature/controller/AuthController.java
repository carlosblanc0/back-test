package com.anonymousibex.Agents.of.Revature.controller;

import com.anonymousibex.Agents.of.Revature.dto.ResponseDto;
import com.anonymousibex.Agents.of.Revature.dto.UserDto;
import com.anonymousibex.Agents.of.Revature.model.User;
import com.anonymousibex.Agents.of.Revature.service.UserService;
import com.anonymousibex.Agents.of.Revature.util.ExceptionResponseUtils;
import com.anonymousibex.Agents.of.Revature.util.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody User user){
        UserDto createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody User user, HttpServletRequest request){
        userService.ensureNoActiveSession(request);

        User authenticatedUser = userService.authenticate(user.getUsername(), user.getPassword());
        HttpSession session = request.getSession(true);
        session.setAttribute("userId", authenticatedUser.getId());
        return ExceptionResponseUtils.buildResponse("Login successful",HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto> logout(HttpServletRequest request, HttpServletResponse response){
        userService.ensureActiveSession(request);

        HttpSession session = request.getSession(false);
        if (session != null){
            session.invalidate();
        }

        UserUtils.clearSessionCookie(response);
        return ExceptionResponseUtils.buildResponse("Logout successful",HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(HttpServletRequest request){
        UserDto user = UserUtils.toUserDto(userService.getCurrentUserBySession(request));
        return ResponseEntity.ok(user);
    }
}
