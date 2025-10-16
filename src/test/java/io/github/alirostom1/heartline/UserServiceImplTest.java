package io.github.alirostom1.heartline;


import io.github.alirostom1.heartline.model.entity.Nurse;
import io.github.alirostom1.heartline.model.entity.User;
import io.github.alirostom1.heartline.repository.UserRepo;
import io.github.alirostom1.heartline.repository.UserRepoImpl;
import io.github.alirostom1.heartline.service.UserService;
import io.github.alirostom1.heartline.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceImplTest{
    private UserRepo userRepo;
    private UserService userService;

    @BeforeEach
    void setup(){
        this.userRepo = mock(UserRepoImpl.class);
        this.userService = new UserServiceImpl(userRepo);
    }
    @Test
    void login(){
        Nurse nurse = new Nurse();
        nurse.setUsername("nurse");
        nurse.setPassword(BCrypt.hashpw("nurse",BCrypt.gensalt()));

        when(userRepo.findByUsername("nurse")).thenReturn(Optional.of(nurse));

        User result = userService.login("nurse","nurse");
        System.out.println(result);
        System.out.println(nurse);
        assertNotNull(result);
        assertEquals(result,nurse);
    }

}
