package br.com.oficinapro.common.exception.handler;

import br.com.oficinapro.common.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;

@ControllerAdvice
public class CustomEntityResponseHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    public final ResponseEntity<ExceptionResponse> handleInvalidJwtAuthenticationException(
            InvalidJwtAuthenticationException ex,
            WebRequest request
    ) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        ExceptionResponse response = new ExceptionResponse(
                ex.getMessage(),
                status.value(),
                request.getDescription(false).replace("uri=", ""),
                OffsetDateTime.now().toString(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public final ResponseEntity<ExceptionResponse> handleResourceConflitException(
            ResourceConflictException ex,
            WebRequest request
    ){
        HttpStatus status = HttpStatus.CONFLICT;
        ExceptionResponse response = new ExceptionResponse(
                ex.getMessage(),
                status.value(),
                request.getDescription(false).replace("uri=", ""),
                OffsetDateTime.now().toString(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public final ResponseEntity<ExceptionResponse> handleBusinessException(
            BusinessException ex,
            WebRequest request
    ){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionResponse response = new ExceptionResponse(
                ex.getMessage(),
                status.value(),
                request.getDescription(false).replace("uri=", ""),
                OffsetDateTime.now().toString(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest request
    ) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ExceptionResponse response = new ExceptionResponse(
                ex.getMessage(),
                status.value(),
                request.getDescription(false).replace("uri=", ""),
                OffsetDateTime.now().toString(),
                status.getReasonPhrase()
        );
        return ResponseEntity.status(status).body(response);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        ValidationError err = new ValidationError(
                "Validation error",
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                extractPath(request),
                OffsetDateTime.now().toString(),
                HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase()
        );

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> err.addError(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
    }

    private String extractPath(WebRequest request) {
        if (request instanceof ServletWebRequest servletWebRequest) {
            return servletWebRequest.getRequest().getRequestURI();
        }
        return request.getDescription(false).replace("uri=", "");
    }
}