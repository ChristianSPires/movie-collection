package br.edu.utfpr.christianpires.persistencia;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import br.edu.utfpr.christianpires.model.Filme;

@Database(entities = {Filme.class}, version = 1, exportSchema = false)
public abstract class FilmesDatabase extends RoomDatabase {

    public abstract FilmeDao filmeDao();

    private static FilmesDatabase instance;

    public static FilmesDatabase getDatabase(final Context context) {

        if (instance == null) {

            synchronized (FilmesDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context,
                            FilmesDatabase.class,
                            "filmes.db").allowMainThreadQueries().build();
                }
            }
        }
        return instance;
    }
}