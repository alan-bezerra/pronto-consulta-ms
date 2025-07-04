package br.prontoconsulta.service_medicos.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class HttpApiException extends RuntimeException {
  private int status;
  private String message;
}
