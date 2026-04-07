package org.example.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.e2e.pages.ProdutoPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Teste de fumaça (Smoke Test) para validação pós-deploy.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SeleniumPostDeployTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private ProdutoPage produtoPage;

    @BeforeEach
    void setUp() {
        // Silencia logs desnecessários do Selenium no console do CI
        Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);

        // Configuração automática do Driver
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        // CONFIGURAÇÕES CRÍTICAS PARA GITHUB ACTIONS (LINUX SEM INTERFACE GRÁFICA)
        options.addArguments("--headless=new"); // Modo sem janela (obrigatório no CI)
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox"); // Necessário para rodar como root/container
        options.addArguments("--disable-dev-shm-usage"); // Evita falhas de memória compartilhada no Docker
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--window-size=1920,1080"); // Define um tamanho de tela padrão

        driver = new ChromeDriver(options);

        // Configura Timeouts globais
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        // Inicialização do Page Object
        produtoPage = new ProdutoPage(driver);
    }

    @Test
    void testarFluxoCompletoDeCriacaoDeProdutoEmProducao() {
        // Prioriza a URL de produção do ambiente; caso contrário, usa o localhost do Spring
        String baseUrl = System.getenv().getOrDefault("PROD_URL", "http://localhost:" + port);

        // Garante que não haverá barras duplas na URL
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        String finalUrl = baseUrl + "/produtos";

        System.out.println("--------------------------------------------------");
        System.out.println("EXECUTANDO TESTE E2E NO ENDEREÇO: " + finalUrl);
        System.out.println("--------------------------------------------------");

        try {
            driver.get(finalUrl);

            // Interação utilizando o Page Object Model (POM)
            produtoPage.preencherNome("Teclado Mecânico Custom CI");
            produtoPage.preencherPreco("450.00");
            produtoPage.selecionarCategoria("ELETRONICO");
            produtoPage.clicarAdicionar();

            // Validação com mensagem de erro customizada para o log do Maven
            boolean existe = produtoPage.existeProdutoNaLista("Teclado Mecânico Custom CI");

            assertTrue(existe, "FALHA CRÍTICA: O produto não foi encontrado na tabela após o salvamento em: " + finalUrl);

            System.out.println("SUCESSO: Produto cadastrado e visualizado corretamente.");

        } catch (Exception e) {
            System.err.println("ERRO DURANTE EXECUÇÃO DO SELENIUM: " + e.getMessage());
            throw e; // Lança novamente para o JUnit marcar o teste como falho
        }
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            System.out.println("Encerrando WebDriver...");
            driver.quit();
        }
    }
}