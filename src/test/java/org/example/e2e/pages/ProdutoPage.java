package org.example.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProdutoPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public ProdutoPage(WebDriver driver) {
        this.driver = driver;
        // Configura uma espera explícita de até 10 segundos
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void preencherNome(String nome) {
        WebElement campoNome = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-nome")));
        campoNome.clear();
        campoNome.sendKeys(nome);
    }

    public void preencherPreco(String preco) {
        WebElement campoPreco = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-preco")));
        campoPreco.clear();
        campoPreco.sendKeys(preco);
    }

    public void selecionarCategoria(String categoria) {
        WebElement selectCategoria = wait.until(ExpectedConditions.elementToBeClickable(By.id("select-categoria")));
        selectCategoria.sendKeys(categoria);
    }

    public void clicarAdicionar() {
        WebElement botaoSalvar = wait.until(ExpectedConditions.elementToBeClickable(By.id("btn-salvar")));
        botaoSalvar.click();
    }

    /**
     * Verifica se o produto aparece na tabela.
     * Utiliza uma espera para garantir que o DOM foi atualizado após o clique no botão.
     */
    public boolean existeProdutoNaLista(String nome) {
        try {
            // Aguarda até que a tabela esteja visível
            WebElement tabela = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tabela-produtos")));

            // Dá um pequeno fôlego para o render do Thymeleaf processar o novo item
            // (Útil em ambientes de CI/CD mais lentos)
            return wait.until(d -> tabela.getText().contains(nome));
        } catch (Exception e) {
            System.err.println("Erro ao localizar produto na lista: " + e.getMessage());
            return false;
        }
    }
}