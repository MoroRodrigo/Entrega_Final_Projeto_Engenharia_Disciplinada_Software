package org.example.controllers;

import org.example.services.ProdutoCommandService;
import org.example.services.ProdutoQueryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoCommandService commandService;
    private final ProdutoQueryService queryService;

    public ProdutoController(ProdutoCommandService commandService, ProdutoQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("produtos", queryService.listarTodos());
        return "produtos-list"; // Nome do template Thymeleaf
    }

    @PostMapping
    public String adicionar(@RequestParam String nome, @RequestParam double precoBase, @RequestParam String categoria) {
        commandService.registrarProduto(nome, precoBase, categoria);
        return "redirect:/produtos";
    }

    @PostMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        commandService.removerProduto(id);
        return "redirect:/produtos";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
        model.addAttribute("produto", queryService.buscarPorId(id));
        return "editar-produto"; // Redireciona para o template de edição
    }

    @PostMapping("/editar/{id}")
    public String atualizar(@PathVariable Long id, @RequestParam String nome,
                            @RequestParam double precoBase, @RequestParam String categoria) {
        commandService.atualizarProduto(id, nome, precoBase, categoria);
        return "redirect:/produtos";
    }
}