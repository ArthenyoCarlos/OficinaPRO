package br.com.oficinapro.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ExceptionResponse {
    private String message;
    private int status;
    private String path;
    private String timestamp;
    private String error;
}
