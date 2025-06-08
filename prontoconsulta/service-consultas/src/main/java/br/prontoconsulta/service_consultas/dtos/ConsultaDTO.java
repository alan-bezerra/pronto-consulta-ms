package br.prontoconsulta.service_consultas.dtos;

import java.time.LocalDateTime;

import br.prontoconsulta.service_consultas.entity.EntidadesPermissaoCancelar;
import br.prontoconsulta.service_consultas.entity.StatusConsulta;

public record ConsultaDTO(
    Long id,
    LocalDateTime datetime,
    StatusConsulta status,
    EntidadesPermissaoCancelar canceladaPor,
    PacienteDTO paciente,
    MedicoDTO medico) {
}
