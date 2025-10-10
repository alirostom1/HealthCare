package io.github.alirostom1.heartline.exception;

public class PatientAlreadyQueued extends RuntimeException {
    public PatientAlreadyQueued(String message) {
        super(message);
    }
}
