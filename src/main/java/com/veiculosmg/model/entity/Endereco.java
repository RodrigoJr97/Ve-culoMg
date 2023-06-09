package com.veiculosmg.model.entity;

import com.veiculosmg.utilitarios.anotacoes.CEP;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Endereco implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CEP
    private String cep;

    @NotBlank(message = "Logradouro obrigatório")
    private String logradouro;

    private String complemento;

    @NotBlank(message = "Bairro obrigatório")
    private String bairro;

    @NotBlank(message = "Localidade obrigatório")
    private String localidade;

    @NotBlank(message = "UF obrigatório")
    private String uf;

    public Endereco(String cep, String logradouro, String complemento, String bairro, String localidade, String uf) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.complemento = complemento;
        this.bairro = bairro;
        this.localidade = localidade;
        this.uf = uf;
    }
}
