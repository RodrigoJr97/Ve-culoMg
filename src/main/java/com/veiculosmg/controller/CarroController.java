package com.veiculosmg.controller;

import com.veiculosmg.model.entity.Carro;
import com.veiculosmg.service.implementacao.ImplCarroService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/carros")
@Slf4j
public class CarroController {

    private final ImplCarroService carroService;

    public CarroController(ImplCarroService carroService) {
        this.carroService = carroService;
    }

    @GetMapping
    public ResponseEntity<List<Carro>> getCarros() {
        log.info("Iniciando requisição para api/carros");
        List<Carro> listaCarros = carroService.listaEntidades();

        if (listaCarros.isEmpty()) {
            log.info("Requisição para api/carros concluída. Lista vazia.");
            return ResponseEntity.notFound().build();
        }

        log.info("Requisição para api/carros concluída.");
        return ResponseEntity.ok(listaCarros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCarroById(@PathVariable Long id) {
        log.info("Iniciando requisição para api/carros/{}", id);
        Optional<Carro> carro = carroService.entidadePorId(id);

        log.info("Requisição para api/carros/{}", id + " concluída.");
        return new ResponseEntity<>(carro, HttpStatus.OK);
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<?> getCarrosDisponiveis() {
        log.info("Iniciando requisição para api/carros/disponiveis");
        List<Carro> disponivel = carroService.disponivel();

        if (disponivel.isEmpty()) {
            log.info("Requisição para api/carros/disponiveis concluída. Lista vazia.");
            return ResponseEntity.notFound().build();
        }

        log.info("Requisição para api/carros/disponiveis concluída.");
        return ResponseEntity.ok(disponivel);
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<?> getCarrosPorCategoria(@PathVariable String categoria) {
        log.info("Iniciando requisição para api/carros/categoria/{}", categoria.toUpperCase());
        List<Carro> listaCategoria = carroService.listCategoria(categoria);

        if (listaCategoria.isEmpty()) {
            log.info("Requisição para api/carros/categoria/{}", categoria.toUpperCase() + " concluída. Lista vazia.");
            return ResponseEntity.notFound().build();
        }

        log.info("Requisição para api/carros/categoria/{}", categoria.toUpperCase() + " concluída.");
        return ResponseEntity.ok(listaCategoria);
    }

    @PostMapping
    public ResponseEntity<Carro> createCarro(@Valid @RequestBody Carro carro) {
        log.info("Iniciando requisição para api/carros");
        Carro novoCarro = carroService.salvaNovaEntidade(carro);

        log.info("Requisição para api/carros concluída.");
        return new ResponseEntity<>(novoCarro, HttpStatus.CREATED);
    }

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
