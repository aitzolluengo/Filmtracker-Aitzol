package com.tzolas.filmtracker.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.tzolas.filmtracker.R;
import com.tzolas.filmtracker.activities.DetallePeliculaActivity;
import com.tzolas.filmtracker.database.entities.Pelicula;
import java.util.List;

public class PeliculaAdapter extends RecyclerView.Adapter<PeliculaAdapter.ViewHolder> {
    private List<Pelicula> peliculas;
    private OnItemClickListener listener;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(Pelicula pelicula);
        void onDeleteClick(Pelicula pelicula);
    }

    public PeliculaAdapter(Context context, List<Pelicula> peliculas, OnItemClickListener listener) {
        this.context = context;
        this.peliculas = peliculas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pelicula, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pelicula pelicula = peliculas.get(position);
        holder.titulo.setText(pelicula.getTitulo());
        holder.director.setText("Dirigida por: " + pelicula.getDirector());
        holder.anio.setText("Año: " + pelicula.getAnio());

        // Abrir pantalla de detalles al hacer clic
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetallePeliculaActivity.class);
            intent.putExtra("titulo", pelicula.getTitulo());
            intent.putExtra("director", pelicula.getDirector());
            intent.putExtra("anio", pelicula.getAnio());
            intent.putExtra("puntuacion", pelicula.getPuntuacion());
            intent.putExtra("reseña", pelicula.getReseña());
            context.startActivity(intent);
        });

        // Eliminar película al hacer clic en el botón
        holder.btnEliminar.setOnClickListener(v -> listener.onDeleteClick(pelicula));
    }

    @Override
    public int getItemCount() {
        return peliculas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, director, anio;
        ImageButton btnEliminar;

        ViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.textTitulo);
            director = itemView.findViewById(R.id.textDirector);
            anio = itemView.findViewById(R.id.textAnio);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
