package com.veiculosmg.exception;

import java.time.LocalDateTime;

public class ResponseException {

    private LocalDateTime timestamp;
    private int codigoErro;
    private String erro;
    private String mensagem;

    public ResponseException(int codigo, String erro, String mensagem) {
        this.timestamp = LocalDateTime.now();
        this.codigoErro = codigo;
        this.erro = erro;
        this.mensagem = mensagem;
    }

    public ResponseException() {
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getCodigoErro() {
        return codigoErro;
    }

    public void setCodigoErro(int codigoErro) {
        this.codigoErro = codigoErro;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }


}
