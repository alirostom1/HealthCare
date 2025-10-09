package io.github.alirostom1.heartline.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CrudRepo<E>{
    E save(E e);
    void delete(UUID id);
    Optional<E> findById(UUID id);
    List<E> findAll();
}
