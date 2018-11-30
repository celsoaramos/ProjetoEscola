package br.com.escolalura.escolalura.controllers;

import br.com.escolalura.escolalura.models.Aluno;
import br.com.escolalura.escolalura.models.Habilidade;
import br.com.escolalura.escolalura.repositories.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Classe de Controller para Habilidade.
 * Responsável por pegar a rota clicada (/aluno/cadastrar por exemplo) e fazer o redirect.
 * @author zroz
 */
@Controller
public class HabilidadeController {
    
    @Autowired
    private AlunoRepository repository;
    
    @GetMapping("habilidade/cadastrar/{id}")
    public String cadastrar(@PathVariable String id, Model model) {
        
        // chama o método para recuperar o Aluno passando o ID.
        Aluno aluno = repository.obterAlunoPorId(id);
        
        // adiciona os atributos no model
        model.addAttribute("aluno", aluno);
        model.addAttribute("habilidade", new Habilidade());
        
        // retorna para a tela
        return "habilidade/cadastrar";
    }
    
    /**
     * Método responsavel por salvar um aluno.
     * @param id - ID enviado na URL.
     * @param habilidade - campos de habilidade da tela.
     * @return para a tela de listar.
     */
    @PostMapping("/habilidade/salvar/{id}")
    public String salvar(@PathVariable String id, @ModelAttribute Habilidade habilidade) {
        
        // chama o método para recuperar o Aluno passando o ID.
        Aluno aluno = repository.obterAlunoPorId(id);
        
        // adiciona habilidade ao aluno
        aluno.getHabilidades().add(habilidade);
        
        repository.salvar(aluno);
        
        return "redirect:/aluno/listar";
    }
    
}
