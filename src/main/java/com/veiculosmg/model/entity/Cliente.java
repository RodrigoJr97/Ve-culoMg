package com.veiculosmg.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"cpf", "telefone", "email"})
@ToString
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Nome Obrigatório")
    private String nome;

    @NotBlank(message = "CPF Obrigatório")
    private String cpf;

    @NotBlank(message = "Telefone Obrigatório")
    private String telefone;

    @NotBlank(message = "Email Obrigatório")
    private String email;

    @NotBlank(message = "Data de Nascimento Obrigatória")
    private LocalDate dataNascimento;

}
