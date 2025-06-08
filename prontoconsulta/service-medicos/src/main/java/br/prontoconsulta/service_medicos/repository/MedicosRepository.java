package br.prontoconsulta.service_medicos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.prontoconsulta.service_medicos.entity.Medico;

public interface MedicosRepository extends JpaRepository<Medico, Long> {
  Optional<Medico> findByCpf(String cpf);

  Optional<Medico> findByEmail(String email);

  Optional<Medico> findByCrm(String crm);
}
