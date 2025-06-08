package br.prontoconsulta.service_pacientes.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends HttpApiException {
  public ResourceNotFoundException(String message) {
    super(HttpStatus.NOT_FOUND.value(), message);
  }

  public ResourceNotFoundException() {
    super(HttpStatus.NOT_FOUND.value(), "Recurso não encontrado");
  }
}
