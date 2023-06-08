package com.veiculosmg.controller;

import com.veiculosmg.model.entity.Carro;
import com.veiculosmg.service.implementacao.ImplCarroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/carros", produces = {"application/json"})
@Tag(name = "api/carros")
public class CarroController {

    private final ImplCarroService carroService;

    public CarroController(ImplCarroService carroService) {
        this.carroService = carroService;
    }

    @Operation(summary = "Criação de novo carro.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Criação realizada com sucesso"),
            @ApiResponse(responseCode = "409", description = "Placa informada já está cadastrada"),
            @ApiResponse(responseCode = "500", description = "Erro ao criar novo carro"),
    })
    @PostMapping
    public ResponseEntity<Carro> createCarro(@Valid @RequestBody Carro carro) {
        carroService.salvaNovaEntidade(carro);
        return new ResponseEntity<>(carro, HttpStatus.CREATED);
    }

    @Operation(summary = "Busca todos carros cadastrados.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca lista de carros realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca de carros"),
    })
    @GetMapping
    public ResponseEntity<List<Carro>> getCarros() {
        return ResponseEntity.ok(carroService.listaEntidades());
    }

    @Operation(summary = "Busca carro por id informado.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Carro com o id informado não foi encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca de carro por id"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getCarroById(@PathVariable Long id) {
        return ResponseEntity.ok(carroService.entidadePorId(id));
    }

    @Operation(summary = "Busca carros disponíveis.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca de carros disponiveis"),
    })
    @GetMapping("/disponiveis")
    public ResponseEntity<?> getCarrosDisponiveis() {
        List<Carro> disponivel = carroService.disponivel();
        if (disponivel.isEmpty()) {
            return ResponseEntity.ok("Nenhum carro disponivel encontrado.");
        }
        return ResponseEntity.ok(disponivel);
    }

    @Operation(summary = "Busca carros por categoria.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca de carros pela categoria"),
    })
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<?> getCarrosPorCategoria(@PathVariable String categoria) {
        List<Carro> listaCategoria = carroService.listCategoria(categoria);
        if (listaCategoria.isEmpty()) {
            return ResponseEntity.ok("Nenhum carro da categoria " + categoria.toUpperCase() + " foi encontrado.");
        }
        return ResponseEntity.ok(listaCategoria);
    }

    @Operation(summary = "Atualizar o carro pelo id.", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualização do carro pelo id realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Carro com o id informado não foi encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar o update do carro"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCarroById(@Valid @RequestBody Carro carroAtualizado, @PathVariable Long id) {
        carroService.updateEntidade(carroAtualizado, id);
        return ResponseEntity.ok(carroAtualizado);
    }

    @Operation(summary = "Deleta carro pelo id.", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete do carro realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Carro com o id informado não foi encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar o delete do carro"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCarroById(@PathVariable Long id) {
        carroService.deletaEntidade(id);
        return ResponseEntity.noContent().build();
    }

}
