package org.example.unit;

import org.example.domain.CategoriaProduto;
import org.example.domain.Produto;
import org.example.exceptions.NegocioException;
import org.example.repositories.ProdutoRepository;
import org.example.services.ProdutoQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoQueryServiceTest {

    private ProdutoRepository repository;
    private ProdutoQueryService queryService;

    @BeforeEach
    void setUp() {
        repository = new ProdutoRepository();
        queryService = new ProdutoQueryService(repository);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
        assertThrows(NegocioException.class, () -> queryService.buscarPorId(99L));
    }

    @Test
    void deveRetornarProdutoQuandoEncontrado() {
        Produto p = repository.salvar(new Produto(null, "Notebook", 5000.0, CategoriaProduto.ELETRONICO));
        Produto encontrado = queryService.buscarPorId(p.id());
        assertNotNull(encontrado);
        assertEquals("Notebook", encontrado.nome());
    }
}
