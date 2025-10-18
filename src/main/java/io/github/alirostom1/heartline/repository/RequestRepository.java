package io.github.alirostom1.heartline.repository;

import io.github.alirostom1.heartline.model.entity.Request;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RequestRepository {
    Request save(Request r);
    List<Request> findAll();
    Optional<Request> findById(UUID id);
}
