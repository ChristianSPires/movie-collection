package br.edu.utfpr.christianpires.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.christianpires.model.Filme;
import br.edu.utfpr.christianpires.model.ListaFilmes;
import br.edu.utfpr.christianpires.R;
import br.edu.utfpr.christianpires.model.ListaUsada;
import br.edu.utfpr.christianpires.persistencia.FilmesDatabase;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerCategoria;
    private ListaFilmes listaFilmes;
    private RadioGroup radioGroup;
    private CheckBox checkBoxFavoritos;
    private EditText editTextAvaliacaoComentario;
    private EditText editTextNomeFilme;
    public static final String MODO = "MODO";
    public static final int NOVO = 1;
    public static final int EDITAR = 2;
    private int modo;
    public static final String ID = "ID";
    private long identificador;
    public static final String CATEGORIA = "CATEGORIA";
    private int categoriaOriginal;
    public static final String NOME = "NOME";
    private String nomeOriginal;
    public static final String LISTA = "LISTA";
    private String listaOriginal;
    public static final String FAVORITO = "FAVORITO";
    private boolean favoritoOriginal;
    public static final String AVALIACAO_COMENTARIO = "AVALIACAO_COMENTARIO";
    private String avaliacaoComentarioOriginal;

    public static void novoFilme(AppCompatActivity activity, ActivityResultLauncher<Intent> launcher){
        Intent intent = new Intent(activity, MainActivity.class);

        intent.putExtra(MODO, NOVO);

        launcher.launch(intent);
    }

    public static void editarFilme(AppCompatActivity activity, ActivityResultLauncher<Intent> launcher, Filme filme) {

        Intent intent = new Intent(activity, MainActivity.class);

        intent.putExtra(MODO, EDITAR);

        intent.putExtra(ID, filme.getId());
        intent.putExtra(CATEGORIA, filme.getCategoria());
        intent.putExtra(NOME, filme.getNome());
        intent.putExtra(LISTA, filme.getListaUsada().toString());
        intent.putExtra(FAVORITO, filme.isFavorito());
        intent.putExtra(AVALIACAO_COMENTARIO, filme.getAvaliacaoComentario());

        launcher.launch(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listaFilmes = new ListaFilmes();

        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        editTextNomeFilme = findViewById(R.id.editTextNomeFilme);
        radioGroup = findViewById(R.id.radioGroup);
        checkBoxFavoritos = findViewById(R.id.checkBoxFavoritos);
        editTextAvaliacaoComentario = findViewById(R.id.editTextAvaliacaoComentario);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            modo = bundle.getInt(MODO, NOVO);

            if (modo == NOVO) {
                setTitle(getString(R.string.novo_filme));
            } else if (modo == EDITAR) {
                setTitle(getString(R.string.editar_filme));

                long id = bundle.getLong(ID);

                FilmesDatabase database = FilmesDatabase.getDatabase(this);

                Filme filme = database.filmeDao().queryForId(id);

                identificador = filme.getId();
                categoriaOriginal = filme.getCategoria();
                nomeOriginal = filme.getNome();
                listaOriginal = filme.getListaUsada().toString();
                RadioButton button = null;
                favoritoOriginal = filme.isFavorito();
                avaliacaoComentarioOriginal = filme.getAvaliacaoComentario();

                spinnerCategoria.setSelection(categoriaOriginal);
                editTextNomeFilme.setText(nomeOriginal);

                if (ListaUsada.Assistidos.equals(ListaUsada.valueOf(listaOriginal))) {
                    button = findViewById(R.id.radioButtonWatched);
                } else if (ListaUsada.Desejados.equals(ListaUsada.valueOf(listaOriginal))) {
                    button = findViewById(R.id.radioButtonDesired);
                } else if (ListaUsada.EmAndamento.equals(ListaUsada.valueOf(listaOriginal))) {
                    button = findViewById(R.id.radioButtonInProgress);
                }

                if (button != null){
                    button.setChecked(true);
                }

                checkBoxFavoritos.setChecked(favoritoOriginal);
                editTextAvaliacaoComentario.setText(avaliacaoComentarioOriginal);
            }
        }
    }

    private boolean camposVazios() {
        if (editTextNomeFilme.getText().toString().trim().isEmpty() ||
                radioGroup.getCheckedRadioButtonId() == -1 ||
                editTextAvaliacaoComentario.getText().toString().trim().isEmpty()) {
            return true;
        }
        return false;
    }

    private void salvar(){
        if (!camposVazios()) {
            int categoria = spinnerCategoria.getSelectedItemPosition();
            String nomeFilme = editTextNomeFilme.getText().toString();
            int radioButtonId = radioGroup.getCheckedRadioButtonId();

            ListaUsada listaUsada = null;

            if (radioButtonId == R.id.radioButtonWatched){
                listaUsada = ListaUsada.Assistidos;
            } else if (radioButtonId == R.id.radioButtonDesired) {
                listaUsada = ListaUsada.Desejados;
            } else if (radioButtonId == R.id.radioButtonInProgress) {
                listaUsada = ListaUsada.EmAndamento;
            }

            boolean favorito = checkBoxFavoritos.isChecked();
            String avaliacaoComentario = editTextAvaliacaoComentario.getText().toString();

            Intent intent = new Intent();
            intent.putExtra(CATEGORIA, categoria);
            intent.putExtra(NOME, nomeFilme);
            intent.putExtra(LISTA, listaUsada.toString());
            intent.putExtra(FAVORITO, favorito);
            intent.putExtra(AVALIACAO_COMENTARIO, avaliacaoComentario);

            FilmesDatabase database = FilmesDatabase.getDatabase(this);

            Filme filme = new Filme();

            filme.setCategoria(categoria);
            filme.setNome(nomeFilme);
            filme.setListaUsada(listaUsada);
            filme.setFavorito(favorito);
            filme.setAvaliacaoComentario(avaliacaoComentario);

            if(modo == NOVO){
                database.filmeDao().insert(filme);
            } else{
                filme.setId(identificador);
                database.filmeDao().update(filme);
            }

            resetUI();
            Toast.makeText(this, R.string.filme_salvo_com_sucesso, Toast.LENGTH_LONG).show();
            setResult(Activity.RESULT_OK, intent);
            finish();

        } else {
            if (radioGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, R.string.favor_selecionar_lista, Toast.LENGTH_LONG).show();
            } else if (editTextAvaliacaoComentario.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, R.string.favor_adicionar_avaliacao, Toast.LENGTH_LONG).show();
                editTextAvaliacaoComentario.requestFocus();
            } else if (editTextNomeFilme.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, R.string.favor_adicionar_nome_filme, Toast.LENGTH_LONG).show();
                editTextNomeFilme.requestFocus();
            }
        }
    }

    private void resetUI() {
        spinnerCategoria.setSelection(0);
        EditText editTextNomeFilme = findViewById(R.id.editTextNomeFilme);
        editTextNomeFilme.getText().clear();
        radioGroup.clearCheck();
        checkBoxFavoritos.setChecked(false);
        EditText editTextAvaliacaoComentario = findViewById(R.id.editTextAvaliacaoComentario);
        editTextAvaliacaoComentario.getText().clear();
    }

    public void cancelar() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cadastro_opcoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int idMenuItem = item.getItemId();

        if (idMenuItem == R.id.menuItemSalvar) {
            salvar();
            return true;
        } else if (idMenuItem == R.id.menuItemLimpar) {
            resetUI();
            Toast.makeText(this, R.string.selecoes_limpas, Toast.LENGTH_LONG).show();
            return true;
        } else if (idMenuItem == android.R.id.home){
            cancelar();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}