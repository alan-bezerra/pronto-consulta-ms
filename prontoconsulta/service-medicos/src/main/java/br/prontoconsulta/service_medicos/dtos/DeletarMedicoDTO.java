package br.prontoconsulta.service_medicos.dtos;

import jakarta.validation.constraints.NotBlank;

public record DeletarMedicoDTO(@NotBlank(message = "A senha é obrigatória.") String senha) {
}
