package com.example.vestparse.excpetion;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(UnsupportedMedia.class)
    public ResponseEntity<String> handleUnsupportedMediaType(UnsupportedMedia ex) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body("Não aceitamos outros formatos a não ser PDF");
    }

    @ExceptionHandler(ContentEmptyException.class)
    public ResponseEntity<String> handleContentEmptyException (ContentEmptyException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Dados obrigatorias não foram enviados corretamente");
    }

    @ExceptionHandler(InvalidDocumentException.class)
    public ResponseEntity<String> handleInvalidDocumentException(InvalidDocumentException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body("O PDF não atende os requsitos para ser processado");

    } @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleExceptionGenral(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body("Algo deu errado, A culpa foi do nosso lado.");
    }

}
