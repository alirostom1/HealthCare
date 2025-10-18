package io.github.alirostom1.heartline.service;

import io.github.alirostom1.heartline.model.entity.Generalist;
import io.github.alirostom1.heartline.model.entity.Nurse;
import io.github.alirostom1.heartline.model.entity.Specialist;
import io.github.alirostom1.heartline.model.entity.User;
import io.github.alirostom1.heartline.model.enums.ERole;
import io.github.alirostom1.heartline.model.enums.Specialty;
import io.github.alirostom1.heartline.repository.UserRepo;
import io.github.alirostom1.heartline.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private HttpSession session;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepo);
    }

    @Test
    void login_Success() throws Exception {
        String hashedPw = BCrypt.hashpw("password123",BCrypt.gensalt());
        when(userRepo.findByUsername("doctor")).thenReturn(Optional.of(new Specialist("Dr. Smith", "doctor", "doc@hospital.com", hashedPw)));

        User result = userService.login("doctor", "password123");

        assertNotNull(result);
    }

    @Test
    void registerNurse_Success() throws Exception {
        when(userRepo.existsByUsername(any())).thenReturn(false);
        when(userRepo.existsByEmail(any())).thenReturn(false);
        when(userRepo.save(any())).thenReturn(new Nurse("Nurse", "nurse", "nurse@hospital.com", "hashed"));

        User result = userService.registerNurse("Nurse Joy", "nurse", "nurse@hospital.com", "password123");

        assertEquals(ERole.NURSE, result.getRole());
    }

    @Test
    void registerGeneralist_Success() throws Exception {
        when(userRepo.existsByUsername(any())).thenReturn(false);
        when(userRepo.existsByEmail(any())).thenReturn(false);
        when(userRepo.save(any())).thenReturn(new Generalist("Dr. General", "general", "general@hospital.com", "hashed"));

        User result = userService.registerGeneralist("Dr. General", "general", "general@hospital.com", "password123");

        assertEquals(ERole.GENERALIST, result.getRole());
    }

    @Test
    void registerSpecialist_Success() throws Exception {
        when(userRepo.existsByUsername(any())).thenReturn(false);
        when(userRepo.existsByEmail(any())).thenReturn(false);
        when(userRepo.save(any())).thenReturn(new Specialist("Dr. Spec", "spec", "spec@hospital.com", "hashed"));

        User result = userService.registerSpecialist("Dr. Spec", "spec", "spec@hospital.com", "password123");

        assertEquals(ERole.SPECIALIST, result.getRole());
    }

    @Test
    void updateSpecialist_Success() {
        Specialist specialist = new Specialist("Dr. Old", "old", "old@hospital.com", "hashed");
        when(userRepo.findById(any())).thenReturn(Optional.of(specialist));
        when(userRepo.save(any())).thenReturn(specialist);

        Specialist result = userService.updateSpecialist(UUID.randomUUID(), Specialty.CARDIOLOGY, 200.0);

        assertEquals(Specialty.CARDIOLOGY, result.getSpecialty());
    }

    @Test
    void getCurrentUser_Success() {
        User user = new Nurse("Test", "test", "test@hospital.com", "hashed");
        when(session.getAttribute("currentUser")).thenReturn(user);

        User result = userService.getCurrentUser(session);

        assertEquals(user, result);
    }
}