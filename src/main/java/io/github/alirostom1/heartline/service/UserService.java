package io.github.alirostom1.heartline.service;

import io.github.alirostom1.heartline.exception.AuthException;
import io.github.alirostom1.heartline.exception.RegisterException;
import io.github.alirostom1.heartline.model.entity.Specialist;
import io.github.alirostom1.heartline.model.entity.User;
import io.github.alirostom1.heartline.model.enums.ERole;
import io.github.alirostom1.heartline.model.enums.Specialty;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    //Auth logic
    User login(String username, String password) throws AuthException;
    void logout(HttpSession session);

    //registration logic
    User registerNurse(String fullName,String username,String email,String password) throws RegisterException;
    User registerGeneralist(String fullName,String username,String email,String password) throws RegisterException;
    User registerSpecialist(String fullName,String username,String email,String password) throws RegisterException;

    //validation logic
    boolean isUsernameAvailable(String username);
    boolean isEmailAvailable(String email);

    //Session logic
    User getCurrentUser(HttpSession session);
    boolean isAuthenticated(HttpSession session);
    boolean hasRole(HttpSession session, ERole role);

    Optional<User> findById(UUID userId);

    Specialist updateSpecialist(UUID userId, Specialty specialty, double fee);

}
