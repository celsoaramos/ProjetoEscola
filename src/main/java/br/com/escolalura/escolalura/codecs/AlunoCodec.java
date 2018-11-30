package br.com.escolalura.escolalura.codecs;

import br.com.escolalura.escolalura.models.Aluno;
import br.com.escolalura.escolalura.models.Curso;
import br.com.escolalura.escolalura.models.Habilidade;
import br.com.escolalura.escolalura.models.Nota;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.BsonReader;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

/**
 * Classe que transforma BSON em JAVA.
 * Necessário para o MONGO entender o objeto Aluno.
 * @author zroz
 */
public class AlunoCodec implements CollectibleCodec<Aluno>{

    private Codec<Document> codec;
    
    public AlunoCodec(Codec<Document> codec) {
        this.codec = codec;
    }
            
    @Override
    public Aluno generateIdIfAbsentFromDocument(Aluno aluno) {
        return documentHasId(aluno) ? aluno.criarId() : aluno;
    }

    @Override
    public boolean documentHasId(Aluno aluno) {
        return aluno.getId() == null;
    }

    @Override
    public BsonValue getDocumentId(Aluno aluno) {
        if (!documentHasId(aluno)) {
           throw new IllegalStateException("Esse document não tem ID");
        }
        
        return new BsonString(aluno.getId().toHexString());
    }

    /**
     * Pega o aluno e transforma em objeto mongo.
     * @param writer
     * @param aluno
     * @param encoder 
     */
    @Override
    public void encode(BsonWriter writer, Aluno aluno, EncoderContext encoder) {
        
        ObjectId id = aluno.getId();
        String nome = aluno.getNome();
        String dataNascimento = aluno.getDataNascimento();
        Curso curso = aluno.getCurso();
        List<Habilidade> habilidades = aluno.getHabilidades();
        List<Nota> notas = aluno.getNota();
        
        Document document = new Document();
        document.put("_id", id);
        document.put("nome", nome);
        document.put("data_nascimento", dataNascimento);
        document.put("curso", new Document("nome", curso.getNome()));
        
        // se habilidades for diferente de null
        if (habilidades != null) {
            // cria uma lista de habilidades document
            List<Document> habilidadesDocument = new ArrayList<Document>();
            
            // realiza o for na lista de habilidades para transformar da lista Java para lista Document
            for (Habilidade habilidade : habilidades) {
                habilidadesDocument.add(new Document("nome", habilidade.getNome())
                                .append("nivel", habilidade.getNivel()));
            }
            // coloca a lista em document
            document.put("habilidades", habilidadesDocument);
        }
        
        // se habilidades for diferente de null
        if (notas != null) {
            // cria uma lista de habilidades document
            List<Double> notasParaSalvar = new ArrayList<Double>();
            
            // realiza o for na lista de notas para transformar da lista Java para lista Document
            for (Nota nota : notas) {
                notasParaSalvar.add(nota.getValor());
            }
            // coloca a lista em document
            document.put("notas", notasParaSalvar);
        }
        
        codec.encode(writer, document, encoder);
        
    }

    @Override
    public Class<Aluno> getEncoderClass() {
        return Aluno.class;
    }

    @Override
    public Aluno decode(BsonReader reader, DecoderContext decoder) {
        
        Document document = codec.decode(reader, decoder);
        
        Aluno aluno = new Aluno();
        
        aluno.setId(document.getObjectId("_id"));
        aluno.setNome(document.getString("nome"));
        aluno.setDataNascimento(document.getString("data_nascimento"));
        
        Document curso = (Document) document.get("curso");
        if (curso != null) {
            String nomeCurso = curso.getString("nome");
            aluno.setCurso(new Curso(nomeCurso));
        }
       
        
        List<Double> notas = (List<Double>) document.get("notas");
        
        if (notas != null) {
            List<Nota> notasDoAluno = new ArrayList<Nota>();
            
            for(Double nota : notas) {
                notasDoAluno.add(new Nota(nota));
            }
            
            aluno.setNota(notasDoAluno);
        }
        
        List<Document> habilidades = (List<Document>) document.get("habilidades");
        
        if (habilidades != null) {
            List<Habilidade> habilidadesDoAluno = new ArrayList<Habilidade>();
            
            for(Document documentHabilidade : habilidades) {
                habilidadesDoAluno.add(new Habilidade(documentHabilidade.getString("nome"), documentHabilidade.getString("nivel")));
            }
            
            aluno.setHabilidades(habilidadesDoAluno);
        }
        
        
        return aluno;
    }
    
}
