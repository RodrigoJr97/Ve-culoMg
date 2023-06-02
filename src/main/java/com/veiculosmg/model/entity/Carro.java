package com.veiculosmg.model.entity;

import com.veiculosmg.exception.AtributoInvalidoException;
import com.veiculosmg.utilitarios.FormataNome;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

@Entity
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
    private double valorDiaria;

    private boolean disponivel = true;

    public Carro(String marca, String modelo, String placa, int ano, String categoria, double valorDiaria) {
        this.marca = marca;
        this.modelo = modelo;
        this.placa = placa;
        this.ano = ano;
        this.categoria = categoria;
        this.valorDiaria = valorDiaria;
    }

    public Carro() { }

    public static void formataAtributos(Carro carro) {
       carro.setMarca(FormataNome.formatacaoNome.apply(carro.getMarca()));
       carro.setModelo(FormataNome.formatacaoNome.apply(carro.getModelo()));
       carro.setCategoria(FormataNome.formatacaoNome.apply(carro.getCategoria()));
       carro.setTipoCombustivel(FormataNome.formatacaoNome.apply(carro.getTipoCombustivel()));
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTipoCombustivel() {
        return tipoCombustivel;
    }

    public void setTipoCombustivel(String tipoCombustivel) {
        this.tipoCombustivel = tipoCombustivel;
    }

    public double getValorDiaria() {
        return valorDiaria;
    }

    public void setValorDiaria(double valorDiaria) {
        this.valorDiaria = valorDiaria;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carro carro = (Carro) o;
        return Objects.equals(placa, carro.placa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placa);
    }

    @Override
    public String toString() {
        return "Carro{" +
                "id=" + id +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", placa='" + placa + '\'' +
                ", ano=" + ano +
                ", categoria='" + categoria + '\'' +
                ", tipoCombustivel='" + tipoCombustivel + '\'' +
                ", valorDiaria=" + valorDiaria +
                ", disponivel=" + disponivel +
                '}';
    }
}
