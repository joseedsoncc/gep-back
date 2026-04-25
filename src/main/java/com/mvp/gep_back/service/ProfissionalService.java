package com.mvp.gep_back.service;

import com.mvp.gep_back.model.dto.ProfissionalDTO;
import com.mvp.gep_back.model.enums.CategoriaEnum;

import java.util.List;

public interface ProfissionalService {

    ProfissionalDTO cadastrar(ProfissionalDTO dto);

    List<ProfissionalDTO> listar(CategoriaEnum categoria);

}
