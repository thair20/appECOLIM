package com.example.ecolim;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecolim.adapter.ClienteAdapter;
import com.example.ecolim.model.Client;
import com.example.ecolim.model.RecoleccionSession;

/** Pantalla 3: selección de cliente para iniciar una recolección. */
public class ClientesActivity extends AppCompatActivity {

    private EditText etBuscarCliente;
    private RecyclerView rvClientes;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        dbHelper = new DatabaseHelper(this);
        etBuscarCliente = findViewById(R.id.etBuscarCliente);
        rvClientes = findViewById(R.id.rvClientes);
        rvClientes.setLayoutManager(new LinearLayoutManager(this));

        TextView btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());

        cargarClientes(null);

        etBuscarCliente.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                cargarClientes(s.toString());
            }
            @Override public void afterTextChanged(Editable s) { }
        });

        LinearLayout navInicio = findViewById(R.id.navInicio);
        LinearLayout navReportes = findViewById(R.id.navReportes);
        LinearLayout navMas = findViewById(R.id.navMas);
        navInicio.setOnClickListener(v -> { startActivity(new Intent(this, DashboardActivity.class)); finish(); });
        navReportes.setOnClickListener(v -> startActivity(new Intent(this, ReportesActivity.class)));
        navMas.setOnClickListener(v -> startActivity(new Intent(this, ConfiguracionActivity.class)));
    }

    private void cargarClientes(String filtro) {
        java.util.List<Client> clientes = dbHelper.obtenerClientes(filtro);
        rvClientes.setAdapter(new ClienteAdapter(clientes, this::seleccionarCliente));
    }

    private void seleccionarCliente(Client cliente) {
        RecoleccionSession.getInstance().idClient = cliente.idClient;
        RecoleccionSession.getInstance().clientName = cliente.name;

        Intent intent = new Intent(ClientesActivity.this, UbicacionesActivity.class);
        startActivity(intent);
    }
}
