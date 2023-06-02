package com.veiculosmg.exception.handler;

import com.veiculosmg.exception.AtributoDuplicadoException;
import com.veiculosmg.exception.AtributoInvalidoException;
import com.veiculosmg.exception.RecursoNaoEncontradoException;
import com.veiculosmg.exception.ResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandlerExceptionCustomizada {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseException> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        ResponseException responseException = new ResponseException(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                "Method Not Allowed",
                "Métodos permitidos para esse recurso: " + ex.getSupportedHttpMethods().toString()
        );

        return new ResponseEntity<>(responseException, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseException> handleValidationException(MethodArgumentNotValidException ex) {
        String mensagemErro = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erroCampo -> erroCampo.getDefaultMessage())
                .reduce((mensagem1, mensagem2) -> mensagem1 + ", " + mensagem2)
                .orElse("Erro de validação");

        ResponseException responseException = new ResponseException(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                mensagemErro);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseException);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ResponseException> handleRecursoNaoEncontradoException(RecursoNaoEncontradoException ex) {
        ResponseException responseException = new ResponseException(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage());

        return new ResponseEntity<>(responseException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AtributoDuplicadoException.class)
    public ResponseEntity<ResponseException> handlePropriedadeJaCadastrada(AtributoDuplicadoException ex) {
        ResponseException responseException = new ResponseException(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage());

        return new ResponseEntity<>(responseException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AtributoInvalidoException.class)
    public ResponseEntity<ResponseException> handleAtributoInvalido(AtributoInvalidoException ex) {
        ResponseException responseException = new ResponseException(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage());

        return new ResponseEntity<>(responseException, HttpStatus.BAD_REQUEST);
    }

}
