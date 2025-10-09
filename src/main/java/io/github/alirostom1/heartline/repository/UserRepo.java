package io.github.alirostom1.heartline.repository;

import io.github.alirostom1.heartline.model.entity.Nurse;
import io.github.alirostom1.heartline.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends CrudRepo<User>{
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    List<Nurse> findAllNurses();

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
