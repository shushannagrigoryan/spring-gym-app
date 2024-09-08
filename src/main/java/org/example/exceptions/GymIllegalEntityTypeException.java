package org.example.exceptions;

public class GymIllegalEntityTypeException extends GymCustomRuntimeException {
    public GymIllegalEntityTypeException(String entityType) {
        super(String.format("Illegal storage name %s", entityType));
    }
}
