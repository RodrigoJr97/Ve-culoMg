package com.veiculosmg.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.veiculosmg.model.enums.StatusAluguel;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aluguel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "carro_id", referencedColumnName = "id")
    private Carro carro;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
    private Cliente cliente;

    private LocalDate dataInicio;

    private LocalDate dataFim;

    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    private StatusAluguel statusAluguel;


}
