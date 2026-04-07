package org.example.e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ProdutoPage {
    private WebDriver driver;

    public ProdutoPage(WebDriver driver) {
        this.driver = driver;
    }

    public void preencherNome(String nome) {
        driver.findElement(By.id("input-nome")).sendKeys(nome);
    }

    public void preencherPreco(String preco) {
        driver.findElement(By.id("input-preco")).sendKeys(preco);
    }

    public void selecionarCategoria(String categoria) {
        driver.findElement(By.id("select-categoria")).sendKeys(categoria);
    }

    public void clicarAdicionar() {
        driver.findElement(By.id("btn-salvar")).click();
    }

    public boolean existeProdutoNaLista(String nome) {
        return driver.findElement(By.id("tabela-produtos")).getText().contains(nome);
    }
}