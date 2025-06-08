package br.prontoconsulta.service_consultas.dtos;

import java.time.LocalDateTime;

import br.prontoconsulta.service_consultas.entity.StatusConsulta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastrarConsultaDTO(
    @NotBlank(message = "O ID do médico é obrigatório.") String medicoId,
    @NotBlank(message = "O ID do paciente é obrigatório.") String pacienteId,
    StatusConsulta status,
    @NotNull(message = "A data e hora da consulta são obrigatórias.") LocalDateTime datetime) {
}
