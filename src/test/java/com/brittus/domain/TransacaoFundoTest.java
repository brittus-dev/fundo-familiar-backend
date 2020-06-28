package com.brittus.domain;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class TransacaoFundoTest {

    private CurrencyUnit reais = Monetary.getCurrency("BRL");
    private MembroDaFamilia brunoBrittus;
    private Familia familiaBrittus;
    private FundoFamiliar emergenciaMedica;
    private TransacaoFundo transacaoFundo;

    @BeforeEach
    private void setup() {
        brunoBrittus = MembroDaFamilia.construtor()
            .comEmail("bruno@brittus.com")
            .comNome("Bruno Brito")
            .comTelefone("21999999999")
            .constroi();

        familiaBrittus = Familia.dos("Brittus");
        familiaBrittus.adiciona(brunoBrittus);

        emergenciaMedica = FundoFamiliar.construtor()
            .comNome("Emergência Médica")
            .comSaldoInicial(Money.of(100, reais))
            .pertenceAFamilia(familiaBrittus)
            .operadoPor(brunoBrittus)
            .constroi();
    }

    @Test
    public void testNovaSolicitacaoEmprestimo() {

        transacaoFundo = TransacaoFundo.construtor()
            .comMembroDaFamilia(brunoBrittus)
            .noFundoFamiliar(emergenciaMedica)
            .noValorDe(Money.of(50, reais))
            .comTipo(TiposTransacao.SOLICITACAO_EMPRESTIMO)
            .constroi();

        emergenciaMedica = emergenciaMedica.registrarTransacao(transacaoFundo);

        Assertions.assertEquals("BRL 50", transacaoFundo.getValor().toString());
        Assertions.assertEquals("bruno@brittus.com", transacaoFundo.getSolicitante().getEmail().toString());
        Assertions.assertEquals(TiposTransacao.SOLICITACAO_EMPRESTIMO, transacaoFundo.getTipoTransacao());
        Assertions.assertEquals(StatusTransacao.EM_ANALISE, transacaoFundo.getStatus());
    }

    @Test
    public void testNovaSolicitacaoDeposito() {

        transacaoFundo = TransacaoFundo.construtor()
            .comMembroDaFamilia(brunoBrittus)
            .noFundoFamiliar(emergenciaMedica)
            .noValorDe(Money.of(50, reais))
            .comTipo(TiposTransacao.SOLICITACAO_DEPOSITO)
            .constroi();

        emergenciaMedica = emergenciaMedica.registrarTransacao(transacaoFundo);

        Assertions.assertEquals("BRL 50", transacaoFundo.getValor().toString());
        Assertions.assertEquals("bruno@brittus.com", transacaoFundo.getSolicitante().getEmail().toString());
        Assertions.assertEquals(TiposTransacao.SOLICITACAO_DEPOSITO, transacaoFundo.getTipoTransacao());
        Assertions.assertEquals(StatusTransacao.EM_ANALISE, transacaoFundo.getStatus());
    }

    @Test
    public void testRegistroDeposito() {

        transacaoFundo = TransacaoFundo.construtor()
            .comMembroDaFamilia(brunoBrittus)
            .noFundoFamiliar(emergenciaMedica)
            .noValorDe(Money.of(50, reais))
            .comTipo(TiposTransacao.SOLICITACAO_DEPOSITO)
            .constroi();

        emergenciaMedica.registrarTransacao(transacaoFundo);
        
        transacaoFundo = transacaoFundo.comOperador(brunoBrittus).operar(TiposTransacao.DEPOSITO);
        emergenciaMedica = emergenciaMedica.registrarTransacao(transacaoFundo);
        
        Assertions.assertEquals("BRL 50", transacaoFundo.getValor().toString());
        Assertions.assertEquals("BRL 150", emergenciaMedica.getSaldo().toString());
        Assertions.assertEquals("bruno@brittus.com", transacaoFundo.getSolicitante().getEmail().toString());
        Assertions.assertEquals(TiposTransacao.DEPOSITO, transacaoFundo.getTipoTransacao());
        Assertions.assertEquals(StatusTransacao.CONCLUIDO, transacaoFundo.getStatus());
    }

}