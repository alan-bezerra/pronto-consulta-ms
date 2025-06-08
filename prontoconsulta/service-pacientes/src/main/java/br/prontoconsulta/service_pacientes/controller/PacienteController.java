package br.prontoconsulta.service_pacientes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import br.prontoconsulta.service_pacientes.dtos.*;
import br.prontoconsulta.service_pacientes.service.PacientesService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {
  @Autowired
  private PacientesService pacientesService;

  @GetMapping
  public List<PacienteDTO> listarPacientes() {
    return pacientesService.listarTodos();
  }

  @GetMapping("/{id}")
  public PacienteDTO obterPacientePorId(@PathVariable Long id) {
    return pacientesService.obterPorId(id);
  }

  @PostMapping
  public void cadastrarPaciente(@RequestBody @Valid CadastrarPacienteDTO data) {
    pacientesService.cadastrar(data);
  }

  @PutMapping("/{id}")
  public void atualizarPaciente(@PathVariable Long id, @RequestBody @Valid AtualizarPacienteDTO dados) {
    pacientesService.atualizar(id, dados);
  }

  @DeleteMapping("/{id}")
  public void deletar(@PathVariable Long id, @RequestBody @Valid DeletarPacienteDTO dados) {
    pacientesService.deletar(id, dados);
  }
}
