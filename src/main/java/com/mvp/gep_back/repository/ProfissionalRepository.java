package com.mvp.gep_back.repository;

import com.mvp.gep_back.model.entity.Profissional;
import com.mvp.gep_back.model.enums.CategoriaEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {

    List<Profissional> findByCategoria(CategoriaEnum categoria);

    boolean existsByRegistro(String registro);

}
