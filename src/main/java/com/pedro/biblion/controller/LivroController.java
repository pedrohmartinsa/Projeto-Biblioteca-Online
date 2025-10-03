package com.pedro.biblion.controller;

import com.pedro.biblion.model.Livro;
import com.pedro.biblion.repository.LivroRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/livros")
public class LivroController {

    @Autowired
    private LivroRepository livroRepository;

    @GetMapping
    public String listarTodos(Model model) {
        model.addAttribute("livros", livroRepository.findAll());
        return "livros/lista";
    }

    @GetMapping("/novo")
    public String novoLivroForm(Model model) {
        model.addAttribute("livro", new Livro());
        return "livros/form";
    }

    @PostMapping("/salvar")
    public String salvarLivro(@Valid @ModelAttribute("livro") Livro livro, BindingResult result) {
        if (result.hasErrors()) {
            return "livros/form";
        }
        livroRepository.save(livro);
        return "redirect:/livros";
    }

    @GetMapping("/editar/{id}")
    public String editarLivroForm(@PathVariable Long id, Model model) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID do Livro inválido:" + id));
        model.addAttribute("livro", livro);
        return "livros/form";
    }

    @GetMapping("/excluir/{id}")
    public String excluirLivro(@PathVariable Long id) {
        livroRepository.deleteById(id);
        return "redirect:/livros";
    }
}