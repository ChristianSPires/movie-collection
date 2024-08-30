package br.edu.utfpr.christianpires.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Collections;
import java.util.List;

import br.edu.utfpr.christianpires.adapter.FilmeAdapter;
import br.edu.utfpr.christianpires.R;
import br.edu.utfpr.christianpires.model.Filme;
import br.edu.utfpr.christianpires.persistencia.FilmesDatabase;

public class ListagemDeFilmesActivity extends AppCompatActivity {

    private ListView listViewFilmes;
    private FilmeAdapter filmeAdapter;
    private ActionMode actionMode;
    private View viewSelecionada;
    private int posicaoSelecionada = -1;
    private List<Filme> filmes;
    public static final String ARQUIVO = "br.edu.utfpr.christianpires.sharedpreferences.PREFERENCIAS";
    public static final String ORDENACAO_ASCENDENTE = "ORDENACAO_ASCENDENTE";
    private boolean ordenacaoAscendente = true;

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflate = mode.getMenuInflater();
            inflate.inflate(R.menu.principal_item_selecionado, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int idMenuItem = item.getItemId();

            if (idMenuItem == R.id.menuItemEditar) {
                editarFilme();
                mode.finish();
                return true;
            } else if(idMenuItem == R.id.menuItemExcluir) {
                excluirFilme();
                mode.finish();
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            if (viewSelecionada != null){
                viewSelecionada.setBackgroundColor(Color.TRANSPARENT);
            }

            actionMode = null;
            viewSelecionada = null;

            listViewFilmes.setEnabled(true);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        lerPreferenciaOrdenacaoAscendente();
        ordenarLista();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_de_filmes);



        FilmesDatabase database = FilmesDatabase.getDatabase(this);
        database.filmeDao().queryAll();

        listViewFilmes = findViewById(R.id.listViewFilmes);
        listViewFilmes.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listViewFilmes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView <?> parent,
                                           View view,
                                           int position,
                                           long id){

                if (actionMode != null){
                    return false;
                }

                posicaoSelecionada = position;

                view.setBackgroundColor(Color.LTGRAY);

                viewSelecionada = view;

                listViewFilmes.setEnabled(false);

                actionMode = startSupportActionMode(mActionModeCallback);

                return false;
            }
        });

        popularLista();

        lerPreferenciaOrdenacaoAscendente();
        ordenarLista();
    }

    private void popularLista() {
        filmes = FilmesDatabase.getDatabase(this).filmeDao().queryAll();
        filmeAdapter = new FilmeAdapter(this, filmes);
        listViewFilmes.setAdapter(filmeAdapter);
    }

    private void excluirFilme() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirmacao);
        builder.setMessage(R.string.tem_certeza_que_deseja_excluir_o_filme_selecionado);
        builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                realizarExclusaoFilme();
            }
        });
        builder.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void realizarExclusaoFilme() {
        FilmesDatabase database = FilmesDatabase.getDatabase(ListagemDeFilmesActivity.this);
        database.filmeDao().delete(filmes.get(posicaoSelecionada));
        filmes.clear();
        filmes.addAll(database.filmeDao().queryAll());
        filmeAdapter.notifyDataSetChanged();
    }

    private void editarFilme() {
        Filme filme = filmes.get(this.posicaoSelecionada);
        MainActivity.editarFilme(this, launcherEditarFilme, filme);
    }

    ActivityResultLauncher<Intent> launcherEditarFilme = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        Bundle bundle = intent.getExtras();

                        if (bundle != null) {
                            posicaoSelecionada = -1;
                            FilmesDatabase database = FilmesDatabase.getDatabase(ListagemDeFilmesActivity.this);
                            database.filmeDao().queryAll();
                            popularLista();
                            ordenarLista();
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> launcherNovoFilme = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        Bundle bundle = intent.getExtras();

                        if (bundle != null) {
                            FilmesDatabase database = FilmesDatabase.getDatabase(ListagemDeFilmesActivity.this);
                            database.filmeDao().queryAll();
                            popularLista();
                            ordenarLista();
                        }
                    }
                }
            });

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal_opcoes, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem menuItemOrdenacao = menu.findItem(R.id.menuItemOrdenacao);

        atualizarIconeOrdenacao(menuItemOrdenacao);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int botao = item.getItemId();

        if(botao == R.id.menuItemOrdenacao){
            salvarPreferenciaOrdenacaoAscendente(!ordenacaoAscendente);
            atualizarIconeOrdenacao(item);
            ordenarLista();
            return true;
        } else
            if(botao == R.id.menuItemAdicionar){
                MainActivity.novoFilme(this, launcherNovoFilme);
                return true;
        } else
            if (botao == R.id.menuItemSobre){
                AutoriaActivity.nova(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void atualizarIconeOrdenacao(MenuItem menuItemOrdenacao){

        int iconResId = (ordenacaoAscendente) ? R.drawable.ic_action_ordenacao_ascendente : R.drawable.ic_action_ordenacao_descendente;
        menuItemOrdenacao.setIcon(iconResId);
    }

    private void ordenarLista() {

        if(ordenacaoAscendente){
            Collections.sort(filmes, Filme.ordenacaoCrescente);
        } else{
            Collections.sort(filmes, Filme.ordenacaoDecrescente);
        }

        filmeAdapter.notifyDataSetChanged();
    }

    private void lerPreferenciaOrdenacaoAscendente() {

        SharedPreferences shared = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);

        ordenacaoAscendente = shared.getBoolean(ORDENACAO_ASCENDENTE, ordenacaoAscendente);
    }

    private void salvarPreferenciaOrdenacaoAscendente(boolean novoValor) {
        SharedPreferences shared = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
        shared.edit().putBoolean(ORDENACAO_ASCENDENTE, novoValor).apply();
        ordenacaoAscendente = novoValor;
    }
}
