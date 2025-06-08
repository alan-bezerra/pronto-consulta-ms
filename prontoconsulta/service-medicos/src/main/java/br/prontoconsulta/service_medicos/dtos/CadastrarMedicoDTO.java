package br.prontoconsulta.service_medicos.dtos;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CadastrarMedicoDTO(
    @NotBlank(message = "O nome é obrigatório.") String nome,
    @NotBlank(message = "O e-mail é obrigatório.") @Email(message = "E-mail inválido") String email,
    @NotBlank(message = "A senha é obrigatória.") String senha,

    @NotBlank(message = "O CPF é obrigatório.") @Length(min = 11, max = 11, message = "CPF Inválido") String cpf,
    @NotBlank(message = "O telefone é obrigatório.") String telefone,
    @NotBlank(message = "O endereço é obrigatório.") String endereco,

    @NotBlank(message = "O CRM é obrigatório.") String crm,
    @NotBlank(message = "A especialidade é obrigatória.") String especialidade) {
}
