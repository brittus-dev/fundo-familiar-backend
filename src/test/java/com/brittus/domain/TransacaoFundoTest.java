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
            .constroi();
    }

    @Test
    public void testNovaSolicitacaoEmprestimo() {

        transacaoFundo = TransacaoFundo.construtor()
            .comMembroDaFamilia(brunoBrittus)
            .noFundoFamiliar(emergenciaMedica)
            .noValorDe(Money.of(50, reais))
            .realizar(TiposTransacao.SOLICITACAO_EMPRESTIMO)
            .registra();

        Assertions.assertEquals("BRL 50", transacaoFundo.getValor().toString());
        Assertions.assertEquals("bruno@brittus.com", transacaoFundo.getMembroDaFamilia().getEmail().toString());
        Assertions.assertEquals(TiposTransacao.SOLICITACAO_EMPRESTIMO, transacaoFundo.getTipoTransacao());
        Assertions.assertEquals(StatusTransacao.EM_ANALISE, transacaoFundo.getStatus());
    }

    @Test
    public void testSolicitacaoDepositoValor() {

        transacaoFundo = TransacaoFundo.construtor()
            .comMembroDaFamilia(brunoBrittus)
            .noFundoFamiliar(emergenciaMedica)
            .noValorDe(Money.of(50, reais))
            .realizar(TiposTransacao.SOLICITACAO_DEPOSITO)
            .registra();
        
        Assertions.assertEquals("BRL 50", transacaoFundo.getValor().toString());
        Assertions.assertEquals("bruno@brittus.com", transacaoFundo.getMembroDaFamilia().getEmail().toString());
        Assertions.assertEquals(TiposTransacao.SOLICITACAO_DEPOSITO, transacaoFundo.getTipoTransacao());
        Assertions.assertEquals(StatusTransacao.EM_ANALISE, transacaoFundo.getStatus());
    }

    /*@Test
    public void testDepositoComValorNulo() {

        FundoFamiliar fundoFamiliar = FundoFamiliar.construtor()
            .pertenceAFamilia(familiaBrittus)
            .comNome("Teste")
            .constroi();
        
        Exception exception = Assertions.assertThrows(NullPointerException.class, () -> {
            fundoFamiliar.depositar(null);
        });

        String mensagemEsperada = "Informe o valor do depósito";
        String mensagemRecebida = exception.getMessage();

        Assertions.assertTrue(mensagemRecebida.equals(mensagemEsperada));
    }

    @Test
    public void testSolicitarEmprestimo() {

        FundoFamiliar fundoFamiliar = FundoFamiliar.construtor()
            .comSaldoInicial(Money.of(100, reais))
            .constroi();
        fundoFamiliar.sacar(Money.of(50, reais));
        Assertions.assertEquals("BRL 50", fundoFamiliar.getSaldo().toString());
    }

    @Test
    public void testSolicitarEmprestimoValorAcimaSaldo() {

        FundoFamiliar fundoFamiliar = FundoFamiliar.construtor()
            .comSaldoInicial(Money.of(100, reais))
            .constroi();

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            fundoFamiliar.sacar(Money.of(150, reais));
        });

        String mensagemEsperada = "Saldo insuficiente para o saque";
        String mensagemRecebida = exception.getMessage();

        Assertions.assertTrue(mensagemRecebida.equals(mensagemEsperada));

    }*/


}