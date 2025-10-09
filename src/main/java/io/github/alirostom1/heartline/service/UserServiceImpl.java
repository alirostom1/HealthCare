package io.github.alirostom1.heartline.service;

import io.github.alirostom1.heartline.exception.AuthException;
import io.github.alirostom1.heartline.exception.RegisterException;
import io.github.alirostom1.heartline.model.entity.Generalist;
import io.github.alirostom1.heartline.model.entity.Nurse;
import io.github.alirostom1.heartline.model.entity.User;
import io.github.alirostom1.heartline.model.enums.ERole;
import io.github.alirostom1.heartline.repository.UserRepo;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class UserServiceImpl implements UserService{
    private final UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo){
        this.userRepo = userRepo;
    }


    @Override
    public User login(String username, String password) throws AuthException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new AuthException("Invalid username or password"));
        if(!BCrypt.checkpw(password,user.getPassword())){
            throw new AuthException("Invalid username or password");
        }
        return user;
    }

    @Override
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @Override
    public User registerNurse(String fullName, String username, String email, String password) throws RegisterException {
        validateRegistration(username,email);
        String hashedPw = BCrypt.hashpw(password,BCrypt.gensalt());
        Nurse nurse = new Nurse(fullName,username,email,hashedPw);
        return userRepo.save(nurse);
    }

    @Override
    public User registerGeneralist(String fullName, String username, String email, String password) throws RegisterException {
        validateRegistration(username,email);
        String hashedPw = BCrypt.hashpw(password,BCrypt.gensalt());
        Generalist generalist = new Generalist(fullName,username,email,hashedPw);
        System.out.println("hi");
        return userRepo.save(generalist);
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return !userRepo.existsByUsername(username);
    }

    @Override
    public boolean isEmailAvailable(String email) {
        return !userRepo.existsByEmail(email);
    }

    @Override
    public User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("currentUser");
    }

    @Override
    public boolean isAuthenticated(HttpSession session) {
        return getCurrentUser(session) != null;
    }

    @Override
    public boolean hasRole(HttpSession session, ERole role) {
        User user = getCurrentUser(session);
        return user != null && user.getRole() == role;
    }

    private void validateRegistration(String username,String email) throws RegisterException{
        if(!isUsernameAvailable(username)){
            throw new RegisterException("Username already exists");
        }
        if(!isEmailAvailable(email)){
            throw new RegisterException("Email already exists");
        }
    }
}
