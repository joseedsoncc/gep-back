package com.mvp.gep_back.controller;

import com.mvp.gep_back.model.dto.EscalaPlantaoDTO;
import com.mvp.gep_back.model.dto.EscalaSemanalDTO;
import com.mvp.gep_back.service.EscalaPlantaoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/gep-back/escala")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class EscalaPlantaoController {

    private EscalaPlantaoService service;

    @PostMapping("/plantao")
    public ResponseEntity<EscalaPlantaoDTO> cadastrarPlantao(@Valid @RequestBody EscalaPlantaoDTO dto) {
        EscalaPlantaoDTO plantao = service.cadastrarPlantao(dto);
        return status(CREATED).body(plantao);
    }

    @DeleteMapping("/plantao/{id}")
    public ResponseEntity<Void> excluirPlantao(@PathVariable Long id) {
        service.excluirPlantao(id);
        return noContent().build();
    }

    @GetMapping("/semanal")
    public ResponseEntity<EscalaSemanalDTO> obterEscalaSemanal(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial) {
        EscalaSemanalDTO escala = service.obterEscalaSemanal(dataInicial);
        return ok(escala);
    }

}
