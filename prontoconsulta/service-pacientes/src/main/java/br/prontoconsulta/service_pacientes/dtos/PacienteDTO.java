package br.prontoconsulta.service_pacientes.dtos;

public record PacienteDTO(Long id, String nome, String email, String telefone, String cpf, String endereco) {
}
