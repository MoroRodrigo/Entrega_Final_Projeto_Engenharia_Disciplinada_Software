package org.example.controllers;

import org.example.exceptions.NegocioException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NegocioException.class)
    public String handleNegocioException(NegocioException ex, Model model) {
        model.addAttribute("erroMensagem", ex.getMessage());
        return "erro"; // Template de erro genérico
    }

    @ExceptionHandler(Exception.class)
    public String handleExceptionGenerica(Exception ex, Model model) {
        model.addAttribute("erroMensagem", "Ocorreu um erro inesperado no servidor.");
        return "erro";
    }
}
