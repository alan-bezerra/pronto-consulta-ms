package br.prontoconsulta.service_consultas.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "consultas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consulta {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private LocalDateTime datetime;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private StatusConsulta status;

  @Enumerated(EnumType.STRING)
  @Column(name = "cancelada_por", nullable = true)
  private EntidadesPermissaoCancelar canceladaPor;

  @Column(name = "medico_id", nullable = false)
  private String medicoId;

  @Column(name = "paciente_id", nullable = false)
  private String pacienteId;
}
