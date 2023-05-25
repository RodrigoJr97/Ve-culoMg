package com.veiculosmg.exception;

import java.time.LocalDateTime;

public class ResponseException {

    private LocalDateTime timestamp;
    private String mensagem;

    public ResponseException(String mensagem) {
        this.timestamp = LocalDateTime.now();
        this.mensagem = mensagem;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

}
