package br.edu.utfpr.christianpires.model;

import java.util.ArrayList;
import java.util.List;

public class ListaFilmes {

    private List<Filme> alterados;
    private List<Filme> assistidos;
    private List<Filme> desejados;
    private List<Filme> emAndamento;
    private List<Filme> favoritos;
    private List<Filme> terror;
    private List<Filme> suspense;
    private List<Filme> acao;
    private List<Filme> ficcao_cientifica;

    public ListaFilmes() {
        alterados = new ArrayList<>();
        assistidos = new ArrayList<>();
        desejados = new ArrayList<>();
        emAndamento = new ArrayList<>();
        favoritos = new ArrayList<>();
        terror = new ArrayList<>();
        suspense = new ArrayList<>();
        acao = new ArrayList<>();
        ficcao_cientifica = new ArrayList<>();
    }

    public List<Filme> getAlterados() {
        return alterados;
    }

    public List<Filme> getAssistidos() {
        return assistidos;
    }

    public List<Filme> getDesejados() {
        return desejados;
    }

    public List<Filme> getEmAndamento() {
        return emAndamento;
    }

    public List<Filme> getFavoritos() {
        return favoritos;
    }

    public List<Filme> getTerror() { return terror; }
    public List<Filme> getSuspense() { return suspense; }
    public List<Filme> getAcao() { return acao; }
    public List<Filme> getFiccao_cientifica() { return ficcao_cientifica; }

    public void adicionarAssistido(Filme filme) {
        assistidos.add(filme);
    }

    public void adicionarDesejado(Filme filme) {
        desejados.add(filme);
    }

    public void adicionarEmAndamento(Filme filme) {
        emAndamento.add(filme);
    }

    public void adicionarFavorito(Filme filme) {
        favoritos.add(filme);
    }

    public void adicionarTerror(Filme filme) { terror.add(filme); }
    public void adicionarSuspense(Filme filme) { suspense.add(filme); }
    public void adicionarAcao(Filme filme) { acao.add(filme); }
    public void adicionarFiccaoCientifica(Filme filme) { ficcao_cientifica.add(filme); }
    public void adicionarAlterados(Filme filme) {
        alterados.add(filme);
    }
}