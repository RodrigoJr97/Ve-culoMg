package com.veiculosmg.controller;

import com.veiculosmg.dto.AluguelDTO;
import com.veiculosmg.dto.AluguelResponseDTO;
import com.veiculosmg.model.entity.Aluguel;
import com.veiculosmg.service.interfaceService.AluguelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/alugueis", produces = {"application/json"})
public class AluguelController {

    private final AluguelService aluguelService;

    public AluguelController(AluguelService aluguelService) {
        this.aluguelService = aluguelService;
    }

    @PostMapping
    public ResponseEntity<Aluguel> createCarro(@Valid @RequestBody AluguelDTO aluguelDTO) {
        Aluguel aluguel = aluguelService.criarAluguel(aluguelDTO);
        return new ResponseEntity<>(aluguel, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AluguelResponseDTO>> getCarros() {
        return ResponseEntity.ok(aluguelService.listarAlugueis());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AluguelResponseDTO> getAluguelById(@PathVariable Long id) {
        return ResponseEntity.ok(aluguelService.buscarAluguelPorId(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAluguelById(@PathVariable Long id) {
        aluguelService.excluirAluguel(id);
        return ResponseEntity.noContent().build();
    }

}
