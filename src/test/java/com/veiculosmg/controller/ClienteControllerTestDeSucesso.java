package com.veiculosmg.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.veiculosmg.model.entity.Cliente;
import com.veiculosmg.model.entity.Endereco;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ClienteControllerTestDeSucesso {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ImplClienteService clienteService;

    private List<Cliente> listaClientes;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        listaClientes = new ArrayList<>();
        Endereco endereco1 = new Endereco("38402200", "Rua Teste API 1", "", "Brasil", "Uberlandia", "MG");
        Endereco endereco2 = new Endereco("38402201", "Rua Teste API 2", "", "Martins", "Uberlandia", "MG");
        listaClientes.add(new Cliente("Karina Silva", "41368104070", "10911112220", "teste01@gmail.com", LocalDate.parse("2002-06-06"), endereco1));
        listaClientes.add(new Cliente("Hugo Borges", "33255435021", "20911112221", "teste02@gmail.com", LocalDate.parse("2002-06-06"), endereco2));
    }

    @Test
    void salvaNovoClienteComSucesso_CodigoStatus_201() throws Exception {
        Cliente novoCliente = new Cliente("Hugo Borges", "33255435021", "20911112221", "teste02@gmail.com", LocalDate.parse("2002-06-06"));
        String requestBody = objectMapper.writeValueAsString(novoCliente);

        when(clienteService.salvaNovaEntidade(novoCliente)).thenReturn(novoCliente);

        this.mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Hugo Borges"))
                .andExpect(jsonPath("$.cpf").value("33255435021"))
                .andExpect(jsonPath("$.numeroTelefone").value("20911112221"))
                .andExpect(jsonPath("$.email").value("teste02@gmail.com"))
                .andExpect(jsonPath("$.dataNascimento").value("2002-06-06"));

        verify(clienteService).salvaNovaEntidade(novoCliente);
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    void retornaListaDeClientesComSucesso_CodigoStatus_200() throws Exception {
        when(clienteService.listaEntidades()).thenReturn(listaClientes);
        String json = "[{\"nome\":\"Karina Silva\",\"cpf\":\"41368104070\",\"numeroTelefone\":\"10911112220\",\"email\":\"teste01@gmail.com\",\"dataNascimento\":\"2002-06-06\"},"
                + "{\"nome\":\"Hugo Borges\",\"cpf\":\"33255435021\",\"numeroTelefone\":\"20911112221\",\"email\":\"teste02@gmail.com\",\"dataNascimento\":\"2002-06-06\"}]";

        this.mockMvc.perform(get("/api/clientes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(json));

        verify(clienteService).listaEntidades();
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    void retornaListaVazia_CodigoStatus_200() throws Exception{
        List<Cliente> listaVazia = new ArrayList<>();

        when(clienteService.listaEntidades()).thenReturn(listaVazia);

        mockMvc.perform(get("/api/clientes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void retornaClientePeloIdComSucesso_CodigoStatus_200() throws Exception {
        Cliente cliente = listaClientes.get(1);
        cliente.setId(1L);
        long id = cliente.getId();

        when(clienteService.entidadePorId(id)).thenReturn(Optional.of(cliente));

        this.mockMvc.perform(get("/api/clientes/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Hugo Borges"))
                .andExpect(jsonPath("$.cpf").value("33255435021"))
                .andExpect(jsonPath("$.numeroTelefone").value("20911112221"))
                .andExpect(jsonPath("$.email").value("teste02@gmail.com"))
                .andExpect(jsonPath("$.dataNascimento").value("2002-06-06"));

        verify(clienteService).entidadePorId(id);
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    void atualizaClientePassandoONovoClienteEOIdComSucesso_CodigoStatus_200() throws Exception {
        long id = 1;
        Cliente novoCliente = listaClientes.get(1);
        novoCliente.setId(id);

        String requestBody = objectMapper.writeValueAsString(novoCliente);

        when(clienteService.entidadePorId(id)).thenReturn(Optional.of(novoCliente));
        doNothing().when(clienteService).updateEntidade(novoCliente, id);

        mockMvc.perform(put("/api/clientes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Hugo Borges"))
                .andExpect(jsonPath("$.cpf").value("33255435021"))
                .andExpect(jsonPath("$.numeroTelefone").value("20911112221"))
                .andExpect(jsonPath("$.email").value("teste02@gmail.com"))
                .andExpect(jsonPath("$.dataNascimento").value("2002-06-06"));

        verify(clienteService).updateEntidade(novoCliente, id);
    }

    @Test
    void deletaOClientePeloIdInformadoComSucesso_CodigoStatus_204() throws Exception {
        long id = 1L;
        Cliente cliente = listaClientes.get(0);

        when(clienteService.entidadePorId(id)).thenReturn(Optional.ofNullable(cliente));
        doNothing().when(clienteService).deletaEntidade(id);

        mockMvc.perform(delete("/api/clientes/{id}", id))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(clienteService).deletaEntidade(id);
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
