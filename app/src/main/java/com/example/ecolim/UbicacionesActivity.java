package com.example.ecolim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecolim.adapter.UbicacionAdapter;
import com.example.ecolim.model.Location;
import com.example.ecolim.model.RecoleccionSession;

import java.util.List;

/** Pantalla 4 (nueva, no existía en el código original): ubicaciones del cliente elegido. */
public class UbicacionesActivity extends AppCompatActivity {

    private RecyclerView rvUbicaciones;
    private TextView tvClienteSeleccionado, tvSinUbicaciones;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicaciones);

        dbHelper = new DatabaseHelper(this);
        rvUbicaciones = findViewById(R.id.rvUbicaciones);
        rvUbicaciones.setLayoutManager(new LinearLayoutManager(this));
        tvClienteSeleccionado = findViewById(R.id.tvClienteSeleccionado);
        tvSinUbicaciones = findViewById(R.id.tvSinUbicaciones);

        TextView btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());

        RecoleccionSession session = RecoleccionSession.getInstance();
        tvClienteSeleccionado.setText("Cliente: " + session.clientName);

        List<Location> ubicaciones = dbHelper.obtenerUbicaciones(session.idClient);
        if (ubicaciones.isEmpty()) {
            tvSinUbicaciones.setVisibility(View.VISIBLE);
            rvUbicaciones.setVisibility(View.GONE);
        } else {
            rvUbicaciones.setAdapter(new UbicacionAdapter(ubicaciones, this::seleccionarUbicacion));
        }

        LinearLayout navInicio = findViewById(R.id.navInicio);
        LinearLayout navReportes = findViewById(R.id.navReportes);
        LinearLayout navMas = findViewById(R.id.navMas);
        navInicio.setOnClickListener(v -> { startActivity(new Intent(this, DashboardActivity.class)); finish(); });
        navReportes.setOnClickListener(v -> startActivity(new Intent(this, ReportesActivity.class)));
        navMas.setOnClickListener(v -> startActivity(new Intent(this, ConfiguracionActivity.class)));
    }

    private void seleccionarUbicacion(Location ubicacion) {
        RecoleccionSession session = RecoleccionSession.getInstance();
        session.idLocation = ubicacion.idLocation;
        session.locationName = ubicacion.name;

        startActivity(new Intent(UbicacionesActivity.this, NuevaRecoleccionActivity.class));
    }
}
