package com.mvp.gep_back.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class EscalaSemanalDTO {

    private LocalDate inicioSemana;
    private LocalDate fimSemana;
    private Map<Long, Map<String, List<PlantaoInfoDTO>>> escala;
    private Map<Long, Boolean> limiteAtingido;

}
