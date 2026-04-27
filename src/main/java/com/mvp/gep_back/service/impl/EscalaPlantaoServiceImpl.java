package com.mvp.gep_back.service.impl;

import com.mvp.gep_back.exception.NegocioException;
import com.mvp.gep_back.model.dto.EscalaPlantaoDTO;
import com.mvp.gep_back.model.dto.EscalaSemanalDTO;
import com.mvp.gep_back.model.dto.PlantaoInfoDTO;
import com.mvp.gep_back.model.entity.EscalaPlantao;
import com.mvp.gep_back.model.entity.Profissional;
import com.mvp.gep_back.repository.EscalaPlantaoRepository;
import com.mvp.gep_back.repository.ProfissionalRepository;
import com.mvp.gep_back.service.EscalaPlantaoService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class EscalaPlantaoServiceImpl implements EscalaPlantaoService {

    private EscalaPlantaoRepository repository;
    private ProfissionalRepository profissionalRepository;

    @Override
    @Transactional
    public EscalaPlantaoDTO cadastrarPlantao(EscalaPlantaoDTO dto) {

        Profissional profissional = profissionalRepository.findById(dto.getProfissionalId())
                .orElseThrow(() -> new NegocioException("Profissional não encontrado!"));

        repository.findByProfissionalAndDataAndTurno(profissional, dto.getData(), dto.getTurno())
                .ifPresent(p -> {
                    throw new NegocioException("Profissional já possui plantão neste dia e turno");
                });

        LocalDate inicioSemana = dto.getData().with(java.time.DayOfWeek.MONDAY);
        LocalDate fimSemana = inicioSemana.plusDays(6);

        Integer horasAlocadas = repository.calcularHorasSemana(profissional, inicioSemana, fimSemana);
        int horasAtuais = nonNull(horasAlocadas) ? horasAlocadas : 0;
        int horasNovoPlantao = dto.getTurno().getDuracaoHoras();

        if (horasAtuais + horasNovoPlantao > profissional.getCargaHorariaSemanal()) {
            throw new NegocioException(String.format(
                    "Carga horária semanal excedida! Limite: %dh, Atual: %dh, Tentativa: %dh",
                    profissional.getCargaHorariaSemanal(), horasAtuais, horasNovoPlantao
            ));
        }

        EscalaPlantao escalaPlantao = new EscalaPlantao();
        escalaPlantao.setProfissional(profissional);
        escalaPlantao.setData(dto.getData());
        escalaPlantao.setTurno(dto.getTurno());

        escalaPlantao = repository.save(escalaPlantao);

        return converterParaDTO(escalaPlantao);
    }

    @Override
    @Transactional
    public void excluirPlantao(Long id) {
        if (!repository.existsById(id)) {
            throw new NegocioException("Plantão não encontrado!");
        }
        repository.deleteById(id);
    }

    @Override
    public EscalaSemanalDTO obterEscalaSemanal(LocalDate dataInicial) {

        LocalDate inicioSemana = dataInicial;
        while (inicioSemana.getDayOfWeek().getValue() != 1) {
            inicioSemana = inicioSemana.minusDays(1);
        }
        LocalDate fimSemana = inicioSemana.plusDays(6);

        List<Profissional> profissionais = profissionalRepository.findAll();
        List<EscalaPlantao> plantoes = repository.findBySemana(inicioSemana, fimSemana);

        Map<Long, Map<String, List<PlantaoInfoDTO>>> escalaMap = new HashMap<>();
        Map<Long, Boolean> limiteAtingidoMap = new HashMap<>();

        for (Profissional profissional : profissionais) {
            escalaMap.put(profissional.getId(), new HashMap<>());

            Integer horasTotais = repository.calcularHorasSemana(profissional, inicioSemana, fimSemana);
            int horas = horasTotais != null ? horasTotais : 0;

            limiteAtingidoMap.put(profissional.getId(), horas >= profissional.getCargaHorariaSemanal());
        }

        for (EscalaPlantao plantao : plantoes) {
            String dataStr = plantao.getData().toString();
            Long profissionalId = plantao.getProfissional().getId();

            Map<String, List<PlantaoInfoDTO>> mapaProfissional = escalaMap.get(profissionalId);

            if (!mapaProfissional.containsKey(dataStr)) {
                mapaProfissional.put(dataStr, new ArrayList<>());
            }

            String turnoLabel = getTurnoLabel(plantao.getTurno().name());
            mapaProfissional.get(dataStr).add(new PlantaoInfoDTO(plantao.getId(), plantao.getTurno().name(), turnoLabel));
        }

        EscalaSemanalDTO dto = new EscalaSemanalDTO();
        dto.setInicioSemana(inicioSemana);
        dto.setFimSemana(fimSemana);
        dto.setEscala(escalaMap);
        dto.setLimiteAtingido(limiteAtingidoMap);

        return dto;
    }

    private EscalaPlantaoDTO converterParaDTO(EscalaPlantao escalaPlantao) {
        EscalaPlantaoDTO dto = new EscalaPlantaoDTO();
        dto.setId(escalaPlantao.getId());
        dto.setProfissionalId(escalaPlantao.getProfissional().getId());
        dto.setProfissionalNome(escalaPlantao.getProfissional().getNome());
        dto.setData(escalaPlantao.getData());
        dto.setTurno(escalaPlantao.getTurno());
        return dto;
    }

    private String getTurnoLabel(String turno) {
        switch (turno) {
            case "MANHA": return "MANHÃ";
            case "TARDE": return "TARDE";
            case "NOITE": return "NOITE";
            default: return turno;
        }
    }

}
