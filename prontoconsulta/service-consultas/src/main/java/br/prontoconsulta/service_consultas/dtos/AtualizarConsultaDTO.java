package br.prontoconsulta.service_consultas.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizarConsultaDTO(
    @NotBlank(message = "O ID do médico é obrigatório.") String medicoId,
    @NotBlank(message = "O ID do paciente é obrigatório.") String pacienteId,
    @NotNull(message = "A data e hora da consulta são obrigatórias.") LocalDateTime datetime) {
}
