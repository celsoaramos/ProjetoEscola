package br.com.escolalura.escolalura.controllers;

import br.com.escolalura.escolalura.models.Aluno;
import br.com.escolalura.escolalura.models.Habilidade;
import br.com.escolalura.escolalura.models.Nota;
import br.com.escolalura.escolalura.repositories.AlunoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author zroz
 */
@Controller
public class NotaController {
    
    @Autowired
    private AlunoRepository repository;
        
    @GetMapping("/nota/cadastrar/{id}")
    public String cadastrar(@PathVariable String id, Model model) {
        
        Aluno aluno = repository.obterAlunoPorId(id);
        model.addAttribute("aluno", aluno);
        model.addAttribute("nota", new Nota());
        
        return "nota/cadastrar";
    }
    
     /**
     * Método responsavel por salvar um aluno.
     * @param id - ID enviado na URL.
     * @param habilidade - campos de habilidade da tela.
     * @return para a tela de listar.
     */
    @PostMapping("/nota/salvar/{id}")
    public String salvar(@PathVariable String id, @ModelAttribute Nota nota) {
        
        // chama o método para recuperar o Aluno passando o ID.
        Aluno aluno = repository.obterAlunoPorId(id);
        
        // adiciona habilidade ao aluno
        aluno.getNota().add(nota);
        
        repository.salvar(aluno);
        
        return "redirect:/aluno/listar";
    }
    
    @GetMapping("/nota/iniciarpesquisa")
    public String iniciarPesquisa() {
      return "nota/pesquisar";
    }
    
    @GetMapping("/nota/pesquisar")
    public String pesquisarPor(@RequestParam("classificacao") String classificacao, @RequestParam("notacorte") String notaCorte, Model model) {
      List<Aluno> alunos = repository.pesquisarPor(classificacao, Double.parseDouble(notaCorte));
      model.addAttribute("alunos", alunos);
      return "nota/pesquisar";
    }
    
}
