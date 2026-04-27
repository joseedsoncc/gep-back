package com.mvp.gep_back.model.entity;

import com.mvp.gep_back.model.enums.CategoriaEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "GEP_PROFISSIONAL")
public class Profissional implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @Column(nullable = false, unique = true)
    private String registro;

    @Enumerated(STRING)
    @Column(nullable = false)
    private CategoriaEnum categoria;

    @Min(value = 20, message = "Carga horária mínima de 20 horas semanais")
    @Max(value = 40, message = "Carga horária máxima de 40 horas semanais")
    @Column(name = "carga_horaria_semanal", nullable = false)
    private int cargaHorariaSemanal;

    @OneToMany(mappedBy = "profissional", cascade = ALL)
    private List<EscalaPlantao> plantoes;

}
