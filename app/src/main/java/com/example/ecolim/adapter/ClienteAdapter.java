package com.example.ecolim.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecolim.R;
import com.example.ecolim.model.Client;

import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder> {

    public interface OnClienteClickListener {
        void onClienteClick(Client cliente);
    }

    private final List<Client> clientes;
    private final OnClienteClickListener listener;

    public ClienteAdapter(List<Client> clientes, OnClienteClickListener listener) {
        this.clientes = clientes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cliente, parent, false);
        return new ClienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        Client cliente = clientes.get(position);
        holder.tvNombre.setText(cliente.name);
        holder.tvDireccion.setText(cliente.address != null ? cliente.address : "");
        holder.itemView.setOnClickListener(v -> listener.onClienteClick(cliente));
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    static class ClienteViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDireccion;

        ClienteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreCliente);
            tvDireccion = itemView.findViewById(R.id.tvDireccionCliente);
        }
    }
}
