package com.tzolas.filmtracker.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tzolas.filmtracker.R;
import com.tzolas.filmtracker.adapters.PeliculaAdapter;
import com.tzolas.filmtracker.database.AppDatabase;
import com.tzolas.filmtracker.database.entities.Pelicula;
import com.tzolas.filmtracker.utils.NotificationHelper;
import java.util.List;
import java.io.*;



public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PeliculaAdapter adapter;
    private List<Pelicula> listaPeliculas;
    private AppDatabase database;
    private static final String FILE_NAME = "peliculas.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aplicarModoOscuro();
        setContentView(R.layout.activity_main);

        // Inicializar la base de datos
        database = AppDatabase.getInstance(this);
        listaPeliculas = database.peliculaDao().obtenerTodasPeliculas();

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewPeliculas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PeliculaAdapter(this, listaPeliculas, new PeliculaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Pelicula pelicula) {
                mostrarDialogoEditar(pelicula);
            }

            @Override
            public void onDeleteClick(Pelicula pelicula) {
                mostrarDialogoEliminar(pelicula);
            }
        });

        recyclerView.setAdapter(adapter);

        // Configurar FloatingActionButton
        FloatingActionButton fabAgregar = findViewById(R.id.fabAgregarPelicula);
        fabAgregar.setOnClickListener(v -> mostrarDialogoAgregar());

        // Crear canal de notificaciones
        NotificationHelper.createNotificationChannel(this);
    }

    private void aplicarModoOscuro() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isDarkMode = prefs.getBoolean("dark_mode", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu); // Asegúrate de que sea main_menu.xml
        return true;
    }

    // Manejar clics en el menú
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                mostrarAcercaDe();
                return true;
            case R.id.action_settings:
                abrirConfiguracion();
                return true;
            case R.id.action_exit:
                cerrarAplicacion();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void mostrarAcercaDe() {
        Toast.makeText(this, "FilmTracker v1.0\nDesarrollado por Aitzol", Toast.LENGTH_LONG).show();
    }

    private void abrirConfiguracion() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void cerrarAplicacion() {
        finishAffinity(); // Cierra toda la aplicación
    }

    private void mostrarDialogoAgregar() {
        Pelicula nuevaPelicula = new Pelicula("Nueva Película", "Director", 2024, 0, "");
        database.peliculaDao().insertarPelicula(nuevaPelicula);
        actualizarLista();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean notificacionesHabilitadas = prefs.getBoolean("notifications", true);
        if (notificacionesHabilitadas) {
            NotificationHelper.sendNotification(this, "Nueva Película", "Se agregó una nueva película.");
        }

        Toast.makeText(this, "Película añadida", Toast.LENGTH_SHORT).show();
    }

    private void mostrarDialogoEditar(Pelicula pelicula) {
        pelicula.setTitulo(pelicula.getTitulo() + " (Editado)");
        database.peliculaDao().actualizarPelicula(pelicula);
        actualizarLista();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean notificacionesHabilitadas = prefs.getBoolean("notifications", true);
        if (notificacionesHabilitadas) {
            NotificationHelper.sendNotification(this, "Película Editada", "Se modificó " + pelicula.getTitulo());
        }

        Toast.makeText(this, "Película actualizada", Toast.LENGTH_SHORT).show();
    }

    private void mostrarDialogoEliminar(Pelicula pelicula) {
        database.peliculaDao().eliminarPelicula(pelicula);
        actualizarLista();
        Toast.makeText(this, "Película eliminada", Toast.LENGTH_SHORT).show();
    }

    private void actualizarLista() {
        listaPeliculas.clear();
        listaPeliculas.addAll(database.peliculaDao().obtenerTodasPeliculas());
        adapter.notifyDataSetChanged();
    }

    // Guardar películas en un archivo
    private void guardarPeliculasEnArchivo() {
        try (FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(listaPeliculas);
            Toast.makeText(this, "Películas guardadas", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar películas", Toast.LENGTH_SHORT).show();
        }
    }

    // Cargar películas desde un archivo
    private void cargarPeliculasDesdeArchivo() {
        try (FileInputStream fis = openFileInput(FILE_NAME);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            listaPeliculas = (List<Pelicula>) ois.readObject();
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Películas cargadas", Toast.LENGTH_SHORT).show();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "No se encontraron datos previos", Toast.LENGTH_SHORT).show();
        }
    }
}
