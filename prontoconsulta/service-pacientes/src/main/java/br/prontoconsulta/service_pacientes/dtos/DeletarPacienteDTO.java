package br.prontoconsulta.service_pacientes.dtos;

import jakarta.validation.constraints.NotBlank;

public record DeletarPacienteDTO(@NotBlank(message = "A senha é obrigatória.") String senha) {
}
