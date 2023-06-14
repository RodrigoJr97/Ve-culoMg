package com.veiculosmg.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.veiculosmg.utilitarios.anotacoes.ValidacaoNumeroTelefone;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = {"cpf", "numeroTelefone", "email"})
@ToString
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Nome Obrigatório")
    private String nome;

    @NotNull(message = "CPF não poder ser nulo")
    @CPF(message = "CPF inválido")
    @Column(unique = true)
    private String cpf;

    @ValidacaoNumeroTelefone
    @Column(unique = true)
    private String numeroTelefone;

    @NotBlank(message = "E-mail Obrigatório")
    @Email(message = "Endereço de e-mail inválido")
    @Column(unique = true)
    private String email;

    @NotNull(message = "Data de Nascimento Obrigatória")
    private LocalDate dataNascimento;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Endereco endereco;

    @JsonIgnore
    @OneToOne(mappedBy = "cliente")
    private Aluguel aluguel;

    public Cliente(String nome, String cpf, String numeroTelefone, String email, LocalDate dataNascimento) {
        this.nome = nome;
        this.cpf = cpf;
        this.numeroTelefone = numeroTelefone;
        this.email = email;
        this.dataNascimento = dataNascimento;
    }
    public Cliente(String nome, String cpf, String numeroTelefone, String email, LocalDate dataNascimento, Endereco endereco) {
        this.nome = nome;
        this.cpf = cpf;
        this.numeroTelefone = numeroTelefone;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.endereco = endereco;
    }
}
