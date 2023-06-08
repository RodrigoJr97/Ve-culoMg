package com.veiculosmg.controller;

import com.veiculosmg.model.entity.Cliente;
import com.veiculosmg.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "api/clientes", produces = {"application/json"})
@Tag(name = "api/clientes")
@Slf4j
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(summary = "Criação de novo cliente.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Criação realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro no corpo da requisição"),
            @ApiResponse(responseCode = "409", description = "Atributos duplicados"),
            @ApiResponse(responseCode = "500", description = "Erro ao criar novo cliente"),
    })
    @PostMapping
    public ResponseEntity<Cliente> createCliente(@Valid @RequestBody Cliente cliente) {
        clienteService.salvaNovaEntidade(cliente);
        return new ResponseEntity<>(cliente, HttpStatus.CREATED);
    }

    @Operation(summary = "Busca todos clientes cadastrados.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca lista de clientes realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar lista de clientes"),
    })
    @GetMapping
    public ResponseEntity<List<Cliente>> getClientes() {
        return ResponseEntity.ok(clienteService.listaEntidades());
    }

    @Operation(summary = "Busca cliente pelo id informado.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca cliente pelo id realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente com o id informado não foi encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar cliente pelo id"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getClienteById(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.entidadePorId(id));
    }

    @Operation(summary = "Atualizar cliente pelo id informado.", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualização do cliente pelo id realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente com o id informado não foi encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao atualizar cliente"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateClienteById(@Valid @RequestBody Cliente clienteAtualizado, @PathVariable Long id) {
        clienteService.updateEntidade(clienteAtualizado, id);
        return ResponseEntity.ok(clienteAtualizado);
    }

    @Operation(summary = "Deleta cliente pelo id.", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete do cliente realizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente com o dd informado não foi encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar o delete do carro"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClienteById(@PathVariable Long id) {
        clienteService.deletaEntidade(id);
        return ResponseEntity.noContent().build();
    }

}
