package org.example.services;

import org.example.domain.Produto;
import org.example.exceptions.NegocioException;
import org.example.repositories.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProdutoQueryService {
    private final ProdutoRepository repository;

    public ProdutoQueryService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public List<Produto> listarTodos() {
        return repository.listarTodos();
    }

    public Produto buscarPorId(Long id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new NegocioException("Produto não encontrado com ID: " + id));
    }
}