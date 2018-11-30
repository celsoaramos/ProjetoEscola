package br.com.escolalura.escolalura.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Classe com os atributos de Aluno TO
 * @author zroz
 */
public class Aluno {
    
    private ObjectId id;
    private String nome;
    private String dataNascimento;
    private Curso curso;
    private List<Nota> nota;
    private List<Habilidade> habilidades;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public List<Nota> getNota() {
        if(nota == null) {
            nota = new ArrayList<Nota>();
        }
        return nota;
    }

    public void setNota(List<Nota> nota) {
        this.nota = nota;
    }

    public List<Habilidade> getHabilidades() {
        if (habilidades == null) {
            habilidades = new ArrayList<Habilidade>();
        }
        return habilidades;
    }

    public void setHabilidades(List<Habilidade> habilidades) {
        this.habilidades = habilidades;
    }
    
    /**
     * cria um ID em branco.
     */
    public Aluno criarId() {
        setId(new ObjectId());
        return this;
    }

}