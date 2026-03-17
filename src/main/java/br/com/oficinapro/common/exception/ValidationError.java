package br.com.oficinapro.common.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends ExceptionResponse {

    private final List<FieldMessage> errors = new ArrayList<>();

    public ValidationError(String message, int status, String path, String timestamp, String error) {
        super(message, status, path, timestamp, error);
    }

    public List<FieldMessage> getErrors() {
        return errors;
    }

    public void addError(String fieldName, String fieldMessage) {
        errors.removeIf(field -> field.name().equals(fieldName));
        errors.add(new FieldMessage(fieldName, fieldMessage));
    }
}
