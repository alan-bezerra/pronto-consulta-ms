package br.prontoconsulta.service_consultas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.prontoconsulta.service_consultas.dtos.AtualizarConsultaDTO;
import br.prontoconsulta.service_consultas.dtos.CadastrarConsultaDTO;
import br.prontoconsulta.service_consultas.dtos.ConsultaCriadaDTO;
import br.prontoconsulta.service_consultas.dtos.ConsultaDTO;
import br.prontoconsulta.service_consultas.dtos.DeletarConsultaDTO;
import br.prontoconsulta.service_consultas.entity.StatusConsulta;
import br.prontoconsulta.service_consultas.service.ConsultasService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {
  @Autowired
  private ConsultasService consultasService;

  @GetMapping
  public List<ConsultaDTO> listarConsultas() {
    return consultasService.listarTodos();
  }

  @GetMapping("/{id}")
  public ConsultaDTO obterConsultaPorId(@PathVariable Long id) {
    return consultasService.obterPorId(id);
  }

  @PostMapping
  public ConsultaCriadaDTO cadastrarConsulta(@RequestBody @Valid CadastrarConsultaDTO data) {
    return consultasService.cadastrar(data);
  }

  @PutMapping("/{id}")
  public void atualizarConsulta(@PathVariable Long id, @RequestBody @Valid AtualizarConsultaDTO dados) {
    consultasService.atualizar(id, dados);
  }

  @DeleteMapping("/{id}")
  public void deletar(@PathVariable Long id, @RequestBody @Valid DeletarConsultaDTO dados) {
    consultasService.deletar(id, dados);
  }

  @GetMapping("/paciente/{pacienteId}")
  public List<ConsultaDTO> listarConsultasPorPaciente(@PathVariable String pacienteId) {
    return consultasService.listarPorPaciente(pacienteId);
  }

  @GetMapping("/medico/{medicoId}")
  public List<ConsultaDTO> listarConsultasPorMedico(@PathVariable String medicoId) {
    return consultasService.listarPorMedico(medicoId);
  }

  @GetMapping("/status/{status}")
  public List<ConsultaDTO> listarConsultasPorStatus(@PathVariable("status") String status) {
    var statusEnum = StatusConsulta.valueOf(status.toUpperCase());
    return consultasService.listarPorStatus(statusEnum);
  }

  @GetMapping("/paciente/{pacienteId}/status/{status}")
  public List<ConsultaDTO> listarConsultasPorPacienteEStatus(
      @PathVariable("pacienteId") String pacienteId, @PathVariable("status") String status) {

    var statusEnum = StatusConsulta.valueOf(status.toUpperCase());
    return consultasService.listarPorPacienteEStatus(pacienteId, statusEnum);
  }

  @GetMapping("/medico/{medicoId}/status/{status}")
  public List<ConsultaDTO> listarConsultasPorMedicoEStatus(
      @PathVariable("medicoId") String medicoId, @PathVariable("status") String status) {

    var statusEnum = StatusConsulta.valueOf(status.toUpperCase());
    return consultasService.listarPorMedicoEStatus(medicoId, statusEnum);
  }

  @PatchMapping("/{id}/cancelar")
  public void cancelarConsulta(@PathVariable Long id, @RequestParam String canceladaPor) {
    consultasService.cancelar(id, canceladaPor);
  }

  @PatchMapping("/{id}/realizar")
  public void marcarComoRealizada(@PathVariable Long id) {
    consultasService.marcarComoRealizada(id);
  }
}
