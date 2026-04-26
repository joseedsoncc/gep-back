package com.mvp.gep_back.controller;

import com.mvp.gep_back.model.dto.ProfissionalDTO;
import com.mvp.gep_back.service.ProfissionalService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.accepted;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/gep-back/profissional")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class ProfissionalController {

    private ProfissionalService service;

    @PostMapping
    public ResponseEntity<ProfissionalDTO> cadastrar(@Valid @RequestBody ProfissionalDTO dto) {
        ProfissionalDTO profissional = service.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(profissional);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalDTO> editar(@PathVariable Long id, @Valid @RequestBody ProfissionalDTO dto) {
        return accepted().body(service.editar(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = NO_CONTENT)
    public void excluirPorId(@PathVariable Long id) {
        service.excluirPorId(id);
    }

    @GetMapping
    public ResponseEntity<List<ProfissionalDTO>> listar(@RequestParam(required = false) String categoria) {
        List<ProfissionalDTO> profissionais = service.listar(categoria);
        return ResponseEntity.ok(profissionais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalDTO> buscarPorId(@PathVariable Long id) {
        return status(HttpStatus.OK).body(service.buscarPorId(id));
    }

}
