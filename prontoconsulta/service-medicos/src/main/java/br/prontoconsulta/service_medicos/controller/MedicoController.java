package br.prontoconsulta.service_medicos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import br.prontoconsulta.service_medicos.dtos.*;
import br.prontoconsulta.service_medicos.service.MedicosService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/medicos")
public class MedicoController {
  @Autowired
  private MedicosService medicosService;

  @GetMapping
  public List<MedicoDTO> listarMedicos() {
    return medicosService.listarTodos();
  }

  @GetMapping("/{id}")
  public MedicoDTO obterMedicoPorId(@PathVariable Long id) {
    return medicosService.obterPorId(id);
  }

  @PostMapping
  public void cadastrarMedico(@RequestBody @Valid CadastrarMedicoDTO data) {
    medicosService.cadastrar(data);
  }

  @PutMapping("/{id}")
  public void atualizarMedico(@PathVariable Long id, @RequestBody @Valid AtualizarMedicoDTO dados) {
    medicosService.atualizar(id, dados);
  }

  @DeleteMapping("/{id}")
  public void deletar(@PathVariable Long id, @RequestBody @Valid DeletarMedicoDTO dados) {
    medicosService.deletar(id, dados);
  }
}
