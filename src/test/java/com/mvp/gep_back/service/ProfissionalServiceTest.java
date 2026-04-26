package com.mvp.gep_back.service;

import com.mvp.gep_back.exception.NegocioException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    //CADASTRAR
    @Test
    @DisplayName("cadastrar - Deve cadastrar profissional com sucesso")
    void cadastrar_DeveCadastrarProfissionalComSucesso() {
        when(repository.existsByRegistro(anyString())).thenReturn(false);
        when(repository.save(any(Profissional.class))).thenReturn(profissional);

        ProfissionalDTO resultado = service.cadastrar(profissionalDTO);

        assertNotNull(resultado);
        assertEquals("Dr. João Silva", resultado.getNome());
        assertEquals("CRM12345", resultado.getRegistro());
        verify(repository, times(1)).save(any(Profissional.class));
    }

    @Test
    @DisplayName("cadastrar - Deve lançar erro quando registro já existe")
    void cadastrar_DeveLancarErroQuandoRegistroDuplicado() {
        when(repository.existsByRegistro(anyString())).thenReturn(true);

        NegocioException exception = assertThrows(NegocioException.class, () -> {
            service.cadastrar(profissionalDTO);
        });

        assertEquals("Já existe um profissional com este CRM/COREN", exception.getMessage());
        verify(repository, never()).save(any(Profissional.class));
    }

    //EDITAR
    @Test
    @DisplayName("editar - Deve editar profissional com sucesso")
    void editar_DeveEditarProfissionalComSucesso() {
        ProfissionalDTO dtoEditado = new ProfissionalDTO();
        dtoEditado.setNome("Dr. João Silva Atualizado");
        dtoEditado.setRegistro("CRM12345");
        dtoEditado.setCategoria(CategoriaEnum.MEDICO);
        dtoEditado.setCargaHorariaSemanal(40);

        Profissional profissionalEditado = new Profissional();
        profissionalEditado.setId(1L);
        profissionalEditado.setNome("Dr. João Silva Atualizado");
        profissionalEditado.setRegistro("CRM12345");
        profissionalEditado.setCategoria(CategoriaEnum.MEDICO);
        profissionalEditado.setCargaHorariaSemanal(40);

        when(repository.findById(1L)).thenReturn(Optional.of(profissional));
        when(repository.save(any(Profissional.class))).thenReturn(profissionalEditado);

        ProfissionalDTO resultado = service.editar(1L, dtoEditado);

        assertNotNull(resultado);
        assertEquals("Dr. João Silva Atualizado", resultado.getNome());
        verify(repository, times(1)).save(any(Profissional.class));
    }

    @Test
    @DisplayName("editar - Deve lançar erro quando profissional não existe")
    void editar_DeveLancarErroQuandoProfissionalNaoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        NegocioException exception = assertThrows(NegocioException.class, () -> {
            service.editar(1L, profissionalDTO);
        });

        assertEquals("Profissional não encontrado", exception.getMessage());
        verify(repository, never()).save(any(Profissional.class));
    }

    //EXCLUIR
    @Test
    @DisplayName("excluirPorId - Deve excluir profissional com sucesso")
    void excluirPorId_DeveExcluirProfissionalComSucesso() {
        doNothing().when(repository).deleteById(1L);

        assertDoesNotThrow(() -> service.excluirPorId(1L));
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("excluirPorId - Deve lançar erro quando id é nulo")
    void excluirPorId_DeveLancarErroQuandoIdNulo() {
        assertThrows(Exception.class, () -> service.excluirPorId(null));
    }

    //LISTAR
    @Test
    @DisplayName("listar - Deve listar todos os profissionais sem filtro")
    void listar_DeveListarTodosOsProfissionaisSemFiltro() {
        Profissional profissional2 = new Profissional();
        profissional2.setId(2L);
        profissional2.setNome("Dra. Maria Santos");
        profissional2.setRegistro("CRM67890");
        profissional2.setCategoria(CategoriaEnum.MEDICO);
        profissional2.setCargaHorariaSemanal(40);

        when(repository.findAll()).thenReturn(Arrays.asList(profissional, profissional2));

        List<ProfissionalDTO> resultado = service.listar(null);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("listar - Deve listar profissionais filtrados por categoria")
    void listar_DeveListarProfissionaisFiltradosPorCategoria() {
        when(repository.findByCategoria(CategoriaEnum.MEDICO))
                .thenReturn(Arrays.asList(profissional));

        List<ProfissionalDTO> resultado = service.listar("MEDICO");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(repository, times(1)).findByCategoria(CategoriaEnum.MEDICO);
    }

    //BUSCAR POR ID
    @Test
    @DisplayName("buscarPorId - Deve buscar profissional com sucesso")
    void buscarPorId_DeveBuscarProfissionalComSucesso() {
        when(repository.findById(1L)).thenReturn(Optional.of(profissional));

        ProfissionalDTO resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Dr. João Silva", resultado.getNome());
    }

    @Test
    @DisplayName("buscarPorId - Deve lançar erro quando profissional não existe")
    void buscarPorId_DeveLancarErroQuandoProfissionalNaoExiste() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        NegocioException exception = assertThrows(NegocioException.class, () -> {
            service.buscarPorId(999L);
        });

        assertEquals("Profissional não encontrado", exception.getMessage());
    }

}
