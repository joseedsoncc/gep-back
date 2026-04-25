package com.mvp.gep_back.service;

import com.mvp.gep_back.model.dto.ProfissionalDTO;
import com.mvp.gep_back.model.entity.Profissional;
import com.mvp.gep_back.model.enums.CategoriaEnum;
import com.mvp.gep_back.repository.ProfissionalRepository;
import com.mvp.gep_back.service.impl.ProfissionalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
 class ProfissionalServiceTest {

    @Mock
    private ProfissionalRepository repository;

    @InjectMocks
    private ProfissionalServiceImpl service;

    private Profissional profissional;
    private ProfissionalDTO profissionalDTO;

    @BeforeEach
    void setUp() {
        // Configurar objetos de teste
        profissional = new Profissional();
        profissional.setId(1L);
        profissional.setNome("Dr. João Silva");
        profissional.setRegistro("CRM12345");
        profissional.setCategoria(CategoriaEnum.MEDICO);
        profissional.setCargaHorariaSemanal(40);

        profissionalDTO = new ProfissionalDTO();
        profissionalDTO.setNome("Dr. João Silva");
        profissionalDTO.setRegistro("CRM12345");
        profissionalDTO.setCategoria(CategoriaEnum.MEDICO);
        profissionalDTO.setCargaHorariaSemanal(40);
    }

    @Test
    @DisplayName("Deve listar todos os profissionais sem filtro")
    void deveListarTodosOsProfissionais() {
        Profissional profissional2 = new Profissional();
        profissional2.setId(2L);
        profissional2.setNome("Dra. Maria Santos");
        profissional2.setRegistro("CRM67890");
        profissional2.setCategoria(CategoriaEnum.MEDICO);
        profissional2.setCargaHorariaSemanal(40);

        List<Profissional> profissionais = Arrays.asList(profissional, profissional2);
        when(repository.findAll()).thenReturn(profissionais);

        List<ProfissionalDTO> resultado = service.listar(null);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("João Silva", resultado.get(0).getNome());
        assertEquals("Maria Santos", resultado.get(1).getNome());

        verify(repository, times(1)).findAll();
        verify(repository, never()).findByCategoria(any());
    }

}
