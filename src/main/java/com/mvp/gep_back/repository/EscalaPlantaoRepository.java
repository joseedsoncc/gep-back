package com.mvp.gep_back.repository;

import com.mvp.gep_back.model.entity.EscalaPlantao;
import com.mvp.gep_back.model.entity.Profissional;
import com.mvp.gep_back.model.enums.TurnoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EscalaPlantaoRepository extends JpaRepository<EscalaPlantao, Long> {

    Optional<EscalaPlantao> findByProfissionalAndDataAndTurno(Profissional profissional, LocalDate data, TurnoEnum turno);

    @Query("SELECT p FROM EscalaPlantao p WHERE p.data BETWEEN :inicio AND :fim")
    List<EscalaPlantao> findBySemana(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query("SELECT SUM(CASE WHEN p.turno = 'NOITE' THEN 12 ELSE 6 END) " +
            "FROM EscalaPlantao p WHERE p.profissional = :profissional " +
            "AND p.data BETWEEN :inicio AND :fim")
    Integer calcularHorasSemana(@Param("profissional") Profissional profissional,
                                @Param("inicio") LocalDate inicio,
                                @Param("fim") LocalDate fim);

}
