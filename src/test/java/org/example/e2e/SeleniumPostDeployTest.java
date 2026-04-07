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

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SeleniumPostDeployTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private ProdutoPage produtoPage;

    @BeforeEach
    void setUp() {
        Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        produtoPage = new ProdutoPage(driver); // Inicialização do POM
    }

    @Test
    void testarFluxoCompletoDeCriacaoDeProdutoEmProducao() {
        String baseUrl = System.getenv().getOrDefault("PROD_URL", "http://localhost:" + port);
        String finalUrl = baseUrl + "/produtos";

        System.out.println("Acessando ambiente para teste E2E: " + finalUrl);
        driver.get(finalUrl);

        // Interação utilizando o Page Object Model
        produtoPage.preencherNome("Teclado Mecânico Custom");
        produtoPage.preencherPreco("450.00");
        produtoPage.selecionarCategoria("ELETRONICO");
        produtoPage.clicarAdicionar();

        // Validação (Fail Gracefully embutido no assert)
        assertTrue(produtoPage.existeProdutoNaLista("Teclado Mecânico Custom"),
                "Erro: O produto adicionado não foi renderizado na lista final.");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}