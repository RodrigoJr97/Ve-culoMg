package com.veiculosmg.controller;

import com.veiculosmg.model.entity.Endereco;
import com.veiculosmg.service.EnderecoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/enderecos", produces = {"application/json"})
public class EnderecoController {

    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @Operation(summary = "Criação de novo endereço.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Criação realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro no corpo da requisição"),
            @ApiResponse(responseCode = "500", description = "Erro ao criar novo endereço"),
    })
    @PostMapping
    public ResponseEntity<Endereco> createEndereco(@Valid @RequestBody Endereco endereco) {
        enderecoService.salvaNovaEntidade(endereco);
        return new ResponseEntity<>(endereco, HttpStatus.CREATED);
    }

    @Operation(summary = "Busca todos endereços cadastrados.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca lista de endereços realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar lista de endereços"),
    })
    @GetMapping
    public ResponseEntity<List<Endereco>> getEnderecos() {
        return ResponseEntity.ok(enderecoService.listaEntidades());
    }

    @Operation(summary = "Busca endereço pelo id informado.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca endereço pelo id realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Endereço com o id informado não foi encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar endereço pelo id"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getEnderecoById(@PathVariable Long id) {
        return ResponseEntity.ok(enderecoService.entidadePorId(id));
    }

    @Operation(summary = "Atualizar endereço pelo id informado.", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualização do endereço pelo id realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Endereço com o id informado não foi encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao atualizar o endereço"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEnderecoById(@Valid @RequestBody Endereco enderecoAtualizado, @PathVariable Long id) {
        enderecoService.updateEntidade(enderecoAtualizado, id);
        return ResponseEntity.ok(enderecoAtualizado);
    }

    @Operation(summary = "Deleta endereço pelo id.", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete do endereço realizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Endereço com o dd informado não foi encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar o delete do endereço"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEnderecoById(@PathVariable Long id) {
        enderecoService.deletaEntidade(id);
        return ResponseEntity.noContent().build();
    }

}
