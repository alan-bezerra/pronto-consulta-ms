package br.prontoconsulta.service_consultas.dtos;

import jakarta.validation.constraints.NotBlank;

public record DeletarConsultaDTO(@NotBlank(message = "Obrigatório") String medicoId) {
}
