package br.com.oficinapro.common.exception.handler;

import br.com.oficinapro.common.exception.ExceptionResponse;
import br.com.oficinapro.common.exception.InvalidJwtAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
}
