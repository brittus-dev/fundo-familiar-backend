package com.brittus.domain;

import java.util.Objects;

import com.brittus.utils.Email;

public class MembroDaFamilia {

    private String nome;
    private Email email;
    private String telefone;

    @Deprecated()
    public MembroDaFamilia() {}

    private MembroDaFamilia(String nome, Email email, String telefone) {
        
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public Email getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public static MembroDaFamiliaBuilder construtor() {
        return new MembroDaFamiliaBuilder();
    }

    public static class MembroDaFamiliaBuilder {

        private String nome;
        private Email email;
        private String telefone;

        public MembroDaFamiliaBuilder comNome(String nome) {
            
            this.nome = Objects.requireNonNull(nome, "Informe o nome do novo membro");
            return this;
        }

        public MembroDaFamiliaBuilder comEmail(String email) {
            
            this.email = Email.of(email);
            return this;
        }
        
        public MembroDaFamiliaBuilder comTelefone(String telefone) {
            
            this.telefone = telefone;
            return this;
        }
        
        public MembroDaFamilia constroi() {
            
            return new MembroDaFamilia(this.nome, this.email, this.telefone);
        }
        
    }

}
