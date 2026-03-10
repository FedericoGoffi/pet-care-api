package com.saltoagro.pet_care_api.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.saltoagro.pet_care_api.dto.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(MascotaNoEncontradaException.class)
        public ResponseEntity<ErrorResponse> manejarMascotaNoEncontrada(
                        MascotaNoEncontradaException ex) {

                ErrorResponse error = new ErrorResponse(
                                HttpStatus.NOT_FOUND.value(),
                                ex.getMessage());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        @ExceptionHandler(VacunaVigenteException.class)
        public ResponseEntity<ErrorResponse> manejarVacunaVigente(
                        VacunaVigenteException ex) {

                ErrorResponse error = new ErrorResponse(
                                HttpStatus.CONFLICT.value(),
                                ex.getMessage());

                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        @ExceptionHandler(VacunaNoEncontradaException.class)
        public ResponseEntity<ErrorResponse> manejarVacunaNoEncontrada(
                        VacunaNoEncontradaException ex) {

                ErrorResponse error = new ErrorResponse(
                                HttpStatus.NOT_FOUND.value(),
                                ex.getMessage());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> manejarValidaciones(
                        MethodArgumentNotValidException ex) {

                String mensaje = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                                .collect(Collectors.joining(", "));

                ErrorResponse error = new ErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                mensaje);

                return ResponseEntity.badRequest().body(error);
        }
}