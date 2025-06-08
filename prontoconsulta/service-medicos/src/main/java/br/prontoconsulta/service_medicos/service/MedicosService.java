package br.prontoconsulta.service_medicos.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.prontoconsulta.service_medicos.dtos.AtualizarMedicoDTO;
import br.prontoconsulta.service_medicos.dtos.CadastrarMedicoDTO;
import br.prontoconsulta.service_medicos.dtos.DeletarMedicoDTO;
import br.prontoconsulta.service_medicos.dtos.MedicoDTO;
import br.prontoconsulta.service_medicos.entity.Medico;
import br.prontoconsulta.service_medicos.exceptions.ResourceNotFoundException;
import br.prontoconsulta.service_medicos.mappers.MedicoMapper;
import br.prontoconsulta.service_medicos.repository.MedicosRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicosService {
  private final MedicosRepository medicosRepository;
  private final PasswordEncoder passwordEncoder;
  private final MedicoMapper medicoMapper;

  public void cadastrar(CadastrarMedicoDTO data) {
    String cpfFormatado = data.cpf().replaceAll("[^0-9]", "");
    String telefoneFormatado = data.telefone().replaceAll("[^0-9]", "");
    String emailFormatado = data.email().trim().toLowerCase();

    var medicoComCpfExiste = medicosRepository.findByCpf(cpfFormatado);
    if (medicoComCpfExiste.isPresent()) {
      throw new IllegalArgumentException("Já existe um medico cadastrado com o CPF informado.");
    }

    var medicoComEmailExiste = medicosRepository.findByEmail(emailFormatado);
    if (medicoComEmailExiste.isPresent()) {
      throw new IllegalArgumentException("Já existe um medico cadastrado com o e-mail informado.");
    }

    var medicoComCrmExiste = medicosRepository.findByCrm(data.crm());
    if (medicoComCrmExiste.isPresent()) {
      throw new IllegalArgumentException("Já existe um medico cadastrado com o CRM informado.");
    }

    String hashedSenha = passwordEncoder.encode(data.senha());
    Medico novoMedico = new Medico();
    novoMedico.setNome(data.nome());
    novoMedico.setCpf(cpfFormatado);
    novoMedico.setEmail(emailFormatado);
    novoMedico.setSenha(hashedSenha);
    novoMedico.setTelefone(telefoneFormatado);
    novoMedico.setEndereco(data.endereco());
    novoMedico.setCrm(data.crm());
    novoMedico.setEspecialidade(data.especialidade());

    medicosRepository.save(novoMedico);
  }

  public MedicoDTO obterPorId(Long id) {
    var medico = medicosRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Medico não encontrado com o ID informado."));

    return medicoMapper.toDTO(medico);
  }

  public List<MedicoDTO> listarTodos() {
    List<Medico> medicos = medicosRepository.findAll();
    return medicos.stream().map(medicoMapper::toDTO).toList();
  }

  public void atualizar(Long id, AtualizarMedicoDTO data) {
    var medico = medicosRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Medico não encontrado com o ID informado."));

    if (data.nome() != null) {
      medico.setNome(data.nome());
    }

    if (data.telefone() != null) {
      medico.setTelefone(data.telefone().replaceAll("[^0-9]", ""));
    }

    if (data.endereco() != null) {
      medico.setEndereco(data.endereco());
    }

    medicosRepository.save(medico);
  }

  public void deletar(Long id, DeletarMedicoDTO dados) {
    var medico = medicosRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Medico não encontrado com o ID informado."));

    boolean isSenhaCorreta = passwordEncoder.matches(dados.senha(), medico.getSenha());
    if (!isSenhaCorreta) {
      throw new IllegalArgumentException("Senha incorreta. Não foi possível deletar o medico.");
    }

    medicosRepository.delete(medico);
  }
}
