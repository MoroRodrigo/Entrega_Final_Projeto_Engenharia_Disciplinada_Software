package org.example.property;

import net.jqwik.api.*;
import org.example.domain.CategoriaProduto;
import org.example.domain.Produto;
import org.example.exceptions.NegocioException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProdutoPropertyTest {

    @Property
    void produtosComPrecoNegativoOuZeroDevemFalhar(@ForAll("precosInvalidos") double precoInvalido) {
        assertThrows(NegocioException.class, () ->
                new Produto(1L, "Teste Fuzz", precoInvalido, CategoriaProduto.VESTUARIO)
        );
    }

    @Property
    void precoFinalDeveSerSempreMenorOuIgualAoPrecoBaseParaEletronicos(@ForAll("precosValidos") double precoBase) {
        Produto p = new Produto(1L, "Mouse", precoBase, CategoriaProduto.ELETRONICO);
        assertTrue(p.getPrecoFinal() <= precoBase);
    }

    @Provide
    Arbitrary<Double> precosInvalidos() {
        return Arbitraries.doubles().lessOrEqual(0.0);
    }

    @Provide
    Arbitrary<Double> precosValidos() {
        return Arbitraries.doubles().between(0.1, 100000.0);
    }
}