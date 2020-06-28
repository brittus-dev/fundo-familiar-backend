package com.brittus.domain;

import java.util.Objects;

import com.brittus.utils.Email;

public class MembroDaFamilia {

    private String nome;
    private Email email;
    private String telefone;
    private FuncoesNaFamilia funcao;

    @Deprecated()
    public MembroDaFamilia() {}

    private MembroDaFamilia(String nome, Email email, String telefone, FuncoesNaFamilia funcao) {
        
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.funcao = funcao;
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

    public FuncoesNaFamilia getFuncao() {
        return funcao;
    }

    public static MembroDaFamiliaBuilder construtor() {
        return new MembroDaFamiliaBuilder();
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setEmail(Email email) {
        this.email = email;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    public void setFuncao(FuncoesNaFamilia funcao) {
        this.funcao = funcao;
    }

    public MembroDaFamilia nome(String nome) {
        this.nome = nome;
        return this;
    }

    public MembroDaFamilia email(Email email) {
        this.email = email;
        return this;
    }

    public MembroDaFamilia telefone(String telefone) {
        this.telefone = telefone;
        return this;
    }

    public MembroDaFamilia funcao(FuncoesNaFamilia funcao) {
        this.funcao = funcao;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof MembroDaFamilia)) {
            return false;
        }
        MembroDaFamilia membroDaFamilia = (MembroDaFamilia) o;
        return Objects.equals(nome, membroDaFamilia.nome) && Objects.equals(email, membroDaFamilia.email) && Objects.equals(telefone, membroDaFamilia.telefone) && Objects.equals(funcao, membroDaFamilia.funcao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, email, telefone, funcao);
    }

    @Override
    public String toString() {
        return "{" +
            " nome='" + getNome() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefone='" + getTelefone() + "'" +
            ", funcao='" + getFuncao() + "'" +
            "}";
    }
    

    public static class MembroDaFamiliaBuilder {

        private String nome;
        private Email email;
        private String telefone;
        private FuncoesNaFamilia funcao;

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

        public MembroDaFamiliaBuilder comFuncao(FuncoesNaFamilia funcao) {

            this.funcao = Objects.isNull(funcao) ? FuncoesNaFamilia.SEM_FUNCAO : funcao;
            return this;
        }
        
        public MembroDaFamilia constroi() {
            
            return new MembroDaFamilia(this.nome, this.email, this.telefone, this.funcao);
        }
        
    }

}
