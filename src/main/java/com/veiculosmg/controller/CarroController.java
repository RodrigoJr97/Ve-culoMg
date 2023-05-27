package com.veiculosmg.controller;

import com.veiculosmg.model.entity.Carro;
import com.veiculosmg.service.implementacao.ImplCarroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/carro/v23")
public class CarroController {

    private final ImplCarroService carroService;

    public CarroController(ImplCarroService carroService) {
        this.carroService = carroService;
    }

    @GetMapping
    public ResponseEntity<List<Carro>> getCarros() {
        List<Carro> listaCarros = carroService.listaEntidades();

        if (listaCarros.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(listaCarros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> carro(@PathVariable Long id) {
        Optional<Carro> carro = carroService.entidadePorId(id);

        return new ResponseEntity<>(carro, HttpStatus.OK);
    }

    @GetMapping("/disponivel")
    public ResponseEntity<?> listaDisponivel() {
        List<Carro> disponivel = carroService.disponivel();

        if (disponivel.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(disponivel);
    }

    @GetMapping("/cat/{categoria}")
    public ResponseEntity<?> listaCategoria(@PathVariable String categoria) {
        List<Carro> listaCategoria = carroService.listCategoria(categoria);

        if (listaCategoria.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(listaCategoria);
    }

    @PostMapping
    public ResponseEntity<Carro> saveCarro(@Valid @RequestBody Carro carro) {
        Carro novoCarro = carroService.salvaNovaEntidade(carro);
        return new ResponseEntity<>(novoCarro, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCarro(@PathVariable Long id) {
        Optional<Carro> carro = carroService.entidadePorId(id);

        return carro.map(car -> {
            carroService.deletaEntidade(id);
            return ResponseEntity.noContent().build();
        }).orElseThrow();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCarro(@Valid @RequestBody Carro carro, @PathVariable Long id) {
        Optional<Carro> carroExiste = carroService.entidadePorId(id);

        return carroExiste.map(car -> {
            carroService.updateEntidade(carro, id);
            return new ResponseEntity<>(car, HttpStatus.OK);
        }).orElse(ResponseEntity.notFound().build());
    }

}
