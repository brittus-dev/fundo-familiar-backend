package com.brittus.domain;

import java.time.LocalDate;
import java.util.Objects;

public class AutorizacaoTransacao {
    
    private TransacaoFundo transacao;
    private boolean autorizacao;
    private MembroDaFamilia autorizador;
    private LocalDate dataAutorizacao;

    private static final String AUTORIZADOR_NAO_PERTENCE_FAMILIA = "Autorizador precisa ser membro da família";
    private static final String INFORME_A_TRANSACAO = "Informe a transação";
    private static final String INFORME_FUNDO_FAMILIAR = "Informe o fundo familiar";
    private static final String INFORME_AUTORIZADOR = "Informe o autorizador";

    private AutorizacaoTransacao(TransacaoFundo transacao, MembroDaFamilia autorizador, 
        boolean autorizacao, LocalDate dataAutorizacao) {
        this.autorizacao = autorizacao;
        this.autorizador = autorizador;
        this.transacao = transacao;
        this.dataAutorizacao = dataAutorizacao;
    }

    public TransacaoFundo getTransacao() {
        return transacao;
    }
    
    public MembroDaFamilia getAutorizador() {
        return autorizador;
    }

    public boolean isAutorizado() {
        return autorizacao;
    }

    public LocalDate getDataAutorizacao() {
        return dataAutorizacao;
    }

    public static AutorizacaoTransacaoRules validador() {
        return new AutorizacaoTransacaoRules();
    }

    public static class AutorizacaoTransacaoBuilder {

        private TransacaoFundo transacao;
        private boolean autorizacao;
        private MembroDaFamilia autorizador;

        public AutorizacaoTransacaoBuilder comTransacao(TransacaoFundo transacao) {

            this.transacao = transacao;
            return this;
        }

        public AutorizacaoTransacaoBuilder comAutorizador(MembroDaFamilia autorizador) {

            this.autorizador = autorizador;
            return this;
        }

        public AutorizacaoTransacao registraAutorizacao(boolean autorizacao) {

            Objects.requireNonNull(transacao, INFORME_A_TRANSACAO);
            Objects.requireNonNull(transacao.getFundoFamiliar(), INFORME_FUNDO_FAMILIAR);
            Objects.requireNonNull(autorizador, INFORME_AUTORIZADOR);

            AutorizacaoTransacao.validador()
                .requerAutorizadorMembroDaFamilia(
                    transacao.getFundoFamiliar().getFamilia(), autorizador);
            this.autorizacao = autorizacao;
            return new AutorizacaoTransacao(this.transacao, this.autorizador, this.autorizacao, LocalDate.now());
        }
    }

    public static class AutorizacaoTransacaoRules {

        private void requerAutorizadorMembroDaFamilia(Familia familia, 
            MembroDaFamilia autorizador) {
            
            if (!familia.possuiMembro(autorizador)) {
                throw new IllegalArgumentException(AUTORIZADOR_NAO_PERTENCE_FAMILIA);
            }

        }
    }
}