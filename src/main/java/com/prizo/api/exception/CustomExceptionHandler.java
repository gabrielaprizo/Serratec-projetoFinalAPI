package com.prizo.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    protected ResponseEntity<CustomException> handleProdutoNaoEncontradoException() {
        return new ResponseEntity<CustomException>(
            new CustomException("produto.nao.encontrado"),
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(ProdutoDeletadoException.class)
    protected ResponseEntity<CustomException> handleProdutoDeletadoException() {
        return new ResponseEntity<CustomException>(
            new CustomException("produto.indisponivel"),
            HttpStatus.NOT_FOUND
        );
    }
}
