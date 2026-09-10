package com.example.ecolim.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecolim.R;
import com.example.ecolim.model.WasteType;
import com.example.ecolim.util.WasteVisuals;

import java.util.List;
import java.util.Map;

public class ResiduoAdapter extends RecyclerView.Adapter<ResiduoAdapter.ResiduoViewHolder> {

    public interface OnResiduoClickListener {
        void onResiduoClick(WasteType wasteType);
    }

    private final List<WasteType> tipos;
    private final OnResiduoClickListener listener;
    /** id_waste_type -> kg ya agregados en esta recolección (para mostrar el check en la lista). */
    private final Map<Long, Double> agregadosPorTipo;

    public ResiduoAdapter(List<WasteType> tipos, Map<Long, Double> agregadosPorTipo, OnResiduoClickListener listener) {
        this.tipos = tipos;
        this.agregadosPorTipo = agregadosPorTipo;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ResiduoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tipo_residuo, parent, false);
        return new ResiduoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResiduoViewHolder holder, int position) {
        WasteType tipo = tipos.get(position);
        holder.tvNombre.setText(tipo.name);
        holder.tvCategoria.setText(tipo.categoryName);
        holder.tvIcono.setText(WasteVisuals.iconFor(tipo.categoryName));
        holder.tvIcono.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(WasteVisuals.colorFor(holder.itemView.getContext(), tipo.categoryName)));

        Double yaAgregado = agregadosPorTipo.get(tipo.idWasteType);
        if (yaAgregado != null) {
            holder.tvYaAgregado.setVisibility(View.VISIBLE);
            holder.tvYaAgregado.setText("✓ " + yaAgregado + " kg");
        } else {
            holder.tvYaAgregado.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> listener.onResiduoClick(tipo));
    }

    @Override
    public int getItemCount() {
        return tipos.size();
    }

    static class ResiduoViewHolder extends RecyclerView.ViewHolder {
        TextView tvIcono, tvNombre, tvCategoria, tvYaAgregado;

        ResiduoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIcono = itemView.findViewById(R.id.tvIconoResiduo);
            tvNombre = itemView.findViewById(R.id.tvNombreResiduo);
            tvCategoria = itemView.findViewById(R.id.tvCategoriaResiduo);
            tvYaAgregado = itemView.findViewById(R.id.tvCantidadYaAgregada);
        }
    }
}
