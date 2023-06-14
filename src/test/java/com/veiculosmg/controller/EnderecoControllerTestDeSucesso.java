package com.veiculosmg.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veiculosmg.model.entity.Endereco;
import com.veiculosmg.service.implementacaoService.ImplEnderecoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EnderecoControllerTestDeSucesso {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ImplEnderecoService enderecoService;

    private List<Endereco> listaEnderecos;

    @BeforeEach
    public void setUp() {
        listaEnderecos = Arrays.asList(
                new Endereco("38402200", "Rua Teste API 1", "", "Brasil", "Uberlandia", "MG"),
                new Endereco("38402201", "Rua Teste API 2", "", "Martins", "Uberlandia", "MG"));
    }


    @Test
    void testSalvaNovoEnderecoComSucesso_CodigoStatus_201() throws Exception {
        Endereco novoEndereco = listaEnderecos.get(0);
        String requestBody = asJsonString(novoEndereco);

        when(enderecoService.salvaNovaEntidade(novoEndereco)).thenReturn(novoEndereco);

        this.mockMvc.perform(post("/api/enderecos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cep").value("38402200"))
                .andExpect(jsonPath("$.logradouro").value("Rua Teste API 1"))
                .andExpect(jsonPath("$.complemento").value(""))
                .andExpect(jsonPath("$.bairro").value("Brasil"))
                .andExpect(jsonPath("$.localidade").value("Uberlandia"))
                .andExpect(jsonPath("$.uf").value("MG"));

        verify(enderecoService).salvaNovaEntidade(novoEndereco);
        verifyNoMoreInteractions(enderecoService);
    }

    @Test
    void testRetornaListaDeEnderecosComSucesso_CodigoStatus_200() throws Exception {
        when(enderecoService.listaEntidades()).thenReturn(listaEnderecos);
        String json = "[{\"cep\": \"38402200\", \"logradouro\": \"Rua Teste API 1\", \"complemento\": \"\", \"bairro\": \"Brasil\", \"localidade\": \"Uberlandia\", \"uf\": \"MG\"},"
                + "{\"cep\": \"38402201\", \"logradouro\": \"Rua Teste API 2\", \"complemento\": \"\", \"bairro\": \"Martins\", \"localidade\": \"Uberlandia\", \"uf\": \"MG\"}]";

        this.mockMvc.perform(get("/api/enderecos"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(json));

        verify(enderecoService).listaEntidades();
        verifyNoMoreInteractions(enderecoService);
    }

    @Test
    void testRetornaEnderecoPeloIdComSucesso_CodigoStatus_200() throws Exception {
        Endereco endereco = listaEnderecos.get(0);
        endereco.setId(1L);
        long id = endereco.getId();

        when(enderecoService.entidadePorId(id)).thenReturn(Optional.of(endereco));

        this.mockMvc.perform(get("/api/enderecos/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cep").value("38402200"))
                .andExpect(jsonPath("$.logradouro").value("Rua Teste API 1"))
                .andExpect(jsonPath("$.complemento").value(""))
                .andExpect(jsonPath("$.bairro").value("Brasil"))
                .andExpect(jsonPath("$.localidade").value("Uberlandia"))
                .andExpect(jsonPath("$.uf").value("MG"));

        verify(enderecoService).entidadePorId(id);
        verifyNoMoreInteractions(enderecoService);
    }

    @Test
    void testAtualizaEnderecoPassandoONovoEnderecoEOIdComSucesso_CodigoStatus_200() throws Exception {
        long id = 1;
        Endereco novoEndereco = listaEnderecos.get(1);
        novoEndereco.setId(id);

        String requestBody = asJsonString(novoEndereco);

        when(enderecoService.entidadePorId(id)).thenReturn(Optional.of(novoEndereco));
        doNothing().when(enderecoService).updateEntidade(novoEndereco, id);

        mockMvc.perform(put("/api/enderecos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cep").value("38402201"))
                .andExpect(jsonPath("$.logradouro").value("Rua Teste API 2"))
                .andExpect(jsonPath("$.complemento").value(""))
                .andExpect(jsonPath("$.bairro").value("Martins"))
                .andExpect(jsonPath("$.localidade").value("Uberlandia"))
                .andExpect(jsonPath("$.uf").value("MG"));

        verify(enderecoService).updateEntidade(novoEndereco, id);
    }

    @Test
    void testDeletaOEnderecoPeloIdInformadoComSucesso_CodigoStatus_204() throws Exception {
        long id = 1L;
        Endereco endereco = listaEnderecos.get(0);

        when(enderecoService.entidadePorId(id)).thenReturn(Optional.ofNullable(endereco));
        doNothing().when(enderecoService).deletaEntidade(id);

        mockMvc.perform(delete("/api/enderecos/{id}", id))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(enderecoService).deletaEntidade(id);
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
