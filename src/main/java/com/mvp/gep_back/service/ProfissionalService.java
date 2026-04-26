package com.mvp.gep_back.service;

import com.mvp.gep_back.model.dto.ProfissionalDTO;

import java.util.List;

public interface ProfissionalService {

    ProfissionalDTO cadastrar(ProfissionalDTO dto);

    ProfissionalDTO editar(Long id, ProfissionalDTO dto);

    void excluirPorId(Long id);

    List<ProfissionalDTO> listar(String categoria);

    ProfissionalDTO buscarPorId(Long id);

}
