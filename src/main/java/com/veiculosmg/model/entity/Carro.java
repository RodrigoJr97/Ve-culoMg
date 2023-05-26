package com.veiculosmg.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

@Entity
public class Carro {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Marca Obrigatório")
    @Column(length = 25)
    private String marca;

    @NotBlank(message = "Modelo Obrigatório")
    @Column(length = 30)
    private String modelo;

    @NotBlank(message = "Placa Obrigatório")
    @Column(length = 6, unique = true)
    private String placa;

    @Column(nullable = false)
    private int ano;

    @NotBlank
    @Column(length = 15)
    private String categoria;

    @Column(nullable = false)
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
                ", preco=" + valorDiaria +
                ", disponivel=" + disponivel +
                '}';
    }

}
