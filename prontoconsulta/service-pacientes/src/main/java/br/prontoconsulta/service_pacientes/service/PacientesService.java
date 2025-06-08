package br.prontoconsulta.service_pacientes.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.prontoconsulta.service_pacientes.dtos.AtualizarPacienteDTO;
import br.prontoconsulta.service_pacientes.dtos.CadastrarPacienteDTO;
import br.prontoconsulta.service_pacientes.dtos.DeletarPacienteDTO;
import br.prontoconsulta.service_pacientes.dtos.PacienteDTO;
import br.prontoconsulta.service_pacientes.entity.Paciente;
import br.prontoconsulta.service_pacientes.exceptions.ResourceNotFoundException;
import br.prontoconsulta.service_pacientes.mappers.PacienteMapper;
import br.prontoconsulta.service_pacientes.repository.PacientesRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PacientesService {
  private final PacientesRepository pacientesRepository;
  private final PasswordEncoder passwordEncoder;
  private final PacienteMapper pacienteMapper;

  public void cadastrar(CadastrarPacienteDTO data) {
    String cpfFormatado = data.cpf().replaceAll("[^0-9]", "");
    String telefoneFormatado = data.telefone().replaceAll("[^0-9]", "");
    String emailFormatado = data.email().trim().toLowerCase();

    var pacienteComCpfExiste = pacientesRepository.findByCpf(cpfFormatado);
    if (pacienteComCpfExiste.isPresent()) {
      throw new IllegalArgumentException("Já existe um paciente cadastrado com o CPF informado.");
    }

    var pacienteComEmailExiste = pacientesRepository.findByEmail(emailFormatado);
    if (pacienteComEmailExiste.isPresent()) {
      throw new IllegalArgumentException("Já existe um paciente cadastrado com o e-mail informado.");
    }

    String hashedSenha = passwordEncoder.encode(data.senha());
    Paciente novoPaciente = new Paciente();
    novoPaciente.setNome(data.nome());
    novoPaciente.setCpf(cpfFormatado);
    novoPaciente.setEmail(emailFormatado);
    novoPaciente.setSenha(hashedSenha);
    novoPaciente.setTelefone(telefoneFormatado);
    novoPaciente.setEndereco(data.endereco());

    pacientesRepository.save(novoPaciente);
  }

  public PacienteDTO obterPorId(Long id) {
    var paciente = pacientesRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com o ID informado."));

    return pacienteMapper.toDTO(paciente);
  }

  public List<PacienteDTO> listarTodos() {
    List<Paciente> pacientes = pacientesRepository.findAll();
    return pacientes.stream().map(pacienteMapper::toDTO).toList();
  }

  public void atualizar(Long id, AtualizarPacienteDTO data) {
    var paciente = pacientesRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com o ID informado."));

    if (data.nome() != null) {
      paciente.setNome(data.nome());
    }

    if (data.telefone() != null) {
      paciente.setTelefone(data.telefone().replaceAll("[^0-9]", ""));
    }

    if (data.endereco() != null) {
      paciente.setEndereco(data.endereco());
    }

    pacientesRepository.save(paciente);
  }

  public void deletar(Long id, DeletarPacienteDTO dados) {
    var paciente = pacientesRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado com o ID informado."));

    boolean isSenhaCorreta = passwordEncoder.matches(dados.senha(), paciente.getSenha());
    if (!isSenhaCorreta) {
      throw new IllegalArgumentException("Senha incorreta. Não foi possível deletar o paciente.");
    }

    pacientesRepository.delete(paciente);
  }
}
