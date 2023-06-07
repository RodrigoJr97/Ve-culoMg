package com.veiculosmg.controller;

import com.veiculosmg.model.entity.Cliente;
import com.veiculosmg.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
        log.info("Iniciando requisiçao para criar novo cliente. Path:'api/clientes'");
        clienteService.salvaNovaEntidade(cliente);

        log.info("Criação de novo cliente concluída.'");
        return ResponseEntity.ok(cliente);
    }

    @Operation(summary = "Busca todos clientes cadastrados.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca lista de clientes realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao criar novo cliente"),
    })
    @GetMapping
    public ResponseEntity<List<Cliente>> getClientes() {
        log.info("Iniciando requisiçao para buscar lista de cliente. Path:'api/clientes'");
        List<Cliente> listaClientes = clienteService.listaEntidades();

        log.info("Busca lista de clientes concluída.");
        return ResponseEntity.ok(listaClientes);
    }

    @Operation(summary = "Busca cliente pelo id informado.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca cliente pelo id realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente com o id informado não foi encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao criar novo cliente"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getClienteById(@PathVariable Long id) {
        log.info("Iniciando requisiçao para buscar cliente pelo id. Path:'api/clientes/{}'", id);
        Optional<Cliente> cliente = clienteService.entidadePorId(id);

        log.info("Busca cliente pelo id concluída.");
        return ResponseEntity.ok(cliente);
    }

    @Operation(summary = "Atualiza cliente pelo id informado.", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualização do cliente pelo id realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente com o id informado não foi encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao criar novo cliente"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateClienteById(@Valid @RequestBody Cliente clienteAtualizado, @PathVariable Long id) {
        log.info("Iniciando requisiçao para buscar cliente pelo id. Path:'api/clientes/{}'", id);
        log.info("Body da requisição Cliente:{}", clienteAtualizado);

        Optional<Cliente> clienteComIdInformado = clienteService.entidadePorId(id);

        return clienteComIdInformado.map(cliente -> {
            clienteService.updateEntidade(clienteAtualizado, id);

            log.info("Atualização do cliente relizada com sucesso");
            return ResponseEntity.ok(cliente);
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Deleta cliente pelo Id.", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete do cliente realizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente com o dd informado não foi encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar o delete do carro"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClienteById(@PathVariable Long id) {
        log.info("Iniciando requisiçao para deletar cliente pelo id. Path:'api/clientes/{}'", id);
        Optional<Cliente> cliente = clienteService.entidadePorId(id);

        return cliente.map(cli -> {
            clienteService.deletaEntidade(id);

            log.info("Delete do cliente relizada com sucesso");
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

}
