package br.prontoconsulta.service_pacientes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.prontoconsulta.service_pacientes.entity.Paciente;

public interface PacientesRepository extends JpaRepository<Paciente, Long> {
  Optional<Paciente> findByCpf(String cpf);

  Optional<Paciente> findByEmail(String email);
}
