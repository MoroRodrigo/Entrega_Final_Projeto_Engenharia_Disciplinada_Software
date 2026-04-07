package org.example.domain;

import org.example.exceptions.NegocioException;

public record Produto(Long id, String nome, double precoBase, CategoriaProduto categoria) {

    public Produto {
        if (nome == null || nome.isBlank()) {
            throw new NegocioException("O nome do produto é obrigatório.");
        }
        if (precoBase <= 0) {
            throw new NegocioException("O preço deve ser maior que zero.");
        }
        if (categoria == null) {
            throw new NegocioException("A categoria do produto é obrigatória.");
        }
    }

    public double getPrecoFinal() {
        return precoBase - categoria.calcularDesconto(precoBase);
    }
}
