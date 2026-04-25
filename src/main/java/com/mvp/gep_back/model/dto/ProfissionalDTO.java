package com.mvp.gep_back.model.dto;

import com.mvp.gep_back.model.enums.CategoriaEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfissionalDTO {

    private Long id;
    private String nome;
    private String registro;
    private CategoriaEnum categoria;
    private int cargaHorariaSemanal;

}
