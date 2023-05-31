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
@RequestMapping("api/carro/v23")
@Slf4j
public class CarroController {

    private final ImplCarroService carroService;

    public CarroController(ImplCarroService carroService) {
        this.carroService = carroService;
    }

    @GetMapping
    public ResponseEntity<List<Carro>> getCarros() {
        log.info("Iniciando requisição para api/carro/v23");
        List<Carro> listaCarros = carroService.listaEntidades();

        if (listaCarros.isEmpty()) {
            log.info("Requisição para api/carro/v23 concluída. Lista vazia.");
            return ResponseEntity.notFound().build();
        }

        log.info("Requisição para api/carro/v23 concluída.");
        return ResponseEntity.ok(listaCarros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> carro(@PathVariable Long id) {
        log.info("Iniciando requisição para api/carro/v23/{}", id);
        Optional<Carro> carro = carroService.entidadePorId(id);

        log.info("Requisição para api/carro/v23/{}", id + " concluída.");
        return new ResponseEntity<>(carro, HttpStatus.OK);
    }

    @GetMapping("/disponivel")
    public ResponseEntity<?> listaDisponivel() {
        log.info("Iniciando requisição para api/carro/v23/disponivel");
        List<Carro> disponivel = carroService.disponivel();

        if (disponivel.isEmpty()) {
            log.info("Requisição para api/carro/v23/disponivel concluída. Lista vazia.");
            return ResponseEntity.notFound().build();
        }

        log.info("Requisição para api/carro/v23/disponivel concluída.");
        return ResponseEntity.ok(disponivel);
    }

    @GetMapping("/cat/{categoria}")
    public ResponseEntity<?> listaCategoria(@PathVariable String categoria) {
        log.info("Iniciando requisição para api/carro/v23/cat/{}", categoria.toUpperCase());
        List<Carro> listaCategoria = carroService.listCategoria(categoria);

        if (listaCategoria.isEmpty()) {
            log.info("Requisição para api/carro/v23/cat/{}", categoria.toUpperCase() + " concluída. Lista vazia.");
            return ResponseEntity.notFound().build();
        }

        log.info("Requisição para api/carro/v23/cat/{}", categoria.toUpperCase() + " concluída.");
        return ResponseEntity.ok(listaCategoria);
    }

    @PostMapping
    public ResponseEntity<Carro> saveCarro(@Valid @RequestBody Carro carro) {
        log.info("Iniciando requisição para api/carro/v23");
        Carro novoCarro = carroService.salvaNovaEntidade(carro);

        log.info("Requisição para api/carro/v23 concluída.");
        return new ResponseEntity<>(novoCarro, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCarro(@PathVariable Long id) {
        log.info("Iniciando requisição para api/carro/v23/{}", id);
        Optional<Carro> carro = carroService.entidadePorId(id);

        return carro.map(car -> {
            carroService.deletaEntidade(id);
            log.info("Requisição para api/carro/v23/{}", id + " concluída!");
            return ResponseEntity.noContent().build();
        }).orElseThrow();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCarro(@Valid @RequestBody Carro carro, @PathVariable Long id) {
        log.info("Iniciando requisição para api/carro/v23/{}", id);
        log.info("Body da requisição Carro:{}", carro);

        Optional<Carro> carroExiste = carroService.entidadePorId(id);

        return carroExiste.map(car -> {
            carroService.updateEntidade(carro, id);
            return new ResponseEntity<>(car, HttpStatus.OK);
        }).orElse(ResponseEntity.notFound().build());
    }

}
