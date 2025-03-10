package com.tzolas.filmtracker.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "peliculas")
public class Pelicula {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String titulo;
    private String director;
    private int anio;
    private float puntuacion;
    private String reseña;

    public Pelicula(String titulo, String director, int anio, float puntuacion, String reseña) {
        this.titulo = titulo;
        this.director = director;
        this.anio = anio;
        this.puntuacion = puntuacion;
        this.reseña = reseña;
    }

    // Getters
    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDirector() { return director; }
    public int getAnio() { return anio; }
    public float getPuntuacion() { return puntuacion; }
    public String getReseña() { return reseña; }

    // Setters - 🔥 Agregados para solucionar los errores
    public void setId(int id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDirector(String director) { this.director = director; }
    public void setAnio(int anio) { this.anio = anio; }
    public void setPuntuacion(float puntuacion) { this.puntuacion = puntuacion; }
    public void setReseña(String reseña) { this.reseña = reseña; }
}
