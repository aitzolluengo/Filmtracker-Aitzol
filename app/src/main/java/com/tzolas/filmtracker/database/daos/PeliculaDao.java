package com.tzolas.filmtracker.database.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.tzolas.filmtracker.database.entities.Pelicula;

import java.util.List;

@Dao
public interface PeliculaDao {
    @Insert
    void insertarPelicula(Pelicula pelicula);

    @Update
    void actualizarPelicula(Pelicula pelicula);

    @Delete
    void eliminarPelicula(Pelicula pelicula);

    @Query("SELECT * FROM peliculas ORDER BY titulo ASC")
    List<Pelicula> obtenerTodasPeliculas();
}
