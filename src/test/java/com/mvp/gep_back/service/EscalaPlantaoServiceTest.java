package com.mvp.gep_back.service;

import com.mvp.gep_back.model.dto.EscalaPlantaoDTO;
import com.mvp.gep_back.model.dto.EscalaSemanalDTO;
import com.mvp.gep_back.exception.NegocioException;
import com.mvp.gep_back.model.entity.EscalaPlantao;
import com.mvp.gep_back.model.entity.Profissional;
import com.mvp.gep_back.model.enums.CategoriaEnum;
import com.mvp.gep_back.model.enums.TurnoEnum;
import com.mvp.gep_back.repository.EscalaPlantaoRepository;
import com.mvp.gep_back.repository.ProfissionalRepository;
import com.mvp.gep_back.service.impl.EscalaPlantaoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EscalaPlantaoServiceTest {

    @Mock
    private EscalaPlantaoRepository repository;

    @Mock
    private ProfissionalRepository profissionalRepository;

    @InjectMocks
    private EscalaPlantaoServiceImpl service;

    private Profissional profissional;
    private EscalaPlantaoDTO plantaoDTO;
    private EscalaPlantao plantao;

    @BeforeEach
    void setUp() {
        profissional = new Profissional();
        profissional.setId(1L);
        profissional.setNome("Dr. João Silva");
        profissional.setRegistro("CRM12345");
        profissional.setCategoria(CategoriaEnum.MEDICO);
        profissional.setCargaHorariaSemanal(40);

        plantaoDTO = new EscalaPlantaoDTO();
        plantaoDTO.setProfissionalId(1L);
        plantaoDTO.setData(LocalDate.of(2024, 1, 15));
        plantaoDTO.setTurno(TurnoEnum.MANHA);

        plantao = new EscalaPlantao();
        plantao.setId(1L);
        plantao.setProfissional(profissional);
        plantao.setData(LocalDate.of(2024, 1, 15));
        plantao.setTurno(TurnoEnum.MANHA);
    }

    //CADASTRAR PLANTAO
    @Test
    @DisplayName("cadastrarPlantao - Deve cadastrar plantão com sucesso")
    void cadastrarPlantao_DeveCadastrarPlantaoComSucesso() {
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(repository.findByProfissionalAndDataAndTurno(any(), any(), any()))
                .thenReturn(Optional.empty());
        when(repository.calcularHorasSemana(any(), any(), any())).thenReturn(30);
        when(repository.save(any(EscalaPlantao.class))).thenReturn(plantao);

        EscalaPlantaoDTO resultado = service.cadastrarPlantao(plantaoDTO);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(repository, times(1)).save(any(EscalaPlantao.class));
    }

    @Test
    @DisplayName("cadastrarPlantao - Deve lançar erro quando profissional não existe")
    void cadastrarPlantao_DeveLancarErroQuandoProfissionalNaoExiste() {
        when(profissionalRepository.findById(1L)).thenReturn(Optional.empty());

        NegocioException exception = assertThrows(NegocioException.class, () -> {
            service.cadastrarPlantao(plantaoDTO);
        });

        assertEquals("Profissional não encontrado!", exception.getMessage());
        verify(repository, never()).save(any(EscalaPlantao.class));
    }

    @Test
    @DisplayName("cadastrarPlantao - Deve lançar erro quando já existe plantão no mesmo dia/turno")
    void cadastrarPlantao_DeveLancarErroQuandoPlantaoDuplicado() {
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(repository.findByProfissionalAndDataAndTurno(any(), any(), any()))
                .thenReturn(Optional.of(plantao));

        NegocioException exception = assertThrows(NegocioException.class, () -> {
            service.cadastrarPlantao(plantaoDTO);
        });

        assertEquals("Profissional já possui plantão neste dia e turno", exception.getMessage());
        verify(repository, never()).save(any(EscalaPlantao.class));
    }

    @Test
    @DisplayName("cadastrarPlantao - Deve lançar erro quando carga horária semanal excede limite")
    void cadastrarPlantao_DeveLancarErroQuandoCargaHorariaExcede() {
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(repository.findByProfissionalAndDataAndTurno(any(), any(), any()))
                .thenReturn(Optional.empty());
        when(repository.calcularHorasSemana(any(), any(), any())).thenReturn(38);

        NegocioException exception = assertThrows(NegocioException.class, () -> {
            service.cadastrarPlantao(plantaoDTO);
        });

        assertTrue(exception.getMessage().contains("Carga horária semanal excedida"));
        verify(repository, never()).save(any(EscalaPlantao.class));
    }

    //EXCLUIR PLANTAO
    @Test
    @DisplayName("excluirPlantao - Deve excluir plantão com sucesso")
    void excluirPlantao_DeveExcluirPlantaoComSucesso() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        assertDoesNotThrow(() -> service.excluirPlantao(1L));
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("excluirPlantao - Deve lançar erro quando plantão não existe")
    void excluirPlantao_DeveLancarErroQuandoPlantaoNaoExiste() {
        when(repository.existsById(999L)).thenReturn(false);

        NegocioException exception = assertThrows(NegocioException.class, () -> {
            service.excluirPlantao(999L);
        });

        assertEquals("Plantão não encontrado!", exception.getMessage());
        verify(repository, never()).deleteById(anyLong());
    }

    //OBTER ESCALA SEMANAL
    @Test
    @DisplayName("obterEscalaSemanal - Deve retornar escala semanal com sucesso")
    void obterEscalaSemanal_DeveRetornarEscalaSemanalComSucesso() {
        LocalDate data = LocalDate.of(2024, 1, 15);

        when(profissionalRepository.findAll()).thenReturn(Arrays.asList(profissional));
        when(repository.findBySemana(any(), any())).thenReturn(Arrays.asList(plantao));
        when(repository.calcularHorasSemana(any(), any(), any())).thenReturn(6);

        EscalaSemanalDTO resultado = service.obterEscalaSemanal(data);

        assertNotNull(resultado);
        assertNotNull(resultado.getInicioSemana());
        assertNotNull(resultado.getFimSemana());
        assertNotNull(resultado.getEscala());
        assertNotNull(resultado.getLimiteAtingido());
    }

    @Test
    @DisplayName("obterEscalaSemanal - Deve retornar escala vazia quando não há profissionais")
    void obterEscalaSemanal_DeveRetornarEscalaVaziaQuandoNaoHaProfissionais() {
        LocalDate data = LocalDate.of(2024, 1, 15);

        when(profissionalRepository.findAll()).thenReturn(Arrays.asList());
        when(repository.findBySemana(any(), any())).thenReturn(Arrays.asList());

        EscalaSemanalDTO resultado = service.obterEscalaSemanal(data);

        assertNotNull(resultado);
        assertTrue(resultado.getEscala().isEmpty());
    }

    @Test
    @DisplayName("obterEscalaSemanal - Deve ajustar para segunda-feira corretamente")
    void obterEscalaSemanal_DeveAjustarParaSegundaFeiraCorretamente() {
        LocalDate quartaFeira = LocalDate.of(2024, 1, 17);

        when(profissionalRepository.findAll()).thenReturn(Arrays.asList());
        when(repository.findBySemana(any(), any())).thenReturn(Arrays.asList());

        EscalaSemanalDTO resultado = service.obterEscalaSemanal(quartaFeira);

        // Deve ajustar para segunda-feira (15/01/2024)
        assertEquals(LocalDate.of(2024, 1, 15), resultado.getInicioSemana());
        assertEquals(LocalDate.of(2024, 1, 21), resultado.getFimSemana());
    }

}
