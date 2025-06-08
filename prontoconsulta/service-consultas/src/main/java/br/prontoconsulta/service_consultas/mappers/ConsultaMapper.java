package br.prontoconsulta.service_consultas.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.prontoconsulta.service_consultas.dtos.ConsultaDTO;
import br.prontoconsulta.service_consultas.dtos.MedicoDTO;
import br.prontoconsulta.service_consultas.dtos.PacienteDTO;
import br.prontoconsulta.service_consultas.entity.Consulta;

@Mapper(componentModel = "spring")
public interface ConsultaMapper {
  
  default ConsultaDTO toDTO(Consulta entity, PacienteDTO paciente, MedicoDTO medico) {
    ConsultaDTO baseDto = toDTOWithoutExternalData(entity);
    return new ConsultaDTO(
        baseDto.id(),
        baseDto.datetime(),
        baseDto.status(),
        baseDto.canceladaPor(),
        paciente,
        medico
    );
  }
  
  @Mapping(target = "paciente", ignore = true)
  @Mapping(target = "medico", ignore = true)
  ConsultaDTO toDTOWithoutExternalData(Consulta entity);
  
  @Mapping(target = "pacienteId", source = "paciente.id")
  @Mapping(target = "medicoId", source = "medico.id")
  Consulta toDomain(ConsultaDTO dto);
}
