package br.prontoconsulta.service_consultas.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends HttpApiException {
  public UnauthorizedException(String message) {
    super(HttpStatus.UNAUTHORIZED.value(), message);
  }

  public UnauthorizedException() {
    super(HttpStatus.UNAUTHORIZED.value(), "Não autorizado");
  }
}
