package com.veiculosmg.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.veiculosmg.exception.AtributoDuplicadoException;
import com.veiculosmg.exception.RecursoNaoEncontradoException;
import com.veiculosmg.model.entity.Carro;
import com.veiculosmg.service.implementacao.ImplCarroService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CarroControllerTestDeErro {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ImplCarroService carroService;


    @Test
    void salvaCarroComPlacaDuplicada_CodigoStatus_409() throws Exception {
        Carro carro = new Carro("FORD", "Mustang", "ABC-1234", 2021, "Esportivo", "Gasolina", 450.75);
        String requestBody = asJsonString(carro);

        String mensagemErro = "Placa: " + carro.getPlaca() + " já está cadastrada!";

        when(carroService.salvaNovaEntidade(carro))
                .thenThrow(new AtributoDuplicadoException(mensagemErro));

        mockMvc.perform(post("/api/carros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigoErro").value(409))
                .andExpect(jsonPath("$.tituloErro").value("Conflict"))
                .andExpect(jsonPath("$.mensagem").value(mensagemErro));
    }

    @Test
    void buscaCarroComIdInexistente_CodigoStatus_404() throws Exception {
        long id = 10;
        String mensagemErro = "Carro com Id: " + id + " Não Encontrado!";
        when(carroService.entidadePorId(id))
                .thenThrow(new RecursoNaoEncontradoException(mensagemErro));

        mockMvc.perform(get("/api/carros/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigoErro").value(404))
                .andExpect(jsonPath("$.tituloErro").value("Not Found"))
                .andExpect(jsonPath("$.mensagem").value(mensagemErro));
    }

    @Test
    void atualizaCarroComPlacaDuplicada_CodigoStatus_409() throws Exception {
        long id = 1;
        Carro carroExistente = new Carro("FORD", "Mustang", "ABC-1234", 2021, "Esportivo", "Gasolina", 450.75);
        Carro novoCarro = new Carro("BMW", "M4", "ABC-1234", 2022, "Sedan", "Gasolina", 350.5);

        String requestBody = asJsonString(novoCarro);
        String mensagemErro = "Placa: " + novoCarro.getPlaca() + " já está cadastrada!";

        when(carroService.entidadePorId(id)).thenReturn(Optional.of(carroExistente));
        doThrow(new AtributoDuplicadoException(mensagemErro))
                .when(carroService).updateEntidade(novoCarro, id);

        mockMvc.perform(put("/api/carros/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigoErro").value(409))
                .andExpect(jsonPath("$.tituloErro").value("Conflict"))
                .andExpect(jsonPath("$.mensagem").value(mensagemErro));
    }

    @Test
    void atualizaCarroComIdInexistente_CodigoStatus_404() throws Exception {
        long id = 1;
        Carro novoCarro = new Carro("FORD", "Mustang", "ABC-1234", 2021, "Esportivo", "Gasolina", 450.75);

        String requestBody = asJsonString(novoCarro);
        String mensagemErro = "Carro com Id: " + id + " Não Encontrado!";

        when(carroService.entidadePorId(id)).thenReturn(Optional.of(novoCarro));
        doThrow(new RecursoNaoEncontradoException(mensagemErro))
                .when(carroService).updateEntidade(novoCarro, id);

        mockMvc.perform(put("/api/carros/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigoErro").value(404))
                .andExpect(jsonPath("$.tituloErro").value("Not Found"))
                .andExpect(jsonPath("$.mensagem").value(mensagemErro));
    }

    @Test
    void deletaCarroComIdInexistente_CodigoStatus_404() throws Exception {
        long id = 1;
        Carro carro = new Carro("FORD", "Mustang", "ABC-1234", 2021, "Esportivo", "Gasolina", 450.75);
        String mensagemErro = "Carro com Id: " + id + " Não Encontrado!";

        when(carroService.entidadePorId(id)).thenReturn(Optional.of(carro));
        doThrow(new RecursoNaoEncontradoException(mensagemErro))
                .when(carroService).deletaEntidade(id);

        mockMvc.perform(delete("/api/carros/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigoErro").value(404))
                .andExpect(jsonPath("$.tituloErro").value("Not Found"))
                .andExpect(jsonPath("$.mensagem").value(mensagemErro));
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
