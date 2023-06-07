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

    @Operation(summary = "Criação de novo carro.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Criação realizada com sucesso"),
            @ApiResponse(responseCode = "409", description = "Placa informada já está cadastrada"),
            @ApiResponse(responseCode = "500", description = "Erro ao criar novo carro"),
    })
    @PostMapping
    public ResponseEntity<Carro> createCarro(@Valid @RequestBody Carro carro) {
        log.info("Iniciando requisiçao para criar novo carro. Path:'api/carros'");
        carroService.salvaNovaEntidade(carro);

        log.info("Criação de novo carro concluída.");
        return new ResponseEntity<>(carro, HttpStatus.CREATED);
    }

    @Operation(summary = "Busca todos carros cadastrados.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca lista de carros realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca de carros"),
    })
    @GetMapping
    public ResponseEntity<List<Carro>> getCarros() {
        log.info("Iniciando requisiçao para buscar lista carro. Path:'api/carros'");
        List<Carro> listaCarros = carroService.listaEntidades();

        log.info("Busca lista de carros concluída.");
        return ResponseEntity.ok(listaCarros);
    }

    @Operation(summary = "Busca carro por id informado.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Carro com o Id informado não foi encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca de carro por Id"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getCarroById(@PathVariable Long id) {
        log.info("Iniciando requisiçao para buscar carro pelo id. Path:'api/carros/{}'", id);
        Optional<Carro> carro = carroService.entidadePorId(id);

        log.info("Busca carro pelo id concluída.");
        return ResponseEntity.ok(carro);
    }

    @Operation(summary = "Busca carros disponíveis.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca de carros disponiveis"),
    })
    @GetMapping("/disponiveis")
    public ResponseEntity<?> getCarrosDisponiveis() {
        log.info("Iniciando requisiçao para buscar lista carros disponiveis. Path:'api/carros/disponiveis'");
        List<Carro> disponivel = carroService.disponivel();

        if (disponivel.isEmpty()) {
            log.info("Busca carros disponíveis concluída. Nenhum carro disponivel encontrado.");
            return ResponseEntity.ok("Nenhum carro disponivel encontrado.");
        }

        log.info("Busca carros disponiveis concluída.");
        return ResponseEntity.ok(disponivel);
    }

    @Operation(summary = "Busca carros por categoria.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a busca de carros pela categoria"),
    })
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<?> getCarrosPorCategoria(@PathVariable String categoria) {
        log.info("Iniciando requisiçao para buscar carros pela categoria. Path:'api/carros/{}'", categoria.toUpperCase());
        List<Carro> listaCategoria = carroService.listCategoria(categoria);

        if (listaCategoria.isEmpty()) {
            log.info("Busca carros pela categoria concluída. Lista vazia.");
            return ResponseEntity.ok("Nenhum carro da categoria " + categoria.toUpperCase() + " foi encontrado.");
        }

        log.info("Busca carros pela categoria concluída.");
        return ResponseEntity.ok(listaCategoria);
    }

    @Operation(summary = "Atualiza do carro pelo Id.", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualização do carro pelo id realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Carro com o Id informado não foi encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar o update do carro"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCarroById(@Valid @RequestBody Carro carro, @PathVariable Long id) {
        log.info("Iniciando requisiçao para atualizar carro pelo id. Path:'api/carros/{}'", id);
        log.info("Body da requisição Carro:{}", carro);
        carroService.updateEntidade(carro, id);

        log.info("Atualização do carro realiazada com sucesso.");
        return ResponseEntity.ok(carro);
    }

    @Operation(summary = "Deleta carro pelo Id.", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete do carro realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Carro com o Id informado não foi encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar o delete do carro"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCarroById(@PathVariable Long id) {
        log.info("Iniciando requisiçao para deletar carro pelo id. Path:'api/carros/{}'", id);
        carroService.deletaEntidade(id);

        log.info("Delete do carro relizada com sucesso");
        return ResponseEntity.noContent().build();
    }

}
