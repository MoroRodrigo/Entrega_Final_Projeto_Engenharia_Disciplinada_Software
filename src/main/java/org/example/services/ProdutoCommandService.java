package org.example.services;

import org.example.domain.CategoriaProduto;
import org.example.domain.Produto;
import org.example.repositories.ProdutoRepository;
import org.springframework.stereotype.Service;

@Service
public class ProdutoCommandService {
    private final ProdutoRepository repository;

    public ProdutoCommandService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public void registrarProduto(String nome, double precoBase, String categoria) {
        Produto novo = new Produto(null, nome, precoBase, CategoriaProduto.valueOf(categoria.toUpperCase()));
        repository.salvar(novo);
    }

    public void atualizarProduto(Long id, String nome, double precoBase, String categoria) {
        Produto atualizado = new Produto(id, nome, precoBase, CategoriaProduto.valueOf(categoria.toUpperCase()));
        repository.atualizar(atualizado);
    }

    public void removerProduto(Long id) {
        repository.deletar(id);
    }
}