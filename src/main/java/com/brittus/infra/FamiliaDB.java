package com.brittus.infra;

import java.util.ArrayList;
import java.util.List;

import com.brittus.domain.Familia;
import com.brittus.domain.MembroDaFamilia;

import org.bson.types.ObjectId;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;

@MongoEntity(collection = "familias")
public class FamiliaDB extends PanacheMongoEntity {

    public ObjectId id;
    public String nome;
    public List<MembroDaFamiliaDB> membros = new ArrayList<>();

    public static class MembroDaFamiliaDB {
        public String nome;
        public String email;
        public String telefone;
    }

    public FamiliaDB adicionaMembro(MembroDaFamiliaDB membroDaFamiliaDB) {

        membros.add(membroDaFamiliaDB);
        return this;
    }

    public FamiliaDB constroi() {
        Familia familia = Familia.dos(nome);
        
        membros.stream().forEach(membro -> {
            MembroDaFamilia membroDaFamilia = MembroDaFamilia.construtor()
                .comEmail(membro.email)
                .comNome(membro.nome)
                .comTelefone(membro.telefone)
                .constroi();
            familia.adiciona(membroDaFamilia);
        });

        return this;
    }
    
}