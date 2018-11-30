package br.com.escolalura.escolalura.repositories;

import br.com.escolalura.escolalura.codecs.AlunoCodec;
import br.com.escolalura.escolalura.models.Aluno;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

/**
 *
 * Classe que faz o acesso ao banco MONGO.
 * 
 * @author zroz
 */
@Repository
public class AlunoRepository {
    
    // instância do client do Mongo informando a URL do banco e as opcoes com o codec
    private MongoClient cliente;
    
    // recupera o banco de dados 
    // alunoBD já estava criado no banco mongo 
    private MongoDatabase bancoDeDados;
    
    
    private void criarConexao() {
        // cria o codec do Mongo.
        Codec<Document> codec = MongoClient.getDefaultCodecRegistry().get(Document.class);
        
        // instância de classe Codec com o codec do Mongo.
        AlunoCodec alunoCodec = new AlunoCodec(codec);
        
        // Registra o alunoCodec
        CodecRegistry registro = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(), CodecRegistries.fromCodecs(alunoCodec));
        
        // opcoes do Mongo
        MongoClientOptions opcoes = MongoClientOptions.builder().codecRegistry(registro).build();
        
        // instância do client do Mongo informando a URL do banco e as opcoes com o codec.
        this.cliente = new MongoClient("localhost:27017", opcoes);
        
        // recupera o banco de dados 
        this.bancoDeDados = cliente.getDatabase("bancoProjetoEscola");
        
    }
    
    /**
     * Método para salvar aluno no banco MONGO.
     * @param aluno - aluno a ser inserido
     */
    public void salvar(Aluno aluno) {
        
        // chama o método de criar conexão
        criarConexao();
        
        // recupera a collection de "alunos" e passa como segundo parâmetro o class de Aluno
        // collection alunos já estava criada no banco mongo
        MongoCollection<Aluno> alunoCollection = this.bancoDeDados.getCollection("alunos", Aluno.class);
        
        // se não tiver ID, adiciona
        if (aluno.getId() == null) {
        
            // chama o método de inserir no banco
            alunoCollection.insertOne(aluno);
            
        // senão faz o update
        } else {
            alunoCollection.updateOne(Filters.eq("_id", aluno.getId()), new Document("$set", aluno));
        }
        
        // fecha a conexão
        fecharConexao();
       
    }
    
    /**
     * Método para listar todos alunos no banco MONGO.
     * @param aluno - aluno a ser inserido
     */
    public List<Aluno> obterTodosAlunos(){
        
        // chama o método de criar conexão
        criarConexao();
        
        // recupera a collection de "alunos" e passa como segundo parâmetro o class de Aluno
        // collection alunos já estava criada no banco mongo
        MongoCollection<Aluno> alunoCollection = this.bancoDeDados.getCollection("alunos", Aluno.class);
        
        // recupera a lista de alunos 
        MongoCursor<Aluno> resultados = alunoCollection.find().iterator();
        
        // transforma de MongoCursor para List
        List<Aluno> alunos = popularAlunos(resultados);
        
        // fecha a conexão
        fecharConexao();
        
        return alunos;
    }
    
    /**
     * Método para recuperar aluno por ID.
     */
    public Aluno obterAlunoPorId(String id) {
        
        // chama o método de criar conexão
        criarConexao();
        
        // recupera a collection de "alunos" e passa como segundo parâmetro o class de Aluno
        // collection alunos já estava criada no banco mongo
        MongoCollection<Aluno> alunoCollection = this.bancoDeDados.getCollection("alunos", Aluno.class);
        
        // recupera um aluno usando o método find e passando o ID
        Aluno aluno = alunoCollection.find(Filters.eq("_id", new ObjectId(id))).first();
        
        // fecha a conexão
        fecharConexao();
        
        return aluno;
    }

    /**
     * Pesquisa por nome.
     * @param nome a ser pesquisado no banco.
     * @return lista de alunos com o nome buscado
     */
    public List<Aluno> pesquisarPor(String nome) {
        criarConexao();
        
        // recupera a collection de "alunos" e passa como segundo parâmetro o class de Aluno
        // collection alunos já estava criada no banco mongo
        MongoCollection<Aluno> alunoCollection = this.bancoDeDados.getCollection("alunos", Aluno.class);
        
        // pesquisa no banco passando o nome.
        MongoCursor<Aluno> resultados = alunoCollection.find(Filters.eq("nome", nome), Aluno.class).iterator();
        
        // transforma de MongoCursor para List
        List<Aluno> alunos = popularAlunos(resultados);
        
        // fecha a conexão
        fecharConexao();
        
        return alunos;
    }
    
    /**
     * Transforma de MongoCursor<Aluno> para List<Aluno>
     * @param resultados
     * @return 
     */
    private List<Aluno> popularAlunos(MongoCursor<Aluno> resultados) {
        // lista a ser retornada
        List<Aluno> alunos = new ArrayList<Aluno>();
        
        // transforma de MongoCursor<Aluno> para List<Aluno>
        while(resultados.hasNext()) {
            alunos.add(resultados.next());
        }
        
        return alunos;
    }
    
    public List<Aluno> pesquisarPor(String classificacao, double nota) {
        
        criarConexao();
        MongoCollection<Aluno> alunoCollection = this.bancoDeDados.getCollection("alunos", Aluno.class);
        MongoCursor<Aluno> resultados = null;

        if(classificacao.equals("reprovados")) {
          resultados = alunoCollection.find(Filters.lt("notas", nota)).iterator();
        }else if(classificacao.equals("aprovados")) {
          resultados = alunoCollection.find(Filters.gte("notas", nota)).iterator();
        }

        List<Aluno> alunos = popularAlunos(resultados);

        fecharConexao();

        return alunos;

    }
    
    /**
     * Método de fechar conexão.
     */
    public void fecharConexao() {
        // fecha a conexão
        this.cliente.close();
    }
}
