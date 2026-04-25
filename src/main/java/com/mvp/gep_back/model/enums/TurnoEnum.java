package com.mvp.gep_back.model.enums;

public enum TurnoEnum {

    MANHA("07:00-13:00", 6),
    TARDE("13:00-19:00", 6),
    NOITE("19:00-07:00", 12);

    private String horario;
    private int duracaoHoras;

    TurnoEnum(String horario, int duracaoHoras) {
        this.horario = horario;
        this.duracaoHoras = duracaoHoras;
    }

    public String getHorario() {
        return horario;
    }

    public int getDuracaoHoras() {
        return duracaoHoras;
    }

}
