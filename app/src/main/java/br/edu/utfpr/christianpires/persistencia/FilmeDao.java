package br.edu.utfpr.christianpires.persistencia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.edu.utfpr.christianpires.model.Filme;

@Dao
public interface FilmeDao {

    @Insert
    long insert(Filme filme);

    @Delete
    void delete(Filme filme);

    @Update
    void update(Filme filme);

    @Query("SELECT * FROM filme WHERE id = :id")
    Filme queryForId(long id);

    @Query("SELECT * FROM filme ORDER BY nome ASC")
    List<Filme> queryAll();
}
