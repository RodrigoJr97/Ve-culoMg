package com.veiculosmg.controller;

import com.veiculosmg.model.entity.Endereco;
import com.veiculosmg.service.EnderecoService;
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

    @PostMapping
    public ResponseEntity<Endereco> createEndereco(@Valid @RequestBody Endereco endereco) {
        enderecoService.salvaNovaEntidade(endereco);
        return new ResponseEntity<>(endereco, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Endereco>> getEnderecos() {
        return ResponseEntity.ok(enderecoService.listaEntidades());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEnderecoById(@PathVariable Long id) {
        return ResponseEntity.ok(enderecoService.entidadePorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEnderecoById(@Valid @RequestBody Endereco enderecoAtualizado, @PathVariable Long id) {
        enderecoService.updateEntidade(enderecoAtualizado, id);
        return ResponseEntity.ok(enderecoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEnderecoById(@PathVariable Long id) {
        enderecoService.deletaEntidade(id);
        return ResponseEntity.noContent().build();
    }

}
