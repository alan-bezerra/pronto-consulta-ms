package br.prontoconsulta.service_medicos.dtos;

public record MedicoDTO(Long id, String nome, String email, String telefone, String cpf, String endereco, String crm,
    String especialidade) {
}
