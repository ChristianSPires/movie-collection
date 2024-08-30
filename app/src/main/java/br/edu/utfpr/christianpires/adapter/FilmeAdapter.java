package br.edu.utfpr.christianpires.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import br.edu.utfpr.christianpires.R;
import br.edu.utfpr.christianpires.model.Filme;
import br.edu.utfpr.christianpires.model.ListaUsada;

public class FilmeAdapter extends BaseAdapter {

    private List<Filme> filmes;
    private Context context;
    private String[] categorias;

    private static class FilmeHolder {

        public TextView textViewNomeFilme;
        public TextView textViewCategoria;
        public TextView textViewEstaNaLista;
        public TextView textViewFavorito;
        public TextView textViewDetalhes;
    }

    public FilmeAdapter(Context context, List<Filme> filmes) {
        this.context = context;
        this.filmes = filmes;
        categorias = context.getResources().getStringArray(R.array.categoria);
    }

    @Override
    public int getCount() {
        return filmes.size();
    }

    @Override
    public Object getItem(int position) {
        return filmes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        FilmeHolder holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_filme, viewGroup, false);

            holder = new FilmeHolder();

            holder.textViewNomeFilme = view.findViewById(R.id.textViewNomeFilme);
            holder.textViewCategoria = view.findViewById(R.id.textViewCategoria);
            holder.textViewEstaNaLista = view.findViewById(R.id.textViewEstaNaLista);
            holder.textViewFavorito = view.findViewById(R.id.textViewFavorito);
            holder.textViewDetalhes = view.findViewById(R.id.textViewDetalhes);

            view.setTag(holder);

        } else {
            holder = (FilmeHolder) view.getTag();
        }

        Filme filme = filmes.get(position);

        holder.textViewNomeFilme.setText(filmes.get(position).getNome());

        switch (filmes.get(position).getCategoria()) {

            case 0:
                holder.textViewCategoria.setText(R.string.terror);
                break;
            case 1:
                holder.textViewCategoria.setText(R.string.suspense);
                break;
            case 2:
                holder.textViewCategoria.setText(R.string.acao);
                break;
            case 3:
                holder.textViewCategoria.setText(R.string.ficcao_cientifica);
                break;
        }

        holder.textViewFavorito.setText(filme.isFavorito() ? R.string.sim : R.string.nao);

        if (filme.getListaUsada() == ListaUsada.Assistidos){
            holder.textViewEstaNaLista.setText(R.string.assistidos);
        } else if (filme.getListaUsada() == ListaUsada.Desejados){
            holder.textViewEstaNaLista.setText(R.string.desejados);
        } else if (filme.getListaUsada() == ListaUsada.EmAndamento){
            holder.textViewEstaNaLista.setText(R.string.em_andamento);
        }

        holder.textViewDetalhes.setText(filmes.get(position).getAvaliacaoComentario());

        return view;
    }
}
