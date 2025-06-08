package br.prontoconsulta.service_consultas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.prontoconsulta.service_consultas.entity.Consulta;
import br.prontoconsulta.service_consultas.entity.StatusConsulta;

public interface ConsultasRepository extends JpaRepository<Consulta, Long> {
  List<Consulta> findByPacienteId(String pacienteId);

  List<Consulta> findByMedicoId(String medicoId);

  List<Consulta> findByStatus(StatusConsulta status);

  List<Consulta> findByPacienteIdAndStatus(String pacienteId, StatusConsulta status);

  List<Consulta> findByMedicoIdAndStatus(String medicoId, StatusConsulta status);
}
