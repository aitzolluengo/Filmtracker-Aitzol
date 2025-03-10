package com.tzolas.filmtracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.tzolas.filmtracker.R;

public class DetallePeliculaActivity extends AppCompatActivity {

    private TextView textTitulo, textDirector, textAnio, textPuntuacion, textReseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pelicula);

        textTitulo = findViewById(R.id.textTituloDetalle);
        textDirector = findViewById(R.id.textDirectorDetalle);
        textAnio = findViewById(R.id.textAnioDetalle);
        textPuntuacion = findViewById(R.id.textPuntuacionDetalle);
        textReseña = findViewById(R.id.textReseñaDetalle);

        // Obtener datos pasados desde MainActivity
        Intent intent = getIntent();
        if (intent != null) {
            textTitulo.setText(intent.getStringExtra("titulo"));
            textDirector.setText("Director: " + intent.getStringExtra("director"));
            textAnio.setText("Año: " + intent.getIntExtra("anio", 0));
            textPuntuacion.setText("Puntuación: " + intent.getFloatExtra("puntuacion", 0.0f));
            textReseña.setText(intent.getStringExtra("reseña"));
        }
    }
}
