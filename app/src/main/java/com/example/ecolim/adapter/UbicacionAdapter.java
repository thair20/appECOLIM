package com.example.ecolim.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecolim.R;
import com.example.ecolim.model.Location;

import java.util.List;

public class UbicacionAdapter extends RecyclerView.Adapter<UbicacionAdapter.UbicacionViewHolder> {

    public interface OnUbicacionClickListener {
        void onUbicacionClick(Location ubicacion);
    }

    private final List<Location> ubicaciones;
    private final OnUbicacionClickListener listener;

    public UbicacionAdapter(List<Location> ubicaciones, OnUbicacionClickListener listener) {
        this.ubicaciones = ubicaciones;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UbicacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ubicacion, parent, false);
        return new UbicacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UbicacionViewHolder holder, int position) {
        Location ubicacion = ubicaciones.get(position);
        holder.tvNombre.setText(ubicacion.name);
        holder.tvDireccion.setText(ubicacion.address != null ? ubicacion.address : "");
        holder.itemView.setOnClickListener(v -> listener.onUbicacionClick(ubicacion));
    }

    @Override
    public int getItemCount() {
        return ubicaciones.size();
    }

    static class UbicacionViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDireccion;

        UbicacionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreUbicacion);
            tvDireccion = itemView.findViewById(R.id.tvDireccionUbicacion);
        }
    }
}
