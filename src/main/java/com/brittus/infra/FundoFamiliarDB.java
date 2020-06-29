package com.brittus.infra;

import javax.money.MonetaryAmount;

import com.brittus.infra.FamiliaDB.MembroDaFamiliaDB;

import org.bson.types.ObjectId;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;

@MongoEntity(collection = "fundos")
public class FundoFamiliarDB extends PanacheMongoEntity {
    
    public ObjectId id;
    public String nome;
    public MonetaryAmount saldo;
    public String idFamilia;
    public MembroDaFamiliaDB operadorDoFundo;

    /*public FundoFamiliarDB constroi() {

        FundoFamiliar.construtor()
            .comNome(nome)
            .comSaldoInicial(Money.of(100, reais))
            .pertenceAFamilia(familiaBrittus)
            .operadoPor(brunoBrittus)
            .constroi();

        return this;
    }*/

}