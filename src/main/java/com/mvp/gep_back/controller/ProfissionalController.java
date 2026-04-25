package com.mvp.gep_back.controller;

import com.mvp.gep_back.model.dto.ProfissionalDTO;
import com.mvp.gep_back.model.enums.CategoriaEnum;
import com.mvp.gep_back.service.ProfissionalService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gep-back/profissional")
//@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class ProfissionalController {

    //@Autowired
    private ProfissionalService service;

    @PostMapping
    public ResponseEntity<ProfissionalDTO> cadastrar(@Valid @RequestBody ProfissionalDTO dto) {
        ProfissionalDTO profissional = service.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(profissional);
    }

    @GetMapping
    public ResponseEntity<List<ProfissionalDTO>> listar() { //@RequestParam(required = false) CategoriaEnum categoria) {
        List<ProfissionalDTO> profissionais = service.listar(null); //categoria);
        return ResponseEntity.ok(profissionais);
    }

}
