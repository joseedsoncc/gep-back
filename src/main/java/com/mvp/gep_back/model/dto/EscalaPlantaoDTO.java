package com.mvp.gep_back.model.dto;

import com.mvp.gep_back.model.enums.TurnoEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class EscalaPlantaoDTO {

    private Long id;
    private Long profissionalId;
    private String profissionalNome;
    private LocalDate data;
    private TurnoEnum turno;

}
