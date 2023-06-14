package com.veiculosmg.dto;

import com.veiculosmg.model.enums.StatusAluguel;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
public class AluguelResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String numeroTelefone;
    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;
    private String marca;
    private String modelo;
    private String placa;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private BigDecimal valorTotal;
    private StatusAluguel statusAluguel;

}
