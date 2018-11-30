package br.com.escolalura.escolalura.controllers;

import br.com.escolalura.escolalura.models.Aluno;
import br.com.escolalura.escolalura.repositories.AlunoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Classe de controller do aluno.
 * Responsável por pegar a rota clicada (/aluno/cadastrar por exemplo) e fazer o redirect.
 * @author zroz
 */
@Controller
public class AlunoController {
    
    @Autowired
    private AlunoRepository repository;
    
    /**
     * Mètodo para navegar para tela de cadastrar aluno
     * @return String - página de cadastrar
     */
    @GetMapping("/aluno/cadastrar")
    public String cadastrar(Model model) {
        
        // manda para a página de cadastrar um novo Aluno
        model.addAttribute("aluno", new Aluno());
        
        return "aluno/cadastrar";
    }
    
    @PostMapping("/aluno/salvar")
    public String salvar(@ModelAttribute Aluno aluno){
        
        repository.salvar(aluno);
        
        return "redirect:/";
    }
    
    @GetMapping("/aluno/listar")
    public String listar(Model model) {
        
        List<Aluno> alunos = repository.obterTodosAlunos();
        
        model.addAttribute("alunos", alunos);
        return "/aluno/listar";
    }
    
    @GetMapping("/aluno/visualizar/{id}")
    public String visualizar(@PathVariable String id, Model model) {
        
          Aluno aluno = repository.obterAlunoPorId(id);
          model.addAttribute("aluno", aluno);
        
        return "aluno/visualizar";
    }
    
    /**
     * Joga para a tela de pesquisar por nome.
     * @return tela de pesquisar por nome.
     */
    @GetMapping("/aluno/pesquisarnome")
    public String pesquisarNome() {
        return "aluno/pesquisarnome";
    }
    
    /**
     * Método que efetua a pesquisa por nome.
     * @param nome a ser pesquisado no banco.
     * @param model para jogar para a tela com os dados populados
     * @return lista de nomes
     */
    @GetMapping("/aluno/pesquisar")
    public String pesquisar(@RequestParam("nome") String nome, Model model) {
        
        List<Aluno> alunos = repository.pesquisarPor(nome);
        
        model.addAttribute("alunos", alunos);
       
        return "aluno/pesquisarnome";
    }
    
}
