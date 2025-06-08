package br.prontoconsulta.service_consultas.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.prontoconsulta.service_consultas.dtos.AtualizarConsultaDTO;
import br.prontoconsulta.service_consultas.dtos.CadastrarConsultaDTO;
import br.prontoconsulta.service_consultas.dtos.ConsultaCriadaDTO;
import br.prontoconsulta.service_consultas.dtos.ConsultaDTO;
import br.prontoconsulta.service_consultas.dtos.DeletarConsultaDTO;
import br.prontoconsulta.service_consultas.entity.Consulta;
import br.prontoconsulta.service_consultas.entity.EntidadesPermissaoCancelar;
import br.prontoconsulta.service_consultas.entity.StatusConsulta;
import br.prontoconsulta.service_consultas.exceptions.ResourceNotFoundException;
import br.prontoconsulta.service_consultas.exceptions.UnauthorizedException;
import br.prontoconsulta.service_consultas.mappers.ConsultaMapper;
import br.prontoconsulta.service_consultas.repository.ConsultasRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsultasService {
  private final ConsultasRepository consultasRepository;
  private final ConsultaMapper consultaMapper;
  private final ExternalServiceClient externalServiceClient;

  public ConsultaCriadaDTO cadastrar(CadastrarConsultaDTO data) {
    externalServiceClient.obterPaciente(data.pacienteId());
    externalServiceClient.obterMedico(data.medicoId());

    Consulta novoConsulta = new Consulta();
    novoConsulta.setDatetime(data.datetime());

    novoConsulta.setStatus(data.status() == null ? StatusConsulta.AGENDADA : data.status());
    novoConsulta.setPacienteId(data.pacienteId());
    novoConsulta.setMedicoId(data.medicoId());

    Consulta consultaSalva = consultasRepository.save(novoConsulta);
    
    return new ConsultaCriadaDTO(consultaSalva.getId(), "Consulta agendada com sucesso!");
  }

  public ConsultaDTO obterPorId(Long id) {
    var consulta = consultasRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrado com o ID informado."));

    var paciente = externalServiceClient.obterPaciente(consulta.getPacienteId());
    var medico = externalServiceClient.obterMedico(consulta.getMedicoId());

    return consultaMapper.toDTO(consulta, paciente, medico);
  }

  public List<ConsultaDTO> listarTodos() {
    List<Consulta> consultas = consultasRepository.findAll();

    return consultas.stream()
        .map(consulta -> {
          var paciente = externalServiceClient.obterPaciente(consulta.getPacienteId());
          var medico = externalServiceClient.obterMedico(consulta.getMedicoId());
          return consultaMapper.toDTO(consulta, paciente, medico);
        })
        .toList();
  }

  public void cancelar(Long id, String canceladaPor) {
    var consulta = consultasRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrado com o ID informado."));

    if (consulta.getStatus() == StatusConsulta.CANCELADA) {
      throw new IllegalArgumentException("Esta consulta já foi cancelada.");
    }

    if (consulta.getStatus() == StatusConsulta.REALIZADA) {
      throw new IllegalArgumentException("Não é possível cancelar uma consulta já realizada.");
    }

    if (!canceladaPor.toUpperCase().equals("PACIENTE") && !canceladaPor.toUpperCase().equals("MEDICO")) {
      throw new UnauthorizedException("Você não tem permissão para cancelar esta consulta.");
    }

    var entidadeQueCancelou = EntidadesPermissaoCancelar.valueOf(canceladaPor.toUpperCase());
    
    consulta.setStatus(StatusConsulta.CANCELADA);
    consulta.setCanceladaPor(entidadeQueCancelou);

    consultasRepository.save(consulta);
  }

  public void marcarComoRealizada(Long id) {
    var consulta = consultasRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrado com o ID informado."));

    if (consulta.getStatus() == StatusConsulta.REALIZADA) {
      throw new IllegalArgumentException("Esta consulta já foi marcada como realizada.");
    }

    consulta.setStatus(StatusConsulta.REALIZADA);
    consultasRepository.save(consulta);
  }

  public void atualizar(Long id, AtualizarConsultaDTO dados) {
    var consulta = consultasRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrado com o ID informado."));

    if (consulta.getStatus() != StatusConsulta.AGENDADA) {
      throw new IllegalArgumentException("Não é possível atualizar a data de uma consulta que não está mais agendada.");
    }

    externalServiceClient.obterPaciente(dados.pacienteId());
    externalServiceClient.obterMedico(dados.medicoId());

    consulta.setDatetime(dados.datetime());
    consulta.setMedicoId(dados.medicoId());
    consulta.setPacienteId(dados.pacienteId());

    consultasRepository.save(consulta);
  }

  public void deletar(Long id, DeletarConsultaDTO dados) {
    var consulta = consultasRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrado com o ID informado."));

    if (consulta.getStatus() == StatusConsulta.REALIZADA) {
      throw new IllegalArgumentException("Não é possível deletar uma consulta já realizada.");
    }

    boolean isMedicoAuthorized = consulta.getMedicoId().equals(dados.medicoId());
    if (!isMedicoAuthorized) {
      throw new UnauthorizedException("Apenas um médico pode deletar uma consulta agendada.");
    }

    consultasRepository.delete(consulta);
  }

  public List<ConsultaDTO> listarPorPaciente(String pacienteId) {
    List<Consulta> consultas = consultasRepository.findByPacienteId(pacienteId);
    return consultas.stream()
        .map(consulta -> {
          var paciente = externalServiceClient.obterPaciente(consulta.getPacienteId());
          var medico = externalServiceClient.obterMedico(consulta.getMedicoId());
          return consultaMapper.toDTO(consulta, paciente, medico);
        })
        .toList();
  }

  public List<ConsultaDTO> listarPorMedico(String medicoId) {
    List<Consulta> consultas = consultasRepository.findByMedicoId(medicoId);
    return consultas.stream()
        .map(consulta -> {
          var paciente = externalServiceClient.obterPaciente(consulta.getPacienteId());
          var medico = externalServiceClient.obterMedico(consulta.getMedicoId());
          return consultaMapper.toDTO(consulta, paciente, medico);
        })
        .toList();
  }

  public List<ConsultaDTO> listarPorStatus(StatusConsulta status) {
    List<Consulta> consultas = consultasRepository.findByStatus(status);
    return consultas.stream()
        .map(consulta -> {
          var paciente = externalServiceClient.obterPaciente(consulta.getPacienteId());
          var medico = externalServiceClient.obterMedico(consulta.getMedicoId());
          return consultaMapper.toDTO(consulta, paciente, medico);
        })
        .toList();
  }

  public List<ConsultaDTO> listarPorPacienteEStatus(String pacienteId, StatusConsulta status) {
    List<Consulta> consultas = consultasRepository.findByPacienteIdAndStatus(pacienteId, status);
    return consultas.stream()
        .map(consulta -> {
          var paciente = externalServiceClient.obterPaciente(consulta.getPacienteId());
          var medico = externalServiceClient.obterMedico(consulta.getMedicoId());
          return consultaMapper.toDTO(consulta, paciente, medico);
        })
        .toList();
  }

  public List<ConsultaDTO> listarPorMedicoEStatus(String medicoId, StatusConsulta status) {
    List<Consulta> consultas = consultasRepository.findByMedicoIdAndStatus(medicoId, status);
    return consultas.stream()
        .map(consulta -> {
          var paciente = externalServiceClient.obterPaciente(consulta.getPacienteId());
          var medico = externalServiceClient.obterMedico(consulta.getMedicoId());
          return consultaMapper.toDTO(consulta, paciente, medico);
        })
        .toList();
  }
}
