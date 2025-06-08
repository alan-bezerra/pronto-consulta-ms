package br.prontoconsulta.service_consultas.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import br.prontoconsulta.service_consultas.dtos.MedicoDTO;
import br.prontoconsulta.service_consultas.dtos.PacienteDTO;
import br.prontoconsulta.service_consultas.exceptions.ResourceNotFoundException;
import br.prontoconsulta.service_consultas.exceptions.ServiceUnavailableException;

@Service
public class ExternalServiceClient {
  private static final Logger logger = LoggerFactory.getLogger(ExternalServiceClient.class);
  
  @Autowired
  private RestTemplate restTemplate;
  
  @Autowired
  private DiscoveryClient discoveryClient;

  /**
   * Verifica se um serviço está registrado e disponível no Eureka
   */
  private boolean isServiceAvailable(String serviceName) {
    try {
      List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
      return !instances.isEmpty();
    } catch (Exception e) {
      logger.warn("Erro ao verificar disponibilidade do serviço {}: {}", serviceName, e.getMessage());
      return false;
    }
  }

  public PacienteDTO obterPaciente(String pacienteId) {
    if (!isServiceAvailable("service-pacientes")) {
      throw new ServiceUnavailableException("Serviço de pacientes não está disponível no momento");
    }

    try {
      PacienteDTO paciente = restTemplate.getForObject(
          "http://service-pacientes/pacientes/" + pacienteId, 
          PacienteDTO.class
      );
      
      if (paciente == null) {
        throw new ResourceNotFoundException("Paciente não encontrado com o ID: " + pacienteId);
      }

      return paciente;
      
    } catch (HttpClientErrorException e) {
      if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        throw new ResourceNotFoundException("Paciente não encontrado com o ID: " + pacienteId);
      } 

      if (e.getStatusCode().is4xxClientError()) {
        throw new IllegalArgumentException("Dados inválidos para buscar paciente: " + e.getMessage());
      } 

      throw new ServiceUnavailableException("Erro ao acessar o serviço de pacientes: " + e.getMessage());
    } 
  }

  public MedicoDTO obterMedico(String medicoId) {
    if (!isServiceAvailable("service-medicos")) {
      throw new ServiceUnavailableException("Serviço de médicos não está disponível no momento");
    }

    try {
      MedicoDTO medico = restTemplate.getForObject(
          "http://service-medicos/medicos/" + medicoId, 
          MedicoDTO.class
      );
      
      if (medico == null) {
        throw new ResourceNotFoundException("Médico não encontrado com o ID: " + medicoId);
      }

      return medico;
      
    } catch (HttpClientErrorException e) {
      if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        throw new ResourceNotFoundException("Médico não encontrado com o ID: " + medicoId);
      } 
      
      if (e.getStatusCode().is4xxClientError()) {
        throw new IllegalArgumentException("Dados inválidos para buscar médicos: " + e.getMessage());
      } 

      throw new ServiceUnavailableException("Erro ao acessar o serviço de médicos: " + e.getMessage());
    } 
  }
}
