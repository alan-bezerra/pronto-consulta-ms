package br.prontoconsulta.service_medicos.mappers;

import org.mapstruct.Mapper;

import br.prontoconsulta.service_medicos.dtos.MedicoDTO;
import br.prontoconsulta.service_medicos.entity.Medico;

@Mapper(componentModel = "spring")
public interface MedicoMapper {
  Medico toDomain(MedicoDTO dto);

  MedicoDTO toDTO(Medico entity);
}
