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
    private ImplCarroService carroService;

    @Mock
    private CarroService carroServiceMock;

    private List<Carro> listaDeCarros;

    @BeforeEach
    public void setUp() {
        listaDeCarros = new ArrayList<>();
        listaDeCarros.add(new Carro("Bmw", "X5", "MGU-0002", 2022, "SUV", "Gasolina", 350.45));
        listaDeCarros.add(new Carro("Gm", "Vectra", "RGD0J07", 2003, "Sedan", "Gasolina", 250.5));
        listaDeCarros.add(new Carro("Vw", "Amarok V6", "MGU-0003", 2022, "Caminhonete", "Diesel", 350.45));
        listaDeCarros.add(new Carro("Gm", "Opala", "RDG9T92", 1992, "Sedan", "Álcool", 320.85));
        listaDeCarros.add(new Carro("Vw", "Golf GTI", "MGU-0004", 2022, "Hatch", "Gasolina", 300.0));
    }

    @Test
    void buscarTodosOsCarrosComSucesso() {
        when(carroServiceMock.listaEntidades()).thenReturn(listaDeCarros);

        List<Carro> listaDeCarrosRetornadoPeloMetodo = carroServiceMock.listaEntidades();

        assertEquals(listaDeCarros, listaDeCarrosRetornadoPeloMetodo);
        verify(carroServiceMock).listaEntidades();
        verifyNoMoreInteractions(carroServiceMock);
    }

    @Test
    void buscaUmCarroPeloIdComSucesso() {
        long id = 5;
        Optional<Carro> carroEsperado = Optional.ofNullable(listaDeCarros.get(1));
        carroEsperado.get().setId(id);

        when(carroServiceMock.entidadePorId(id)).thenReturn(carroEsperado);

        Optional<Carro> carroObtidoPeloMetodo = carroServiceMock.entidadePorId(id);

        assertEquals(carroEsperado, carroObtidoPeloMetodo);
        verify(carroServiceMock).entidadePorId(id);
        verifyNoMoreInteractions(carroServiceMock);
    }

    @Test
    void buscaCarrosDisponiveisComSucesso() {
        List<Carro> listaDeCarrosDisponiveis = listaDeCarros.stream()
                .filter(Carro::isDisponivel)
                .toList();

        when(carroServiceMock.disponivel()).thenReturn(listaDeCarrosDisponiveis);

        List<Carro> carrosDisponiveisRetornadosPeloMetodo = carroServiceMock.disponivel();

        assertEquals(listaDeCarrosDisponiveis, carrosDisponiveisRetornadosPeloMetodo);
        verify(carroServiceMock).disponivel();
        verifyNoMoreInteractions(carroServiceMock);
    }

    @Test
    void bucaCarrosPelaCategoriaComSucesso() {
        String categoria = "Sedan";
        List<Carro> carrosPelaCategoria = listaDeCarros.stream()
                .filter(carro -> carro.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());

        when(carroServiceMock.listCategoria(categoria)).thenReturn(carrosPelaCategoria);

        List<Carro> carrosObtidosPelaCategoria = carroServiceMock.listCategoria(categoria);

        assertEquals(carrosPelaCategoria, carrosObtidosPelaCategoria);
        verify(carroServiceMock).listCategoria(categoria);
        verifyNoMoreInteractions(carroServiceMock);
    }

    @Test
    void salvaNovoCarroComSucesso() {
        Carro novoCarro = new Carro("Ford", "F250", "RGD0J07", 2005, "Caminhonete", "Diesel", 250.5);

        when(carroServiceMock.salvaNovaEntidade(novoCarro)).thenReturn(novoCarro);
        Carro carroSalvo = carroServiceMock.salvaNovaEntidade(novoCarro);

        assertEquals(novoCarro, carroSalvo);
        verify(carroServiceMock).salvaNovaEntidade(novoCarro);
        verifyNoMoreInteractions(carroServiceMock);
    }

    @Test
    void deleteCarroPeloId() {
        doNothing().when(carroServiceMock).deletaEntidade(5L);

        carroServiceMock.deletaEntidade(5L);

        verify(carroServiceMock).deletaEntidade(5L);
        verifyNoMoreInteractions(carroServiceMock);
    }

    @Test
    void atualizaCarroQueFoiSelecionadoPeloIdComSucesso() {
        long id = 5;
        Carro carroQueJaEstaSalvo = new Carro("Ford", "F250", "MGU0097", 2005, "Caminhonete", "Diesel", 250.5);
        carroQueJaEstaSalvo.setId(id);

        Carro carroAtualizado = new Carro("Vw", "Golf GTI", "MGU0097", 2022, "Hatch", "Gasolina", 300.0);
        carroAtualizado.setId(id);

        doNothing().when(carroServiceMock).updateEntidade(carroAtualizado, id);

        carroServiceMock.updateEntidade(carroAtualizado, id);

        assertEquals(carroQueJaEstaSalvo.getId(), carroAtualizado.getId());
        assertEquals(carroQueJaEstaSalvo.getPlaca(), carroAtualizado.getPlaca());

        verify(carroServiceMock).updateEntidade(carroAtualizado, id);
        verifyNoMoreInteractions(carroServiceMock);
    }

    @Test
    void salvaCarroComPlacaDuplicadaDeveLancarExcecao() {
        Carro novoCarro = new Carro("Ford", "F250", "RGD0J07", 2005, "Caminhonete", "Diesel", 250.5);

        doThrow(new AtributoDuplicadoException("Placa: " + novoCarro.getPlaca() + " já está cadastrada!"))
                .when(carroServiceMock)
                .salvaNovaEntidade(novoCarro);

        assertThrows(AtributoDuplicadoException.class, () -> carroServiceMock.salvaNovaEntidade(novoCarro));

        verify(carroServiceMock).salvaNovaEntidade(novoCarro);
        verifyNoMoreInteractions(carroServiceMock);
    }

    @Test
    void deletaCarroPeloIdQueNaoEstaCadastradoDeveLancarExcecao() {
        long id = 10;

        doThrow(new RecursoNaoEncontradoException("Carro com Id: " + id + " Não Encontrado!"))
                .when(carroServiceMock)
                .deletaEntidade(id);

        assertThrows(RecursoNaoEncontradoException.class, () -> carroServiceMock.deletaEntidade(id));

        verify(carroServiceMock).deletaEntidade(id);
        verifyNoMoreInteractions(carroServiceMock);
    }

    @Test
    void atualizaCarroComPlacaDuplicadaDeveLancarExcecao() {
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
