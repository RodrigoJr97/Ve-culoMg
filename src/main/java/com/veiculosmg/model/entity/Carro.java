package com.veiculosmg.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.veiculosmg.exception.AtributoInvalidoException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = "placa")
@ToString
public class Carro {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Marca Obrigatória")
    @Column(length = 25)
    private String marca;

    @NotBlank(message = "Modelo Obrigatório")
    @Column(length = 30)
    private String modelo;

    @NotBlank(message = "Placa Obrigatória")
    @Column(length = 10, unique = true)
    private String placa;

    @NotNull(message = "Ano Obrigatório")
    private int ano;

    @NotBlank(message = "Categoria Obrigatória")
    @Column(length = 15)
    private String categoria;

    @NotBlank(message = "Tipo Combustível Obrigatório")
    private String tipoCombustivel;

    @NotNull(message = "Valor Diária Obrigatória")
    @Positive(message = "O valor da diária deve ser maior que Zero")
    private BigDecimal valorDiaria;

    private boolean disponivel = true;

    @JsonIgnore
    @OneToOne(mappedBy = "carro")
    private Aluguel aluguel;

    public Carro(String marca, String modelo, String placa, int ano, String categoria, String tipoCombustivel, BigDecimal valorDiaria, boolean disponivel) {
        this.marca = marca;
        this.modelo = modelo;
        this.placa = placa;
        this.ano = ano;
        this.categoria = categoria;
        this.tipoCombustivel = tipoCombustivel;
        this.valorDiaria = valorDiaria;
        this.disponivel = disponivel;
    }

    public Carro(String marca, String modelo, String placa, int ano, String categoria, String tipoCombustivel, BigDecimal valorDiaria) {
        this.marca = marca;
        this.modelo = modelo;
        this.placa = placa;
        this.ano = ano;
        this.categoria = categoria;
        this.tipoCombustivel = tipoCombustivel;
        this.valorDiaria = valorDiaria;
    }

    public Carro(Long id, String marca, String modelo, String placa, int ano, String categoria, String tipoCombustivel, BigDecimal valorDiaria) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.placa = placa;
        this.ano = ano;
        this.categoria = categoria;
        this.tipoCombustivel = tipoCombustivel;
        this.valorDiaria = valorDiaria;
    }

    public static void formataAtributos(Carro carro) {
       carro.setMarca(carro.getMarca().toUpperCase());
       carro.setModelo(carro.getModelo().toUpperCase());
       carro.setCategoria(carro.getCategoria().toUpperCase());
       carro.setTipoCombustivel(carro.getTipoCombustivel().toUpperCase());
       carro.setPlaca(formataPlaca.apply(carro.getPlaca()));
    }

    private static UnaryOperator<String> formataPlaca = placa -> {
        String regexPlacaModeloAntigo = "^[A-Za-z]{3}\\d{4}$";
        String regexPlacaMercosul = "^[A-Za-z]{3}\\d[A-Za-z]\\d{2}$";

        StringBuilder placaFormatada = new StringBuilder();
        String comecoDaPlaca = placa.substring(0, 3).toUpperCase();
        String finalDaPlaca = placa.substring(3);

        if (Pattern.matches(regexPlacaModeloAntigo, placa)) {
            placaFormatada.append(comecoDaPlaca).append("-").append(finalDaPlaca);
            return placaFormatada.toString();
        } else if(Pattern.matches(regexPlacaMercosul, placa)) {
            return placa.toUpperCase();
        }

        throw new AtributoInvalidoException("Formatos Válidos: ABC1234 ou ABC1D23");
    };

}
