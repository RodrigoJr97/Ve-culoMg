package com.veiculosmg.controller;

import com.veiculosmg.model.entity.Carro;
import com.veiculosmg.service.implementacao.ImplCarroService;
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
@RequestMapping(value = "api/carros", produces = {"application/json"})
@Tag(name = "api/carros")
@Slf4j
public class CarroController {

    private final ImplCarroService carroService;

    public CarroController(ImplCarroService carroService) {
        this.carroService = carroService;
    }

    @Operation(summary = "Busca todos os carros cadastrados.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca de carros"),
    })
    @GetMapping
    public ResponseEntity<List<Carro>> getCarros() {
        log.info("Iniciando requisição para api/carros");
        List<Carro> listaCarros = carroService.listaEntidades();

        log.info("Requisição para api/carros concluída.");
        return ResponseEntity.ok(listaCarros);
    }

    @Operation(summary = "Busca carro por Id.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Carro com o Id informado não foi encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca de carro por Id"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getCarroById(@PathVariable Long id) {
        log.info("Iniciando requisição para api/carros/{}", id);
        Optional<Carro> carro = carroService.entidadePorId(id);

        log.info("Requisição para api/carros/{}", id + " concluída.");
        return new ResponseEntity<>(carro, HttpStatus.OK);
    }

    @Operation(summary = "Busca carros disponíveis.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca de carros disponíveis"),
    })
    @GetMapping("/disponiveis")
    public ResponseEntity<?> getCarrosDisponiveis() {
        log.info("Iniciando requisição para api/carros/disponiveis");
        List<Carro> disponivel = carroService.disponivel();

        if (disponivel.isEmpty()) {
            log.info("Requisição para api/carros/disponiveis concluída. Nenhum carro disponível encontrado.");
            return ResponseEntity.ok("Nenhum carro disponível encontrado.");
        }

        log.info("Requisição para api/carros/disponiveis concluída.");
        return ResponseEntity.ok(disponivel);
    }

    @Operation(summary = "Busca carros por categoria.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca de carros pela categoria"),
    })
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<?> getCarrosPorCategoria(@PathVariable String categoria) {
        log.info("Iniciando requisição para api/carros/categoria/{}", categoria.toUpperCase());
        List<Carro> listaCategoria = carroService.listCategoria(categoria);

        if (listaCategoria.isEmpty()) {
            log.info("Requisição para api/carros/categoria/{}", categoria.toUpperCase() + " concluída. Lista vazia.");
            return ResponseEntity.ok("Nenhum carro da categoria " + categoria.toUpperCase() + " foi encontrado.");
        }

        log.info("Requisição para api/carros/categoria/{}", categoria.toUpperCase() + " concluída.");
        return ResponseEntity.ok(listaCategoria);
    }

    @Operation(summary = "Criação de novo carro.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Criação realizada com sucesso"),
            @ApiResponse(responseCode = "409", description = "Placa informada já está cadastrada"),
            @ApiResponse(responseCode = "500", description = "Erro ao criar novo carro"),
    })
    @PostMapping
    public ResponseEntity<Carro> createCarro(@Valid @RequestBody Carro carro) {
        log.info("Iniciando requisição para api/carros");
        Carro novoCarro = carroService.salvaNovaEntidade(carro);

        log.info("Requisição para api/carros concluída.");
        return new ResponseEntity<>(novoCarro, HttpStatus.CREATED);
    }

    @Operation(summary = "Deleta carro pelo Id.", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete do carro realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Carro com o Id informado não foi encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar o delete do carro"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCarroById(@PathVariable Long id) {
        log.info("Iniciando requisição para api/carros/{}", id);
        Optional<Carro> carro = carroService.entidadePorId(id);

        return carro.map(car -> {
            carroService.deletaEntidade(id);
            log.info("Requisição para api/carros/{}", id + " concluída!");
            return ResponseEntity.noContent().build();
        }).orElseThrow();
    }

    @Operation(summary = "Update do carro pelo Id.", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update realizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Carro com o Id informado não foi encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar o update do carro"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCarroById(@Valid @RequestBody Carro carro, @PathVariable Long id) {
        log.info("Iniciando requisição para api/carros/{}", id);
        log.info("Body da requisição Carro:{}", carro);

        Optional<Carro> carroExiste = carroService.entidadePorId(id);

        return carroExiste.map(car -> {
            carroService.updateEntidade(carro, id);
            return new ResponseEntity<>(car, HttpStatus.OK);
        }).orElse(ResponseEntity.notFound().build());
    }

}
