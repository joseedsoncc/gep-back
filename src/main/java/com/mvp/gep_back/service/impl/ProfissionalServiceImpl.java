package com.mvp.gep_back.service.impl;

import com.mvp.gep_back.exception.NegocioException;
import com.mvp.gep_back.model.dto.ProfissionalDTO;
import com.mvp.gep_back.model.entity.Profissional;
import com.mvp.gep_back.model.enums.CategoriaEnum;
import com.mvp.gep_back.repository.ProfissionalRepository;
import com.mvp.gep_back.service.ProfissionalService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class ProfissionalServiceImpl implements ProfissionalService {

    private ProfissionalRepository repository;

    @Override
    @Transactional
    public ProfissionalDTO cadastrar(ProfissionalDTO dto) {
        if (repository.existsByRegistro(dto.getRegistro())) {
            throw new NegocioException("Já existe um profissional com este CRM/COREN");
        }

        Profissional profissional = new Profissional();
        profissional.setNome(dto.getNome());
        profissional.setRegistro(dto.getRegistro());
        profissional.setCategoria(dto.getCategoria());
        profissional.setCargaHorariaSemanal(dto.getCargaHorariaSemanal());

        profissional = repository.save(profissional);
        return converterParaDTO(profissional);
    }

    @Override
    public List<ProfissionalDTO> listar(CategoriaEnum categoria) {
        List<Profissional> profissionais;
        if (nonNull(categoria)) {
            profissionais = repository.findByCategoria(categoria);
        } else {
            profissionais = repository.findAll();
        }
        return profissionais.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    private ProfissionalDTO converterParaDTO(Profissional profissional) {
        ProfissionalDTO dto = new ProfissionalDTO();
        dto.setId(profissional.getId());
        dto.setNome(profissional.getNome());
        dto.setRegistro(profissional.getRegistro());
        dto.setCategoria(profissional.getCategoria());
        dto.setCargaHorariaSemanal(profissional.getCargaHorariaSemanal());
        return dto;
    }

}
