package org.example.domain;

public enum CategoriaProduto {
    ELETRONICO {
        @Override
        public double calcularDesconto(double precoBase) {
            return precoBase * 0.10; // 10% de desconto
        }
    },
    VESTUARIO {
        @Override
        public double calcularDesconto(double precoBase) {
            return precoBase * 0.20; // 20% de desconto
        }
    },
    ALIMENTACAO {
        @Override
        public double calcularDesconto(double precoBase) {
            return 0.0; // Sem desconto
        }
    };

    public abstract double calcularDesconto(double precoBase);
}
