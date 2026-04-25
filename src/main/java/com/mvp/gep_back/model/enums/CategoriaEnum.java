package com.mvp.gep_back.model.enums;

public enum CategoriaEnum {

    MEDICO("Médico"),
    ENFERMEIRO("Enfermeiro"),
    TECNICO("Técnico");

    private String descricao;

    CategoriaEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

}
