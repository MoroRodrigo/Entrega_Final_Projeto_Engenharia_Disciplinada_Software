package org.example.repositories;

import org.example.domain.Produto;
import org.example.exceptions.NegocioException;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProdutoRepository {
    private final List<Produto> produtos = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Produto salvar(Produto produto) {
        Produto novoProduto = new Produto(idGenerator.getAndIncrement(), produto.nome(), produto.precoBase(), produto.categoria());
        produtos.add(novoProduto);
        return novoProduto;
    }

    public void atualizar(Produto produto) {
        int index = -1;
        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).id().equals(produto.id())) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            produtos.set(index, produto);
        } else {
            throw new NegocioException("Não é possível atualizar: Produto inexistente.");
        }
    }

    public List<Produto> listarTodos() {
        return new ArrayList<>(produtos);
    }

    public Optional<Produto> buscarPorId(Long id) {
        return produtos.stream().filter(p -> p.id().equals(id)).findFirst();
    }

    public void deletar(Long id) {
        produtos.removeIf(p -> p.id().equals(id));
    }
}