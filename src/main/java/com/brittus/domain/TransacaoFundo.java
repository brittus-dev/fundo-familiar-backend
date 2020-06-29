package com.brittus.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.money.MonetaryAmount;

import org.bson.types.ObjectId;

import io.quarkus.mongodb.panache.MongoEntity;

@MongoEntity(collection = "transacoes")
public class TransacaoFundo {

    public ObjectId id;
    private MembroDaFamilia operador;
    private MembroDaFamilia solicitante;
    private FundoFamiliar fundoFamiliar;
    private MonetaryAmount valor;
    private LocalDate dataSolicitacao;
    private TiposTransacao tipoTransacao;
    private StatusTransacao status;

    private List<AutorizacaoTransacao> autorizacoes = new ArrayList<>();

    public static final String AUTORIZACAO_JA_REGISTRADA = "Autorização já registrada";
    public static final String AUTORIZADOR_NAO_PERTENCE_FAMILIA = "Autorizador não pertence a Familia";
    public static final String APENAS_SOLICITACOES = "Apenas transacões de solicitação";

    private TransacaoFundo(MembroDaFamilia solicitante, MembroDaFamilia operador, FundoFamiliar fundoFamiliar,
        MonetaryAmount valor, LocalDate dataSolicitacao, TiposTransacao tipoTransacao) {
        
        this.solicitante = solicitante;
        this.operador = operador;
        this.fundoFamiliar = fundoFamiliar;
        this.valor = valor;
        this.dataSolicitacao = dataSolicitacao;
        this.tipoTransacao = tipoTransacao;
    }

    @Deprecated()
    public TransacaoFundo() {}

    public MembroDaFamilia getSolicitante() {
        return solicitante;
    }

    public MembroDaFamilia getOperador() {
        return operador;
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

    public List<AutorizacaoTransacao> getAutorizacoes() {
        return autorizacoes;
    }

    public void atualizarStatus(StatusTransacao status) {
        this.status = status;
    }

    public void registrarAutorizacao(final AutorizacaoTransacao autorizacaoTransacao) {

        TransacaoFundo.validador().autorizacaoJaRegistrada(this.autorizacoes, autorizacaoTransacao);
        TransacaoFundo.validador().autorizadorNaoPertenceFamilia(this.fundoFamiliar, autorizacaoTransacao);
        this.autorizacoes.add(autorizacaoTransacao);

        if (this.isAutorizada()) {
            this.atualizarStatus(StatusTransacao.AUTORIZADO);
        }
    }

    private boolean isAutorizada() {
        long qtdAutorizacoes = this.autorizacoes.stream()
            .filter(autorizacao -> autorizacao.isAutorizado())
            .map(autorizacao -> autorizacao.getAutorizador())
            .filter(membroDaFamilia -> this.fundoFamiliar.getFamilia().possuiMembro(membroDaFamilia))
            .count();
        
        return Objects.equals(qtdAutorizacoes, this.fundoFamiliar.getFamilia().totalMembros());
    }

    public TransacaoFundo comOperador(MembroDaFamilia operador) {

        this.operador = operador;
        return this;
    }

    public TransacaoFundo operar(TiposTransacao tipoTransacao) {

        Objects.requireNonNull(this.operador, "Informe o operador");
        TransacaoFundo.validador().requerTipoOperacao(tipoTransacao);
        TransacaoFundo.validador().operadorNaoPertenceFamilia(fundoFamiliar, this.operador);
        
        this.tipoTransacao = tipoTransacao;
        return this;
    }

    public static TransacaoFundoRules validador() {
        return new TransacaoFundoRules();
    }

    public static TransacaoFundoBuilder construtor() {
        return new TransacaoFundoBuilder();
    }
    
    public static class TransacaoFundoBuilder {

        private MembroDaFamilia solicitante;
        private MembroDaFamilia operador;
        private FundoFamiliar fundoFamiliar;
        private MonetaryAmount valor;
        private TiposTransacao tipoTransacao;

        public TransacaoFundoBuilder comMembroDaFamilia(MembroDaFamilia solicitante) {

            this.solicitante = solicitante;
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

        public TransacaoFundoBuilder comTipo(TiposTransacao tipoTransacao) {

            this.tipoTransacao = tipoTransacao;
            return this;
        }

        public TransacaoFundo constroi() {

            Objects.requireNonNull(solicitante, "Informe o membro da família");
            Objects.requireNonNull(fundoFamiliar, " Informe o fundo familiar");
            Objects.requireNonNull(valor, "Infome o valor");
            Objects.requireNonNull(tipoTransacao, "Informe a transação");

            TransacaoFundo.validador().requerTipoSolicitacao(tipoTransacao);
            TransacaoFundo.validador().solicitanteNaoPertenceFamilia(fundoFamiliar, solicitante);

            TransacaoFundo transacaoFundo = 
                new TransacaoFundo(solicitante, operador, fundoFamiliar, valor, LocalDate.now(), tipoTransacao);

            return transacaoFundo;

        }

    }

    public static class TransacaoFundoRules {

        private void autorizacaoJaRegistrada(List<AutorizacaoTransacao> autorizacoes, 
            AutorizacaoTransacao autorizacaoTransacao) {
            if (autorizacoes.contains(autorizacaoTransacao)) {
                throw new IllegalArgumentException(AUTORIZACAO_JA_REGISTRADA);
            }
        }

        private void autorizadorNaoPertenceFamilia(FundoFamiliar fundoFamiliar, 
            AutorizacaoTransacao autorizacaoTransacao) {

            if (!fundoFamiliar.getFamilia().possuiMembro(autorizacaoTransacao.getAutorizador())) {
                throw new IllegalArgumentException(AUTORIZADOR_NAO_PERTENCE_FAMILIA);
            }
        }

        private void requerTipoSolicitacao(TiposTransacao tipoTransacao) {
            if (!(TiposTransacao.SOLICITACAO_DEPOSITO.equals(tipoTransacao) || TiposTransacao.SOLICITACAO_EMPRESTIMO.equals(tipoTransacao))) {
                throw new IllegalArgumentException(APENAS_SOLICITACOES);
            }
        }

        private void requerTipoOperacao(TiposTransacao tipoTransacao) {
            if (!(TiposTransacao.DEPOSITO.equals(tipoTransacao) || TiposTransacao.EMPRESTIMO.equals(tipoTransacao))) {
                throw new IllegalArgumentException(APENAS_SOLICITACOES);
            }
        }

        private void solicitanteNaoPertenceFamilia(FundoFamiliar fundoFamiliar, 
            MembroDaFamilia solicitante) {
            if (!fundoFamiliar.getFamilia().possuiMembro(solicitante)) {
                throw new IllegalArgumentException("Solicitante não faz parte da Família");
            }
        }

        private void operadorNaoPertenceFamilia(FundoFamiliar fundoFamiliar, 
            MembroDaFamilia operador) {
            if (!fundoFamiliar.getOperadorDoFundo().equals(operador)) {
                throw new IllegalArgumentException("Operador não opera esse fundo familiar");
            }
        }
    }
}