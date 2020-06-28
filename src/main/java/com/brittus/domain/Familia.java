package com.brittus.domain;

import java.util.ArrayList;
import java.util.List;

public class Familia {
    
    private String nome;
    private List<MembroDaFamilia> membrosDaFamilia = new ArrayList<>();

    @Deprecated
    public Familia(){}

    private Familia(String nomeDaFamilia) {
        this.nome = nomeDaFamilia;
    }

    public String getNome() {
        return nome;
    }

    public boolean possuiMembro(MembroDaFamilia membroDaFamilia) {
        return this.membrosDaFamilia.contains(membroDaFamilia);
    }

    public long totalMembros() {
        return this.membrosDaFamilia.size();
    }

    public void adiciona(MembroDaFamilia membroDaFamilia) {

        if (membrosDaFamilia.contains(membroDaFamilia)) {
            throw new IllegalArgumentException("O membro informado já pertence a essa família");
        }

        this.membrosDaFamilia.add(membroDaFamilia);
    }

    public static Familia dos(String nome) {
        return new Familia(nome);
    }

}