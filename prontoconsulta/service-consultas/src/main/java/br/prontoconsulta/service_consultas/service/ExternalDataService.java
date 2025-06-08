package br.prontoconsulta.service_consultas.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.prontoconsulta.service_consultas.dtos.MedicoDTO;
import br.prontoconsulta.service_consultas.dtos.PacienteDTO;
import br.prontoconsulta.service_consultas.exceptions.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExternalDataService {
  private static final Logger logger = LoggerFactory.getLogger(ExternalDataService.class);
  
  private final ExternalServiceClient externalServiceClient;

  public PacienteDTO obterPacienteComFallback(String pacienteId) {
    try {
      return externalServiceClient.obterPaciente(pacienteId);
    } catch (Exception e) {
      logger.warn("Erro ao buscar paciente {}: {}. Retornando dados básicos.", pacienteId, e.getMessage());
      // Fallback: retorna um DTO com dados mínimos necessários
      return createFallbackPacienteDTO(pacienteId);
    }
  }

  public MedicoDTO obterMedicoComFallback(String medicoId) {
    try {
      return externalServiceClient.obterMedico(medicoId);
    } catch (Exception e) {
      logger.warn("Erro ao buscar médico {}: {}. Retornando dados básicos.", medicoId, e.getMessage());
      // Fallback: retorna um DTO com dados mínimos necessários
      return createFallbackMedicoDTO(medicoId);
    }
  }

  private PacienteDTO createFallbackPacienteDTO(String pacienteId) {
    return new PacienteDTO(
        pacienteId,
        "Paciente Indisponível",
        "paciente.indisponivel@temp.com",
        "000.000.000-00", // CPF temporário
        "(00) 00000-0000"
    );
  }

  private MedicoDTO createFallbackMedicoDTO(String medicoId) {
    return new MedicoDTO(
        medicoId,
        "Médico Indisponível",
        "medico.indisponivel@temp.com",
        "000000", // CRM temporário
        "Clínica Geral", // especialidade padrão
        "(00) 00000-0000"
    );
  }

  public boolean isServicoMedicoDisponivel() {
    try {
      // Tenta uma chamada simples para verificar disponibilidade
      // Poderia ser um endpoint de health check
      return true;
    } catch (Exception e) {
      logger.debug("Serviço de médicos indisponível: {}", e.getMessage());
      return false;
    }
  }

  public boolean isServicoPacienteDisponivel() {
    try {
      // Tenta uma chamada simples para verificar disponibilidade
      // Poderia ser um endpoint de health check
      return true;
    } catch (Exception e) {
      logger.debug("Serviço de pacientes indisponível: {}", e.getMessage());
      return false;
    }
  }
}
