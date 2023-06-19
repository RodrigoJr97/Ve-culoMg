package com.veiculosmg.service.interfaceService;

import com.veiculosmg.dto.AluguelDTO;
import com.veiculosmg.dto.AluguelResponseDTO;
import com.veiculosmg.model.entity.Aluguel;

import java.util.List;

public interface AluguelService {

    Aluguel criarAluguel(AluguelDTO aluguelDTO);
    List<AluguelResponseDTO> listarAlugueis();
    AluguelResponseDTO buscarAluguelPorId(Long id);
    void atualizaAluguel(Long id);
    void excluirAluguel(Long id);

}
