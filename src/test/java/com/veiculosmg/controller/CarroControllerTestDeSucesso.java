package com.veiculosmg.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veiculosmg.model.entity.Carro;
import com.veiculosmg.service.implementacao.ImplCarroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CarroControllerTestDeSucesso {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ImplCarroService carroService;

    private List<Carro> listaDeCarros;

    @BeforeEach
    public void setUp() {
        Carro carro1 = new Carro(1L, "FORD", "Mustang", "ABC-1234", 2021, "Esportivo", "Gasolina", 450.75, false);
        Carro carro2 = new Carro(2L, "GM", "Vectra", "RGD0J07", 2003, "Sedan", "Gasolina", 250.5, true);
        Carro carro3 = new Carro(3L, "VW", "Amarok V6", "MGU-0003", 2022, "Caminhonete", "Diesel", 350.45, true);

        listaDeCarros = Arrays.asList(carro1, carro2, carro3);
    }

    @Test
    void testSalvaNovoCarroComSucesso_CodigoStatus_201() throws Exception {
        Carro novoCarro = new Carro("BMW", "M4", "ABC-1234", 2022, "Sedan", "Gasolina", 350.45);
        String requestBody = asJsonString(novoCarro);

        when(carroService.salvaNovaEntidade(novoCarro)).thenReturn(novoCarro);

        this.mockMvc.perform(post("/api/carros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.marca").value("BMW"))
                .andExpect(jsonPath("$.modelo").value("M4"))
                .andExpect(jsonPath("$.placa").value("ABC-1234"))
                .andExpect(jsonPath("$.ano").value(2022))
                .andExpect(jsonPath("$.categoria").value("Sedan"))
                .andExpect(jsonPath("$.tipoCombustivel").value("Gasolina"))
                .andExpect(jsonPath("$.valorDiaria").value(350.45));

        verify(carroService).salvaNovaEntidade(novoCarro);
        verifyNoMoreInteractions(carroService);
    }

    @Test
    void testRetornaListaDeCarrosComSucesso_CodigoStatus_200() throws Exception {
        when(carroService.listaEntidades()).thenReturn(listaDeCarros);

        this.mockMvc.perform(get("/api/carros"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\": 1, \"marca\": \"FORD\", \"modelo\": \"Mustang\", \"placa\": \"ABC-1234\", \"ano\": 2021, \"categoria\": \"Esportivo\", \"tipoCombustivel\": \"Gasolina\", \"valorDiaria\": 450.75}," +
                        "{\"id\": 2, \"marca\": \"GM\", \"modelo\": \"Vectra\", \"placa\": \"RGD0J07\", \"ano\": 2003, \"categoria\": \"Sedan\", \"tipoCombustivel\": \"Gasolina\", \"valorDiaria\": 250.5}, " +
                        "{\"id\": 3 ,\"marca\": \"VW\", \"modelo\": \"Amarok V6\", \"placa\": \"MGU-0003\", \"ano\": 2022, \"categoria\": \"Caminhonete\", \"tipoCombustivel\": \"Diesel\", \"valorDiaria\": 350.45}]"));

        verify(carroService).listaEntidades();
        verifyNoMoreInteractions(carroService);
    }

    @Test
    void testRetornaListaVazia_CodigoStatus_200() throws Exception{
        List<Carro> listaVazia = new ArrayList<>();

        when(carroService.listaEntidades()).thenReturn(listaVazia);

        mockMvc.perform(get("/api/carros"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testRetornaCarroPeloIdComSucesso_CodigoStatus_200() throws Exception {
        Carro carro = listaDeCarros.get(0);
        long id = carro.getId();

        when(carroService.entidadePorId(id)).thenReturn(Optional.of(carro));

        this.mockMvc.perform(get("/api/carros/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marca").value("FORD"))
                .andExpect(jsonPath("$.modelo").value("Mustang"))
                .andExpect(jsonPath("$.placa").value("ABC-1234"))
                .andExpect(jsonPath("$.ano").value(2021))
                .andExpect(jsonPath("$.categoria").value("Esportivo"))
                .andExpect(jsonPath("$.tipoCombustivel").value("Gasolina"))
                .andExpect(jsonPath("$.valorDiaria").value(450.75));

        verify(carroService).entidadePorId(id);
        verifyNoMoreInteractions(carroService);
    }

    @Test
    void testRetornaListaDeCarrosDisponiveis_CodigoStatus_200() throws Exception {
        List<Carro> listaDeCarrosDisponiveis = listaDeCarros.stream()
                .filter(Carro::isDisponivel)
                .toList();

        when(carroService.disponivel()).thenReturn(listaDeCarrosDisponiveis);

        this.mockMvc.perform(get("/api/carros/disponiveis"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"marca\": \"GM\", \"modelo\": \"Vectra\", \"placa\": \"RGD0J07\", \"ano\": 2003, \"categoria\": \"Sedan\", \"tipoCombustivel\": \"Gasolina\", \"valorDiaria\": 250.5}," +
                        "{\"marca\": \"VW\", \"modelo\": \"Amarok V6\", \"placa\": \"MGU-0003\", \"ano\": 2022, \"categoria\": \"Caminhonete\", \"tipoCombustivel\": \"Diesel\", \"valorDiaria\": 350.45}]"));

        verify(carroService).disponivel();
        verifyNoMoreInteractions(carroService);
    }

    @Test
    void testRetornaListaDeCarros_SelecionadosPela_CategoriaInformadaComSucesso_CodigoStatus_200() throws Exception{
        String categoria = "Sedan";
        List<Carro> listaDeCarrosPelaCategoria = listaDeCarros.stream()
                .filter(carro -> carro.getCategoria().equalsIgnoreCase(categoria))
                .toList();

        when(carroService.listCategoria(categoria)).thenReturn(listaDeCarrosPelaCategoria);

        mockMvc.perform(get("/api/carros/categoria/{categoria}", categoria))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].categoria", hasItem(categoria)));

        verify(carroService).listCategoria(categoria);
    }

    @Test
    void testAtualizaCarroPassandoONovoCarroEOIdComSucesso_CodigoStatus_200() throws Exception {
        long id = 1;
        Carro novoCarro = new Carro("BMW", "M4", "ABC-1234", 2022, "Sedan", "Gasolina", 350.5);
        novoCarro.setId(id);

        String requestBody = asJsonString(novoCarro);

        when(carroService.entidadePorId(id)).thenReturn(Optional.of(novoCarro));
        doNothing().when(carroService).updateEntidade(novoCarro, id);

        mockMvc.perform(put("/api/carros/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marca").value("BMW"))
                .andExpect(jsonPath("$.modelo").value("M4"))
                .andExpect(jsonPath("$.placa").value("ABC-1234"))
                .andExpect(jsonPath("$.ano").value(2022))
                .andExpect(jsonPath("$.categoria").value("Sedan"))
                .andExpect(jsonPath("$.tipoCombustivel").value("Gasolina"))
                .andExpect(jsonPath("$.valorDiaria").value(350.5));

        verify(carroService).updateEntidade(novoCarro, id);
    }

    @Test
    void testDeletaOCarroPeloIdInformadoComSucesso_CodigoStatus_204() throws Exception {
        long id = 1L;
        Carro carro = listaDeCarros.get(0);

        when(carroService.entidadePorId(id)).thenReturn(Optional.ofNullable(carro));
        doNothing().when(carroService).deletaEntidade(id);

        mockMvc.perform(delete("/api/carros/{id}", id))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(carroService).deletaEntidade(id);
    }

    // Converte um objeto Java para uma representação JSON
    private String asJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
