package br.prontoconsulta.service_pacientes.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class HttpApiExceptionResponse {
  private int status;
  private String message;
}
