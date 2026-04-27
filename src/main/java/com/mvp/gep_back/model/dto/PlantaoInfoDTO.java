package com.mvp.gep_back.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantaoInfoDTO {

    private Long id;
    private String turno;
    private String turnoLabel;

}
