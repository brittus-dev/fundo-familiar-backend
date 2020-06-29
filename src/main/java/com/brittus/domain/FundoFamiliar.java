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
    private MembroDaFamilia operadorDoFundo;

    private List<TransacaoFundo> transacoes = new ArrayList<>();

    private static final CurrencyUnit REAIS = Monetary.getCurrency("BRL");
    private static final MonetaryAmount VALOR_MINIMO_DEPOSITO = Money.of(50, REAIS);

    public static final String TRANSACAO_INVALIDA = "Transação inválida";
    public static final String INFORME_VALOR_TRANSACAO = "Informe um valor para a transação";
    public static final String INFORME_A_FAMILIA = "Informe a Família";
    public static final String INFORME_NOME_FUNDO = "Informe o nome do fundo";
    public static final String INFORME_OPERADOR = "Informe o operador do fundo";
    public static final String VALOR_INFORMADO_MENOR_MINIMO_DEPOSITO = "Valor informado menor que o mínimo de " + VALOR_MINIMO_DEPOSITO.toString();
    public static final String TRANSACAO_SEM_AUTORIZACAO = "Solicite autorização para a transação";
    public static final String SALDO_INSUFICIENTE = "Saldo insuficiente para o saque";
    public static final String OPERADOR_NAO_PERTENCE_FAMILIA = "Operador do fundo precisa ser da família";
    public static final String REQUER_STATUS_ANALISE = "Realize uma solicitação antes dessa operação";

    @Deprecated
    public FundoFamiliar() {}

    private FundoFamiliar(MonetaryAmount saldoInicial, Familia familia, String nomeDoFundo, MembroDaFamilia operadorDoFundo) {

        this.saldo = saldoInicial;
        this.familia = familia;
        this.nome = nomeDoFundo;
        this.operadorDoFundo = operadorDoFundo;
    }

    public FundoFamiliar registrarTransacao(final TransacaoFundo transacao) {
        
        Objects.requireNonNull(transacao, TRANSACAO_INVALIDA);
        Objects.requireNonNull(transacao.getValor(), INFORME_VALOR_TRANSACAO);

        switch (transacao.getTipoTransacao()) {
            case SOLICITACAO_EMPRESTIMO:
            case SOLICITACAO_DEPOSITO: this.registrarSolicitacao(transacao); break;
            case DEPOSITO: this.depositar(transacao); break;
            case EMPRESTIMO: this.emprestar(transacao); break;
            default: throw new IllegalArgumentException(TRANSACAO_INVALIDA); 
        }

        transacoes.add(transacao);
        return this;
    }

    private void registrarSolicitacao(final TransacaoFundo transacao) { 

        transacao.atualizarStatus(StatusTransacao.EM_ANALISE);
    }

    private void depositar(final TransacaoFundo transacao) {
        
        FundoFamiliar.validador().requerStatusEmAnalise(transacao.getStatus());
        FundoFamiliar.validador().requerValorMinimoDeposito(transacao.getValor());
        this.saldo = this.saldo.add(transacao.getValor());
        transacao.atualizarStatus(StatusTransacao.CONCLUIDO);
    }

    private void emprestar(final TransacaoFundo transacao) throws IllegalArgumentException {
        
        FundoFamiliar.validador().requerAutorizacao(transacao.getStatus());
        FundoFamiliar.validador().requerSaldo(transacao.getValor(), this.getSaldo());
        this.saldo = saldo.subtract(transacao.getValor());
        transacao.atualizarStatus(StatusTransacao.EM_PROCESSAMENTO);
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

    public MembroDaFamilia getOperadorDoFundo() {
        return operadorDoFundo;
    }

    public static FundoFamiliarBuild construtor() {
        return new FundoFamiliarBuild();
    }

    public static FundoFamiliarRules validador() {
        return new FundoFamiliarRules();
    }

    public static class FundoFamiliarBuild {

        private MonetaryAmount saldo = Money.zero(REAIS);
        private Familia familia;
        private String nome;
        private MembroDaFamilia operadorDoFundo;

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

        public FundoFamiliarBuild operadoPor(MembroDaFamilia operadorDoFundo) {

            FundoFamiliar.validador().requerOperadorDoFundoDaFamilia(familia, operadorDoFundo);
            this.operadorDoFundo = operadorDoFundo;
            return this;
        }

        public FundoFamiliar constroi() {

            Objects.requireNonNull(familia, INFORME_A_FAMILIA);
            Objects.requireNonNull(nome, INFORME_NOME_FUNDO);
            Objects.requireNonNull(operadorDoFundo, INFORME_OPERADOR);

            return new FundoFamiliar(this.saldo, this.familia, this.nome, this.operadorDoFundo);
        }

    }

    public static class FundoFamiliarRules {

        private void requerStatusEmAnalise(StatusTransacao status) {

            if (!StatusTransacao.EM_ANALISE.equals(status)) {
                throw new IllegalArgumentException(REQUER_STATUS_ANALISE);
            }
        }

        private void requerValorMinimoDeposito(MonetaryAmount valor) {
            
            if (VALOR_MINIMO_DEPOSITO.isGreaterThan(valor)) {
                throw new IllegalArgumentException(VALOR_INFORMADO_MENOR_MINIMO_DEPOSITO);
            }
        }

        private void requerAutorizacao(StatusTransacao status) {

            if (!StatusTransacao.AUTORIZADO.equals(status)) {
                throw new IllegalArgumentException(TRANSACAO_SEM_AUTORIZACAO);
            }
        }

        private void requerSaldo(MonetaryAmount valorEmprestimo, MonetaryAmount saldo) {

            if (valorEmprestimo.isGreaterThan(saldo)) {
                throw new IllegalArgumentException(SALDO_INSUFICIENTE);
            }
        }

        private void requerOperadorDoFundoDaFamilia(Familia familia, MembroDaFamilia operadorDoFundo) {
            
            if (!familia.possuiMembro(operadorDoFundo)) {
                throw new IllegalArgumentException(OPERADOR_NAO_PERTENCE_FAMILIA);
            }
        }

    }
}