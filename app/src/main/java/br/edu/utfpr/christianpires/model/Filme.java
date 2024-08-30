package br.edu.utfpr.christianpires.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

@Entity
public class Filme implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    private long id;

    @NonNull
    @ColumnInfo(name = "NOME")
    private String nome;

    @NonNull
    @ColumnInfo(name = "CATEGORIA")
    private int categoria;

    @ColumnInfo(name = "DURACAO")
    private String duracao;

    @ColumnInfo(name = "DESCRICAO")
    private String descricao;

    @NonNull
    @ColumnInfo(name = "AVALIACAO_COMENTARIO")
    private String avaliacaoComentario;

    @NonNull
    @ColumnInfo(name = "LISTA")
    private ListaUsada listaUsada;

    @NonNull
    @ColumnInfo(name = "FAVORITO")
    private boolean favorito;

    public Filme() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getNome() {
        return nome;
    }

    public void setNome(@NonNull String nome) {
        this.nome = nome;
    }

    @NonNull
    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(@NonNull int categoria) {
        this.categoria = categoria;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @NonNull
    public String getAvaliacaoComentario() {
        return avaliacaoComentario;
    }

    public void setAvaliacaoComentario(@NonNull String avaliacaoComentario) {
        this.avaliacaoComentario = avaliacaoComentario;
    }

    @NonNull
    public ListaUsada getListaUsada() {
        return listaUsada;
    }

    public void setListaUsada(@NonNull ListaUsada listaUsada) {
        this.listaUsada = listaUsada;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Filme filme = (Filme) obj;
        return Objects.equals(nome, filme.nome) && Objects.equals(categoria, filme.categoria)
                && Objects.equals(duracao, filme.duracao) && Objects.equals(descricao, filme.descricao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, categoria, duracao, descricao);
    }

    public static Comparator ordenacaoCrescente = new Comparator<Filme>() {
        @Override
        public int compare(Filme filme1, Filme filme2) {
            return filme1.getNome().compareToIgnoreCase(filme2.getNome());
        }
    };

    public static Comparator ordenacaoDecrescente = new Comparator<Filme>() {
        @Override
        public int compare(Filme filme1, Filme filme2) {
            return -1 * filme1.getNome().compareToIgnoreCase(filme2.getNome());
        }
    };
}
