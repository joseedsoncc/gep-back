package com.mvp.gep_back.service;

import com.mvp.gep_back.model.dto.EscalaPlantaoDTO;
import com.mvp.gep_back.model.dto.EscalaSemanalDTO;

import java.time.LocalDate;

public interface EscalaPlantaoService {

    EscalaPlantaoDTO cadastrarPlantao(EscalaPlantaoDTO dto);

    void excluirPlantao(Long id);

    EscalaSemanalDTO obterEscalaSemanal(LocalDate dataInicial);

}
