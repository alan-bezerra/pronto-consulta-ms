package br.prontoconsulta.service_consultas.dtos;

public record PacienteDTO(
    String id,
    String nome,
    String email,
    String cpf,
    String telefone) {
}
