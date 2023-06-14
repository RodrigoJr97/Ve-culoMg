package com.veiculosmg.service.implementacaoService;

import com.veiculosmg.exception.RecursoNaoEncontradoException;
import com.veiculosmg.model.entity.Endereco;
import com.veiculosmg.service.interfaceService.EnderecoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImplEnderecoServiceTest {

    @InjectMocks
    private ImplEnderecoService implEnderecoService;

    @Mock
    private EnderecoService enderecoServiceMock;

    private List<Endereco> listaEnderecos;

    @BeforeEach
    public void setUp() {
        listaEnderecos = new ArrayList<>();
        listaEnderecos.add(new Endereco("38402200", "Rua Teste API 1", "", "Brasil", "Uberlandia", "MG"));
        listaEnderecos.add(new Endereco("38402201", "Rua Teste API 2", "", "Martins", "Uberlandia", "MG"));
    }

    @Test
    void testSalvaNovoEnderecoComSucesso() {
        Endereco novoEndereco = listaEnderecos.get(0);

        when(enderecoServiceMock.salvaNovaEntidade(novoEndereco)).thenReturn(novoEndereco);
        Endereco enderecoSalvo = enderecoServiceMock.salvaNovaEntidade(novoEndereco);

        assertEquals(enderecoSalvo, novoEndereco);
        verify(enderecoServiceMock).salvaNovaEntidade(novoEndereco);
        verifyNoMoreInteractions(enderecoServiceMock);
    }

    @Test
    void testBucaTodosOsEnderecosComSucesso() {
        when(enderecoServiceMock.listaEntidades()).thenReturn(listaEnderecos);

        List<Endereco> listaEnderecosRetornadaPeloMetodo = enderecoServiceMock.listaEntidades();

        assertEquals(listaEnderecos, listaEnderecosRetornadaPeloMetodo);
        verify(enderecoServiceMock).listaEntidades();
        verifyNoMoreInteractions(enderecoServiceMock);
    }

    @Test
    void testBucaUmEnderecoPeloIdComSucesso() {
        long id = 1;
        Optional<Endereco> enderecoEsperado = Optional.ofNullable(listaEnderecos.get(0));
        enderecoEsperado.get().setId(id);

        when(enderecoServiceMock.entidadePorId(id)).thenReturn(enderecoEsperado);
        Optional<Endereco> enderecoObtidoPeloMetodo = enderecoServiceMock.entidadePorId(id);

        assertEquals(enderecoEsperado, enderecoObtidoPeloMetodo);
        verify(enderecoServiceMock).entidadePorId(id);
        verifyNoMoreInteractions(enderecoServiceMock);
    }

    @Test
    void testDeleteEnderecoPeloIdComSucesso() {
        long id = 1;
        doNothing().when(enderecoServiceMock).deletaEntidade(id);
        enderecoServiceMock.deletaEntidade(id);

        verify(enderecoServiceMock).deletaEntidade(id);
        verifyNoMoreInteractions(enderecoServiceMock);
    }

    @Test
    void testAtualizaEnderecoComSucesso() {
        long id = 1;
        Endereco enderecoQueJaEstaSalvo = listaEnderecos.get(0);
        enderecoQueJaEstaSalvo.setId(id);

        Endereco enderecoAtualizado = listaEnderecos.get(1);
        enderecoAtualizado.setId(id);

        doNothing().when(enderecoServiceMock).updateEntidade(enderecoAtualizado, id);
        enderecoServiceMock.updateEntidade(enderecoAtualizado, id);

        assertEquals(enderecoAtualizado.getId(), enderecoQueJaEstaSalvo.getId());
        verify(enderecoServiceMock).updateEntidade(enderecoAtualizado, id);
        verifyNoMoreInteractions(enderecoServiceMock);
    }

    @Test
    void testBuscaEnderecoPeloIdQueNaoEstaCadastradoDeveLancarExcecao() {
        long id = 1;

        doThrow(new RecursoNaoEncontradoException("Endereco com Id: " + id + " não Encontrado!"))
                .when(enderecoServiceMock)
                .entidadePorId(id);

        assertThrows(RecursoNaoEncontradoException.class, () -> enderecoServiceMock.entidadePorId(id));

        verify(enderecoServiceMock).entidadePorId(id);
        verifyNoMoreInteractions(enderecoServiceMock);
    }

    @Test
    void testDeletaEnderecoPeloIdQueNaoEstaCadastradoDeveLancarExcecao() {
        long id = 1;

        doThrow(new RecursoNaoEncontradoException("Endereco com Id: " + id + " Não Encontrado!"))
                .when(enderecoServiceMock)
                .deletaEntidade(id);

        assertThrows(RecursoNaoEncontradoException.class, () -> enderecoServiceMock.deletaEntidade(id));

        verify(enderecoServiceMock).deletaEntidade(id);
        verifyNoMoreInteractions(enderecoServiceMock);
    }

}
