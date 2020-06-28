package com.brittus.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money; 

public class FundoFamiliar {
    
    private String nome;
    private MonetaryAmount saldo;
    private Familia familia;

    private List<TransacaoFundo> transacoes = new ArrayList<>();

    private static final String TRANSACAO_INVALIDA = "Transação inválida";

    @Deprecated
    public FundoFamiliar() {}

    private FundoFamiliar(MonetaryAmount saldoInicial, Familia familia, String nomeDoFundo) {

        this.saldo = saldoInicial;
        this.familia = familia;
        this.nome = nomeDoFundo;
    }

    public TransacaoFundo registrarTransacao(TransacaoFundo transacao) {

        Objects.requireNonNull(transacao, TRANSACAO_INVALIDA);

        if (!(TiposTransacao.SOLICITACAO_EMPRESTIMO.equals(transacao.getTipoTransacao()) || 
              TiposTransacao.SOLICITACAO_DEPOSITO.equals(transacao.getTipoTransacao()))) {
            throw new IllegalArgumentException(TRANSACAO_INVALIDA);
        }

        transacao.atualizaStatus(StatusTransacao.EM_ANALISE);
        transacoes.add(transacao);

        return transacao;
        
    }

    public void depositar(final TransacaoFundo transacao) {

        if (!TiposTransacao.DEPOSITO.equals(transacao.getTipoTransacao())) {
            throw new IllegalArgumentException(TRANSACAO_INVALIDA); 
        }
        Objects.requireNonNull(transacao.getValor(), "Informe o valor do depósito");
        this.saldo = this.saldo.add(transacao.getValor());
    }

    public void sacar(final TransacaoFundo transacao) throws IllegalArgumentException {
        
        if (!TiposTransacao.EMPRESTIMO.equals(transacao.getTipoTransacao())) {
            throw new IllegalArgumentException(TRANSACAO_INVALIDA); 
        }

        Objects.requireNonNull(transacao.getValor(), "Informe um valor para o saque");

        if (transacao.getValor().isGreaterThan(this.saldo)) {
            throw new IllegalArgumentException("Saldo insuficiente para o saque");
        }

        this.saldo = saldo.subtract(transacao.getValor());
    } 

    public MonetaryAmount getSaldo() {

        return saldo;
    }

    public Familia getFamilia() {

        return familia;
    }

    public String getNome() {
        return nome;
    }

    public static FundoFamiliarBuild construtor() {
        return new FundoFamiliarBuild();
    }

    public static class FundoFamiliarBuild {

        private CurrencyUnit brl = Monetary.getCurrency("BRL");
        private MonetaryAmount saldo = Money.zero(brl);
        private Familia familia;
        private String nome;

        public FundoFamiliarBuild comSaldoInicial(MonetaryAmount saldoInicial) {

            this.saldo = saldoInicial;
            return this;
        }

        public FundoFamiliarBuild pertenceAFamilia(Familia familia) {

            this.familia = familia;
            return this;
        }

        public FundoFamiliarBuild comNome(String nomeDoFundo) {

            this.nome = nomeDoFundo;
            return this;
        }

        public FundoFamiliar constroi() {

            Objects.requireNonNull(familia, "Infome a Família");
            Objects.requireNonNull(nome, "Infome o nome do fundo");

            return new FundoFamiliar(this.saldo, this.familia, this.nome);
        }

    }

}