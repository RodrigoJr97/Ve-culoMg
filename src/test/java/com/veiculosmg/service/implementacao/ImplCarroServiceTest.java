package com.veiculosmg.service.implementacao;

import com.veiculosmg.exception.AtributoDuplicadoException;
import com.veiculosmg.exception.RecursoNaoEncontradoException;
import com.veiculosmg.model.entity.Carro;
import com.veiculosmg.service.CarroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImplCarroServiceTest {

    @InjectMocks
    private ImplCarroService implCarroService;

    @Mock
    private CarroService carroServiceMock;

    private List<Carro> listaCarros;

    @BeforeEach
    public void setUp() {
        listaCarros = new ArrayList<>();
        listaCarros.add(new Carro("Bmw", "X5", "MGU-0002", 2022, "SUV", "Gasolina", 350.45));
        listaCarros.add(new Carro("Gm", "Vectra", "RGD0J07", 2003, "Sedan", "Gasolina", 250.5));
        listaCarros.add(new Carro("Vw", "Amarok V6", "MGU-0003", 2022, "Caminhonete", "Diesel", 350.45));
    }

    @Test
    void testSalvaNovoCarroComSucesso() {
        Carro novoCarro = listaCarros.get(0);

        when(carroServiceMock.salvaNovaEntidade(novoCarro)).thenReturn(novoCarro);
        Carro carroSalvo = carroServiceMock.salvaNovaEntidade(novoCarro);

        assertEquals(novoCarro, carroSalvo);
        verify(carroServiceMock).salvaNovaEntidade(novoCarro);
        verifyNoMoreInteractions(carroServiceMock);
    }

    @Test
    void testBuscarTodosOsCarrosComSucesso() {
        when(carroServiceMock.listaEntidades()).thenReturn(listaCarros);

        List<Carro> listaDeCarrosRetornadoPeloMetodo = carroServiceMock.listaEntidades();

        assertEquals(listaCarros, listaDeCarrosRetornadoPeloMetodo);
        verify(carroServiceMock).listaEntidades();
        verifyNoMoreInteractions(carroServiceMock);
    }

    @Test
    void testBuscaUmCarroPeloIdComSucesso() {
        long id = 5;
        Optional<Carro> carroEsperado = Optional.ofNullable(listaCarros.get(1));
        carroEsperado.get().setId(id);

        when(carroServiceMock.entidadePorId(id)).thenReturn(carroEsperado);
        Optional<Carro> carroObtidoPeloMetodo = carroServiceMock.entidadePorId(id);

        assertEquals(carroEsperado, carroObtidoPeloMetodo);
        verify(carroServiceMock).entidadePorId(id);
        verifyNoMoreInteractions(carroServiceMock);
    }

    @Test
    void testBuscaCarrosDisponiveisComSucesso() {
        List<Carro> listaDeCarrosDisponiveis = listaCarros.stream()
                .filter(Carro::isDisponivel)
                .toList();

        when(carroServiceMock.disponivel()).thenReturn(listaDeCarrosDisponiveis);

        List<Carro> carrosDisponiveisRetornadosPeloMetodo = carroServiceMock.disponivel();

        assertEquals(listaDeCarrosDisponiveis, carrosDisponiveisRetornadosPeloMetodo);
        verify(carroServiceMock).disponivel();
        verifyNoMoreInteractions(carroServiceMock);
    }

    @Test
    void testBucaCarrosPelaCategoriaComSucesso() {
        String categoria = "Sedan";
        List<Carro> carrosPelaCategoria = listaCarros.stream()
                .filter(carro -> carro.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());

        when(carroServiceMock.listCategoria(categoria)).thenReturn(carrosPelaCategoria);

        List<Carro> carrosObtidosPelaCategoria = carroServiceMock.listCategoria(categoria);

        assertEquals(carrosPelaCategoria, carrosObtidosPelaCategoria);
        verify(carroServiceMock).listCategoria(categoria);
        verifyNoMoreInteractions(carroServiceMock);
    }

    @Test
    void testDeleteCarroPeloIdComSucesso() {
        long id = 1;
        doNothing().when(carroServiceMock).deletaEntidade(id);
        carroServiceMock.deletaEntidade(id);

        verify(carroServiceMock).deletaEntidade(id);
        verifyNoMoreInteractions(carroServiceMock);
    }

    @Test
    void testAtualizaCarroQueFoiSelecionadoPeloIdComSucesso() {
        long id = 5;
        Carro carroQueJaEstaSalvo = listaCarros.get(0);
        carroQueJaEstaSalvo.setId(id);

        Carro carroAtualizado = new Carro("Vw", "Golf GTI", "MGU-0002", 2022, "Hatch", "Gasolina", 300.0);
        carroAtualizado.setId(id);

        doNothing().when(carroServiceMock).updateEntidade(carroAtualizado, id);
        carroServiceMock.updateEntidade(carroAtualizado, id);

        assertEquals(carroQueJaEstaSalvo.getId(), carroAtualizado.getId());
        assertEquals(carroQueJaEstaSalvo.getPlaca(), carroAtualizado.getPlaca());

        verify(carroServiceMock).updateEntidade(carroAtualizado, id);
        verifyNoMoreInteractions(carroServiceMock);
    }

    @Test
    void testSalvaCarroComPlacaDuplicadaDeveLancarExcecao() {
        Carro novoCarro = new Carro("Ford", "F250", "RGD0J07", 2005, "Caminhonete", "Diesel", 250.5);

        doThrow(new AtributoDuplicadoException("Placa: " + novoCarro.getPlaca() + " já está cadastrada!"))
                .when(carroServiceMock)
                .salvaNovaEntidade(novoCarro);

        assertThrows(AtributoDuplicadoException.class, () -> carroServiceMock.salvaNovaEntidade(novoCarro));

        verify(carroServiceMock).salvaNovaEntidade(novoCarro);
        verifyNoMoreInteractions(carroServiceMock);
    }

    @Test
    void testBuscaCarroPeloIdQueNaoEstaCadastradoDeveLancarExcecao() {
        long id = 1;

        doThrow(new RecursoNaoEncontradoException("Carro com Id: " + id + " não Encontrado!"))
                .when(carroServiceMock)
                .entidadePorId(id);

        assertThrows(RecursoNaoEncontradoException.class, () -> carroServiceMock.entidadePorId(id));

        verify(carroServiceMock).entidadePorId(id);
        verifyNoMoreInteractions(carroServiceMock);
    }

    @Test
    void testDeletaCarroPeloIdQueNaoEstaCadastradoDeveLancarExcecao() {
        long id = 10;

        doThrow(new RecursoNaoEncontradoException("Carro com Id: " + id + " Não Encontrado!"))
                .when(carroServiceMock)
                .deletaEntidade(id);

        assertThrows(RecursoNaoEncontradoException.class, () -> carroServiceMock.deletaEntidade(id));

        verify(carroServiceMock).deletaEntidade(id);
        verifyNoMoreInteractions(carroServiceMock);
    }

    @Test
    void testAtualizaCarroComPlacaDuplicadaDeveLancarExcecao() {
        long id = 5;
        Carro carroAtualizado = new Carro("Ford", "F250", "MGU0097", 2005, "Caminhonete", "Diesel", 250.5);
        carroAtualizado.setId(id);

        doThrow(new AtributoDuplicadoException("Placa informada já está cadastrada em outro veículo!"))
                .when(carroServiceMock).updateEntidade(carroAtualizado, id);

        assertThrows(AtributoDuplicadoException.class, () -> carroServiceMock.updateEntidade(carroAtualizado, id));

        verify(carroServiceMock).updateEntidade(carroAtualizado, id);
        verifyNoMoreInteractions(carroServiceMock);
    }

}
