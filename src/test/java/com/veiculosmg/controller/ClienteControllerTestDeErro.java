package com.veiculosmg.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.veiculosmg.exception.AtributoDuplicadoException;
import com.veiculosmg.exception.MenorDeIdadeException;
import com.veiculosmg.exception.RecursoNaoEncontradoException;
import com.veiculosmg.model.entity.Cliente;
import com.veiculosmg.service.implementacaoService.ImplClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ClienteControllerTestDeErro {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ImplClienteService clienteService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testSalvaNovoClienteComNomeCpfEmailNumeroTelefoneDuplicados_CodigoStatus_409() throws Exception {
        Cliente novoCliente = new Cliente("Karina Silva", "41368104070", "10911112220", "teste01@gmail.com", LocalDate.parse("2002-06-06"));
        String requestBody = objectMapper.writeValueAsString(novoCliente);

        List<String> atributosDuplicados = Arrays.asList("nome", "cpf", "email", "numeroTelefone");
        String mensagemErro = "Os seguintes atributos já estão vinculados a outro cliente: " + atributosDuplicados;

        when(clienteService.salvaNovaEntidade(novoCliente))
                .thenThrow(new AtributoDuplicadoException(mensagemErro));

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigoErro").value(409))
                .andExpect(jsonPath("$.tituloErro").value("Conflict"))
                .andExpect(jsonPath("$.mensagem").value(mensagemErro));
    }

    @Test
    void testSalvaNovoClienteMenorDeIdade_CodigoStatus_400() throws Exception {
        Cliente novoCliente = new Cliente("Karina Silva", "41368104070", "10911112220", "teste01@gmail.com", LocalDate.parse("2022-06-06"));
        String requestBody = objectMapper.writeValueAsString(novoCliente);

        String mensagemErro = "Cliente é menor de idade.";

        when(clienteService.salvaNovaEntidade(novoCliente))
                .thenThrow(new MenorDeIdadeException(mensagemErro));

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigoErro").value(400))
                .andExpect(jsonPath("$.tituloErro").value("Permite cadastro somente para maiores de 18"))
                .andExpect(jsonPath("$.mensagem").value(mensagemErro));
    }

    @Test
    void testAtualizaComNomeCpfEmailNumeroTelefoneDuplicados_CodigoStatus_409() throws Exception {
        long id = 1;
        Cliente clienteCadastrado = new Cliente("Karina Silva", "41368104070", "10911112220", "teste01@gmail.com", LocalDate.parse("2002-06-06"));
        Cliente novoCliente = new Cliente("Hugo Borges", "41368104070", "10911112220", "teste01@gmail.com", LocalDate.parse("2002-06-06"));

        String requestBody = objectMapper.writeValueAsString(novoCliente);
        List<String> atributosDuplicados = Arrays.asList("nome", "cpf", "email", "numeroTelefone");
        String mensagemErro = "Os seguintes atributos já estão vinculados a outro cliente: " + atributosDuplicados;

        when(clienteService.entidadePorId(id)).thenReturn(Optional.of(clienteCadastrado));
        doThrow(new AtributoDuplicadoException(mensagemErro))
                .when(clienteService).updateEntidade(novoCliente, id);

        mockMvc.perform(put("/api/clientes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigoErro").value(409))
                .andExpect(jsonPath("$.tituloErro").value("Conflict"))
                .andExpect(jsonPath("$.mensagem").value(mensagemErro));
    }

    @Test
    void testAtualizaClienteComIdInexistente_CodigoStatus_404() throws Exception {
        long id = 1;
        Cliente clienteAtualizado = new Cliente("Karina Silva", "41368104070", "10911112220", "teste01@gmail.com", LocalDate.parse("2002-06-06"));

        String requestBody = objectMapper.writeValueAsString(clienteAtualizado);
        String mensagemErro = "Cliente com Id: " + id + " Não Encontrado!";

        when(clienteService.entidadePorId(id)).thenReturn(Optional.of(clienteAtualizado));
        doThrow(new RecursoNaoEncontradoException(mensagemErro))
                .when(clienteService).updateEntidade(clienteAtualizado, id);

        mockMvc.perform(put("/api/clientes/{id}", id)
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
        Cliente cliente = new Cliente("Karina Silva", "41368104070", "10911112220", "teste01@gmail.com", LocalDate.parse("2002-06-06"));
        String mensagemErro = "Cliente com Id: " + id + " Não Encontrado!";

        when(clienteService.entidadePorId(id)).thenReturn(Optional.of(cliente));
        doThrow(new RecursoNaoEncontradoException(mensagemErro))
                .when(clienteService).deletaEntidade(id);

        mockMvc.perform(delete("/api/clientes/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigoErro").value(404))
                .andExpect(jsonPath("$.tituloErro").value("Not Found"))
                .andExpect(jsonPath("$.mensagem").value(mensagemErro));
    }

}
