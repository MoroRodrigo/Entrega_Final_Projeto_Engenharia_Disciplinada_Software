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

        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);

        // Configura Timeouts globais
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        // Inicialização do Page Object
        produtoPage = new ProdutoPage(driver);
    }

    @Test
    void testarFluxoCompletoDeCriacaoDeProdutoEmProducao() {
        String baseUrl = System.getenv("PROD_URL");

        // Se a variável estiver vazia no GitHub, usamos o local como fallback
        if (baseUrl == null || baseUrl.isEmpty() || baseUrl.isBlank()) {
            baseUrl = "http://localhost:" + port;
            System.out.println("AVISO: PROD_URL não encontrada. Usando fallback local: " + baseUrl);
        }

        // Garante que a URL comece com http (evita o InvalidArgumentException)
        if (!baseUrl.startsWith("http")) {
            baseUrl = "http://" + baseUrl;
        }

        // Limpa barras duplicadas
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        String finalUrl = baseUrl + "/produtos";

        System.out.println("URL FINAL PARA TESTE: " + finalUrl);

        try {
            driver.get(finalUrl); // Se a URL não for válida, o erro ocorre aqui

            produtoPage.preencherNome("Teclado Teste CI");
            produtoPage.preencherPreco("100.00");
            produtoPage.selecionarCategoria("ELETRONICO");
            produtoPage.clicarAdicionar();

            assertTrue(produtoPage.existeProdutoNaLista("Teclado Teste CI"),
                    "Produto não encontrado na URL: " + finalUrl);

        } catch (Exception e) {
            System.err.println("FALHA AO ACESSAR A URL: " + finalUrl);
            throw e;
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