package com.veiculosmg.exception.handler;

import com.veiculosmg.exception.PropriedadeJaCadastradaException;
import com.veiculosmg.exception.RecursoNaoEncontradoException;
import com.veiculosmg.exception.ResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class HandlerExceptionCustomizada extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ResponseException> handleRecursoNaoEncontradoException(RecursoNaoEncontradoException ex) {
        ResponseException responseException = new ResponseException(ex.getMessage());

        return new ResponseEntity<>(responseException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PropriedadeJaCadastradaException.class)
    public ResponseEntity<ResponseException> handlePropriedadeJaCadastrada(PropriedadeJaCadastradaException ex) {
        ResponseException responseException = new ResponseException(ex.getMessage());

        return new ResponseEntity<>(responseException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
