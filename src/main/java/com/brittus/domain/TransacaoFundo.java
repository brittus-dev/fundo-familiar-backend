package com.brittus.domain;

import java.time.LocalDate;
import java.util.Objects;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;

public class TransacaoFundo {

    private static CurrencyUnit brl = Monetary.getCurrency("BRL");
    private static final MonetaryAmount VALOR_MINIMO_SOLICITACAO = Money.of(50, brl);

    private MembroDaFamilia membroDaFamilia;
    private FundoFamiliar fundoFamiliar;
    private MonetaryAmount valor;
    private LocalDate dataSolicitacao;
    private TiposTransacao tipoTransacao;
    private StatusTransacao status;

    private TransacaoFundo(MembroDaFamilia membroDaFamilia, FundoFamiliar fundoFamiliar,
        MonetaryAmount valor, LocalDate dataSolicitacao, TiposTransacao tipoTransacao) {
        
        this.membroDaFamilia = membroDaFamilia;
        this.fundoFamiliar = fundoFamiliar;
        this.valor = valor;
        this.dataSolicitacao = dataSolicitacao;
        this.tipoTransacao = tipoTransacao;
    }

    @Deprecated()
    public TransacaoFundo() {}

    public MembroDaFamilia getMembroDaFamilia() {
        return membroDaFamilia;
    }

    public FundoFamiliar getFundoFamiliar() {
        return fundoFamiliar;
    }

    public MonetaryAmount getValor() {
        return valor;
    }

    public LocalDate getDataSolicitacao() {
        return dataSolicitacao;
    }

    public TiposTransacao getTipoTransacao() {
        return tipoTransacao;
    }

    public StatusTransacao getStatus() {
        return status;
    }

    public void atualizaStatus(StatusTransacao status) {
        this.status = status;
    }

    public static TransacaoFundoBuilder construtor() {
        return new TransacaoFundoBuilder();
    }
    
    public static class TransacaoFundoBuilder {

        private MembroDaFamilia membroDaFamilia;
        private FundoFamiliar fundoFamiliar;
        private MonetaryAmount valor;
        private TiposTransacao tipoTransacao;

        public TransacaoFundoBuilder realizar(TiposTransacao tipoTransacao) {

            this.tipoTransacao = tipoTransacao;
            return this;
        }

        public TransacaoFundoBuilder comMembroDaFamilia(MembroDaFamilia membroDaFamilia) {

            this.membroDaFamilia = membroDaFamilia;
            return this;
        }

        public TransacaoFundoBuilder noFundoFamiliar(FundoFamiliar fundoFamiliar) {

            this.fundoFamiliar = fundoFamiliar;
            return this;
        }

        public TransacaoFundoBuilder noValorDe(MonetaryAmount valor) {

            this.valor = valor;
            return this;
        }

        public TransacaoFundo registra() {

            Objects.requireNonNull(membroDaFamilia, "Informe o membro da família");
            Objects.requireNonNull(fundoFamiliar, " Informe o fundo familiar");
            Objects.requireNonNull(valor, "Infome o valor");
            Objects.requireNonNull(tipoTransacao, "Informe a transação");

            if (!fundoFamiliar.getFamilia().isMembro(membroDaFamilia)) {
                throw new IllegalArgumentException("Solicitante não faz parte da Família");
            }

            if (VALOR_MINIMO_SOLICITACAO.isGreaterThan(valor)) {
                throw new IllegalArgumentException("Valor informado menor que o mínimo de " + VALOR_MINIMO_SOLICITACAO.toString());
            }

            TransacaoFundo transacaoFundo = new TransacaoFundo(membroDaFamilia, fundoFamiliar, valor, LocalDate.now(), tipoTransacao);

            fundoFamiliar.registrarTransacao(transacaoFundo);

            return transacaoFundo;

        }

    }

}