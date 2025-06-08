package br.prontoconsulta.service_consultas.dtos;

public record MedicoDTO(
    String id,
    String nome,
    String email,
    String crm,
    String especialidade,
    String telefone) {
}
