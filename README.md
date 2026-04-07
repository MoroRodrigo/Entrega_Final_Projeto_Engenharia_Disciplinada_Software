# Sistema CRUD Integrado - Engenharia de Software (Entrega Final)

![CI/CD Pipeline](https://img.shields.io/badge/CI%2FCD-Finalizado-success?style=for-the-badge&logo=githubactions)
![Java Version](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.4-brightgreen?style=for-the-badge&logo=springboot)
![Test Coverage](https://img.shields.io/badge/Coverage-95%25-blue?style=for-the-badge)

## 1. Descrição do Projeto
Este projeto representa a consolidação final da disciplina de **Engenharia Disciplinada de Software**. O sistema evoluiu para um ecossistema CRUD completo e resiliente, integrando automação de ponta a ponta, design orientado a objetos e uma esteira de CI/CD rigorosa. A aplicação gerencia produtos com regras de negócio complexas, aplicando descontos polimórficos e garantindo a integridade dos dados através de imutabilidade.

## 2. Tecnologias e Ferramentas
* **Linguagem:** Java 21 (Records, Sealed Classes)
* **Framework:** Spring Boot 3.2.4 & Thymeleaf
* **Testes de Unidade/Propriedades:** JUnit 5 & Jqwik (Fuzz Testing)
* **Automação E2E:** Selenium WebDriver (Padrão Page Object Model)
* **Esteira CI/CD:** GitHub Actions (Multi-environment)
* **Qualidade e Cobertura:** JaCoCo & Qodana (SAST)
* **Gestão de Dependências:** Maven

## 3. Arquitetura e Padrões de Projeto
O software foi estruturado seguindo princípios de **Clean Code** e **SOLID**:

* **CQRS (Command Query Responsibility Segregation):** Divisão lógica entre `ProdutoCommandService` (escrita) e `ProdutoQueryService` (leitura), otimizando a manutenção e a escalabilidade.
* **Page Object Model (POM):** Encapsulamento da lógica de interação com a interface web na classe `ProdutoPage`, desacoplando os testes da estrutura HTML.
* **Fail Early & Fail Gracefully:** Validações rigorosas no construtor dos Records e tratamento global de exceções via `@ControllerAdvice` com redirecionamento para uma página de erro humanizada (`erro.html`).
* **Imutabilidade:** Uso extensivo de Java Records para garantir que o estado do domínio seja consistente e protegido contra efeitos colaterais.

## 4. Estratégia de Testes Avançada
A cobertura de testes foi projetada para garantir resiliência em múltiplos níveis:
* **Fuzz Testing:** O Jqwik gera milhares de entradas aleatórias para validar limites financeiros e comportamentos inesperados.
* **Testes de Resiliência:** Implementação de `assertTimeoutPreemptively` para garantir que operações críticas atendam aos SLAs de performance.
* **Validação Pós-Deploy:** O pipeline executa testes Selenium em modo *headless* contra a URL de produção real, validando o CRUD funcional imediatamente após o deploy.

## 5. Pipeline de CI/CD (GitHub Actions)
O fluxo automatizado (`ci.cd.pipeline.yml`) garante a segurança e a qualidade do código:
1. **Build & Quality Gate:** Compilação com Maven e execução de testes. Se um teste falha ou a cobertura é insuficiente, o pipeline é interrompido (**Fail Fast**).
2. **SAST (Security):** Varredura estática em busca de vulnerabilidades e exposição de segredos.
3. **Deploy em Homologação:** Publicação automática para validação em ambiente de teste.
4. **Deploy em Produção:** Promoção do artefato utilizando *GitHub Environments* e proteção de segredos via *Actions Secrets*.
5. **Smoke Tests:** Testes automatizados pós-deploy com Selenium para garantir "Health Check" positivo.

## 6. Instruções de Execução

### Pré-requisitos
* Java 21+
* Maven 3.9+
* Google Chrome (para testes locais de interface)

### Como rodar a aplicação
```bash
# Clone o repositório
git clone [https://github.com/MoroRodrigo/Entrega_Final_Projeto_Engenharia_Disciplinada_Softwaret](https://github.com/MoroRodrigo/Entrega_Final_Projeto_Engenharia_Disciplinada_Software)

# Execute o projeto
mvn spring-boot:run