package org.example.unit;

import org.example.domain.CategoriaProduto;
import org.example.domain.Produto;
import org.example.repositories.ProdutoRepository;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResilienciaTest {

    @Test
    void sistemaDeveResponderEmTempoHabilMesmoComCargaSimulada() {
        ProdutoRepository repository = new ProdutoRepository();

        // Popula o repositório com uma carga inicial
        for (int i = 0; i < 1000; i++) {
            repository.salvar(new Produto(null, "Produto " + i, 10.0, CategoriaProduto.ALIMENTACAO));
        }

        // Valida se a busca de todos os elementos ocorre em menos de 50 milissegundos (Timeout/Resiliência)
        assertTimeoutPreemptively(Duration.ofMillis(50), () -> {
            var lista = repository.listarTodos();
            assertTrue(lista.size() >= 1000, "A lista deve conter os itens inseridos.");
        }, "A operação de leitura demorou mais que o esperado (Timeout excedido). O sistema falhou em atender ao SLA de performance.");
    }
}