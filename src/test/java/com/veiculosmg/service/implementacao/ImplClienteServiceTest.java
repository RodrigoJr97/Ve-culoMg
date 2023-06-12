package com.veiculosmg.service.implementacao;

import com.veiculosmg.exception.AtributoDuplicadoException;
import com.veiculosmg.exception.MenorDeIdadeException;
import com.veiculosmg.exception.RecursoNaoEncontradoException;
import com.veiculosmg.model.entity.Cliente;
import com.veiculosmg.model.entity.Endereco;
import com.veiculosmg.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImplClienteServiceTest {

    @InjectMocks
    private ImplClienteService implClienteService;

    @Mock
    private ClienteService clienteServiceMock;

    private List<Cliente> listaClientes;

    @BeforeEach
    public void setUp() {
        listaClientes = new ArrayList<>();
        Endereco endereco1 = new Endereco("38402200", "Rua Teste API 1", "", "Brasil", "Uberlandia", "MG");
        Endereco endereco2 = new Endereco("38402201", "Rua Teste API 2", "", "Martins", "Uberlandia", "MG");
        listaClientes.add(new Cliente("Karina Silva", "41368104070", "10911112220", "teste01@gmail.com", LocalDate.parse("2002-06-06"), endereco1));
        listaClientes.add(new Cliente("Hugo Borges", "33255435021", "20911112221", "teste02@gmail.com", LocalDate.parse("2002-06-06"), endereco2));
    }

    @Test
    void testSalvaNovoClienteComSucesso() {
        Endereco endereco2 = new Endereco("38402201", "Rua Teste API 2", "", "Martins", "Uberlandia", "MG");
        Cliente novoCliente = listaClientes.get(0);
        novoCliente.setEndereco(endereco2);

        when(clienteServiceMock.salvaNovaEntidade(novoCliente)).thenReturn(novoCliente);
        Cliente clienteSalvo = clienteServiceMock.salvaNovaEntidade(novoCliente);

        assertEquals(novoCliente, clienteSalvo);
        verify(clienteServiceMock).salvaNovaEntidade(novoCliente);
        verifyNoMoreInteractions(clienteServiceMock);
    }

    @Test
    void testBuscarTodosOsClientesComSucesso() {
        when(clienteServiceMock.listaEntidades()).thenReturn(listaClientes);

        List<Cliente> listaDeClientesRetornadoPeloMetodo = clienteServiceMock.listaEntidades();

        assertEquals(listaClientes, listaDeClientesRetornadoPeloMetodo);
        verify(clienteServiceMock).listaEntidades();
        verifyNoMoreInteractions(clienteServiceMock);
    }

    @Test
    void testBuscaUmClientePeloIdComSucesso() {
        long id = 1;
        Optional<Cliente> clienteEsperado = Optional.ofNullable(listaClientes.get(0));
        clienteEsperado.get().setId(id);

        when(clienteServiceMock.entidadePorId(id)).thenReturn(clienteEsperado);
        Optional<Cliente> clienteObtidoPeloMetodo = clienteServiceMock.entidadePorId(id);

        assertEquals(clienteEsperado, clienteObtidoPeloMetodo);
        verify(clienteServiceMock).entidadePorId(id);
        verifyNoMoreInteractions(clienteServiceMock);
    }

    @Test
    void testDeleteClientePeloIdComSucesso() {
        long id = 1;
        doNothing().when(clienteServiceMock).deletaEntidade(id);
        clienteServiceMock.deletaEntidade(id);

        verify(clienteServiceMock).deletaEntidade(id);
        verifyNoMoreInteractions(clienteServiceMock);
    }

    @Test
    void testAtualizaClienteComSucesso() {
        long id = 1;
        Cliente clienteQueJaEstaSalvo = listaClientes.get(0);
        clienteQueJaEstaSalvo.setId(id);

        Cliente clienteAtualizado = listaClientes.get(1);
        clienteAtualizado.setId(id);

        doNothing().when(clienteServiceMock).updateEntidade(clienteAtualizado, id);
        clienteServiceMock.updateEntidade(clienteAtualizado, id);

        assertEquals(clienteAtualizado.getId(), clienteQueJaEstaSalvo.getId());
        verify(clienteServiceMock).updateEntidade(clienteAtualizado, id);
        verifyNoMoreInteractions(clienteServiceMock);
    }

    @Test
    void testSalvaNovoClienteComNomeCpfEmailNumeroTelefoneDuplicadosDeveLancarExcecao() {
        Cliente novoCliente = listaClientes.get(0);

        List<String> atributosDuplicados = Arrays.asList("nome", "cpf", "email", "numeroTelefone");

        doThrow(new AtributoDuplicadoException("Os seguintes atributos já estão vinculados a outro cliente: " + atributosDuplicados))
                .when(clienteServiceMock)
                .salvaNovaEntidade(novoCliente);

        assertThrows(AtributoDuplicadoException.class, () -> clienteServiceMock.salvaNovaEntidade(novoCliente));

        verify(clienteServiceMock).salvaNovaEntidade(novoCliente);
        verifyNoMoreInteractions(clienteServiceMock);
    }

    @Test
    void testBuscaClientePeloIdQueNaoEstaCadastradoDeveLancarExcecao() {
        long id = 1;

        doThrow(new RecursoNaoEncontradoException("Cliente com Id: " + id + " não Encontrado!"))
                .when(clienteServiceMock)
                .entidadePorId(id);

        assertThrows(RecursoNaoEncontradoException.class, () -> clienteServiceMock.entidadePorId(id));

        verify(clienteServiceMock).entidadePorId(id);
        verifyNoMoreInteractions(clienteServiceMock);
    }

    @Test
    void testDeletaClientePeloIdQueNaoEstaCadastradoDeveLancarExcecao() {
        long id = 1;

        doThrow(new RecursoNaoEncontradoException("Cliente com Id: " + id + " Não Encontrado!"))
                .when(clienteServiceMock)
                .deletaEntidade(id);

        assertThrows(RecursoNaoEncontradoException.class, () -> clienteServiceMock.deletaEntidade(id));

        verify(clienteServiceMock).deletaEntidade(id);
        verifyNoMoreInteractions(clienteServiceMock);
    }

    @Test
    void testAtualizaClienteComNomeCpfEmailNumeroTelefoneDuplicadosDeveLancarExcecao() {
        long id = 1;
        Cliente clienteAtualizado = listaClientes.get(1);
        clienteAtualizado.setId(id);

        List<String> atributosDuplicados = Arrays.asList("nome", "cpf", "email", "numeroTelefone");

        doThrow(new AtributoDuplicadoException("Atributos já estão vinculados a outro cliente: " + atributosDuplicados))
                .when(clienteServiceMock)
                .updateEntidade(clienteAtualizado, id);

        assertThrows(AtributoDuplicadoException.class, () -> clienteServiceMock.updateEntidade(clienteAtualizado, id));
        verify(clienteServiceMock).updateEntidade(clienteAtualizado, id);
        verifyNoMoreInteractions(clienteServiceMock);
    }

    @Test
    void testSalvaNovoClienteMenorDeIdadeDeveLancarExcecao() {
        Cliente novoCliente = listaClientes.get(0);

        doThrow(new MenorDeIdadeException("Cliente é menor de idade."))
                .when(clienteServiceMock)
                .salvaNovaEntidade(novoCliente);

        assertThrows(MenorDeIdadeException.class, () -> clienteServiceMock.salvaNovaEntidade(novoCliente));

        verify(clienteServiceMock).salvaNovaEntidade(novoCliente);
        verifyNoMoreInteractions(clienteServiceMock);
    }


}
