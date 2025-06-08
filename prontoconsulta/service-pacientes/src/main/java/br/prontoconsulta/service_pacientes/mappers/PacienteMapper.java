package br.prontoconsulta.service_pacientes.mappers;

import org.mapstruct.Mapper;

import br.prontoconsulta.service_pacientes.dtos.PacienteDTO;
import br.prontoconsulta.service_pacientes.entity.Paciente;

@Mapper(componentModel = "spring")
public interface PacienteMapper {
  Paciente toDomain(PacienteDTO dto);

  PacienteDTO toDTO(Paciente entity);
}
