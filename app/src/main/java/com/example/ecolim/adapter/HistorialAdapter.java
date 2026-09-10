package com.example.ecolim.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecolim.R;

import java.util.List;

/**
 * Muestra cada fila de historial como String[]{idCollection, clientName, locationName, date, status, totalKg}
 * (viene directo de DatabaseHelper.obtenerHistorial()).
 */
public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder> {

    private final List<String[]> filas;

    public HistorialAdapter(List<String[]> filas) {
        this.filas = filas;
    }

    @NonNull
    @Override
    public HistorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historial, parent, false);
        return new HistorialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialViewHolder holder, int position) {
        String[] fila = filas.get(position);
        holder.tvCliente.setText(fila[1]);
        holder.tvEstado.setText(fila[4]);
        holder.tvUbicacion.setText(fila[2]);
        holder.tvFechaKg.setText(fila[3] + " · " + fila[5] + " kg");
    }

    @Override
    public int getItemCount() {
        return filas.size();
    }

    static class HistorialViewHolder extends RecyclerView.ViewHolder {
        TextView tvCliente, tvEstado, tvUbicacion, tvFechaKg;

        HistorialViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCliente = itemView.findViewById(R.id.tvHistCliente);
            tvEstado = itemView.findViewById(R.id.tvHistEstado);
            tvUbicacion = itemView.findViewById(R.id.tvHistUbicacion);
            tvFechaKg = itemView.findViewById(R.id.tvHistFechaKg);
        }
    }
}
