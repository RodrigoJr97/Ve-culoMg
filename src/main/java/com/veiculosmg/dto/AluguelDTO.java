package com.veiculosmg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AluguelDTO {

    private String nomeCliente;
    private String cpfCliente;
    private String placaCarro;
    private int diasAluguel;

}
