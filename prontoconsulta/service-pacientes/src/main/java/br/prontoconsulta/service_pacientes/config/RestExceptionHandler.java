package br.prontoconsulta.service_pacientes.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.prontoconsulta.service_pacientes.exceptions.HttpApiExceptionResponse;
import br.prontoconsulta.service_pacientes.exceptions.HttpApiException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler(HttpApiException.class)
  private ResponseEntity<HttpApiExceptionResponse> httpApiErrorMessage(HttpApiException e) {
    return getResponse(e.getStatus(), e.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  private ResponseEntity<HttpApiExceptionResponse> illegalArgumentExceptionHandler(IllegalArgumentException e) {
    return getResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
  }

  @ExceptionHandler(Exception.class)
  private ResponseEntity<HttpApiExceptionResponse> runtimeExceptionHandler(Exception e) {
    return getResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
  }

  private ResponseEntity<HttpApiExceptionResponse> getResponse(int status, String message) {
    HttpApiExceptionResponse response = new HttpApiExceptionResponse(status, message);
    return ResponseEntity.status(status).body(response);
  }
}
