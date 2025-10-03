package com.pedro.biblion.controller;

import com.pedro.biblion.model.Emprestimo;
import com.pedro.biblion.model.Livro;
import com.pedro.biblion.model.StatusLivro;
import com.pedro.biblion.repository.EmprestimoRepository;
import com.pedro.biblion.repository.LivroRepository;
import com.pedro.biblion.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/emprestimos")
public class EmprestimoController {

    @Autowired
    private EmprestimoRepository emprestimoRepository;
    @Autowired
    private LivroRepository livroRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public String listarAtivos(Model model) {
        model.addAttribute("emprestimos", emprestimoRepository.findByAtivo(true));
        return "emprestimos/lista-ativos";
    }

    @GetMapping("/novo")
    public String novoEmprestimoForm(Model model) {
        model.addAttribute("emprestimo", new Emprestimo());
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("livrosDisponiveis", livroRepository.findByStatus(StatusLivro.DISPONIVEL));
        return "emprestimos/form";
    }

    @PostMapping("/salvar")
    @Transactional
    public String salvarEmprestimo(@ModelAttribute Emprestimo emprestimo, BindingResult result, RedirectAttributes attributes) {
        if (emprestimo.getDataPrevistaDevolucao() != null && emprestimo.getDataPrevistaDevolucao().isBefore(emprestimo.getDataEmprestimo())) {
            result.rejectValue("dataPrevistaDevolucao", "error.emprestimo", "Data de devolução deve ser posterior à data de retirada.");
        }

        if (result.hasErrors()) {
            return "emprestimos/form";
        }

        Livro livro = emprestimo.getLivro();
        livro.setStatus(StatusLivro.EMPRESTADO);
        livroRepository.save(livro);

        emprestimoRepository.save(emprestimo);
        attributes.addFlashAttribute("mensagem", "Empréstimo registrado com sucesso!");
        return "redirect:/emprestimos";
    }

    @PostMapping("/devolver/{id}")
    @Transactional
    public String devolverLivro(@PathVariable Long id, RedirectAttributes attributes) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de Empréstimo inválido:" + id));

        emprestimo.setAtivo(false);
        Livro livro = emprestimo.getLivro();
        livro.setStatus(StatusLivro.DISPONIVEL);

        livroRepository.save(livro);
        emprestimoRepository.save(emprestimo);

        attributes.addFlashAttribute("mensagem", "Livro devolvido com sucesso!");
        return "redirect:/livros";
    }
}