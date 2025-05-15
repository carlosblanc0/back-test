package com.anonymousibex.Agents.of.Revature.util;

import com.anonymousibex.Agents.of.Revature.dto.UserDto;
import com.anonymousibex.Agents.of.Revature.model.Role;
import com.anonymousibex.Agents.of.Revature.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class UserUtils {

    public static boolean isValidPassword(String password){
        if (password == null || password.length() < 8 || password.length() > 30){
            return false;
        }

        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasNum = password.matches(".*[0-9].*");
        boolean hasSpecial = password.matches(".*[^a-zA-Z0-9].*");

        return hasUpper && hasLower && hasNum && hasSpecial;
    }

    public static UserDto toUserDto(User user){
        Long id = user.getId();
        String username = user.getUsername();
        Role role = user.getRole();

        return new UserDto(id, username, role);
    }

    public static void clearSessionCookie(HttpServletResponse response){
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }
}
