package com.example.ecolim;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecolim.adapter.HistorialAdapter;
import com.example.ecolim.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

/** Pantalla bonus (no pedida por el mockup de 10 pantallas): historial completo de recolecciones. */
public class HistorialActivity extends AppCompatActivity {

    private RecyclerView rvHistorial;
    private TextView tvSinHistorial;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        dbHelper = new DatabaseHelper(this);
        rvHistorial = findViewById(R.id.rvHistorial);
        rvHistorial.setLayoutManager(new LinearLayoutManager(this));
        tvSinHistorial = findViewById(R.id.tvSinHistorial);

        TextView btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDatosHistorial();
    }

    private void cargarDatosHistorial() {
        List<String[]> filasCrudas = dbHelper.obtenerHistorial();

        if (filasCrudas.isEmpty()) {
            tvSinHistorial.setVisibility(View.VISIBLE);
            rvHistorial.setVisibility(View.GONE);
            return;
        }

        tvSinHistorial.setVisibility(View.GONE);
        rvHistorial.setVisibility(View.VISIBLE);

        // Formateamos la fecha ISO (yyyy-MM-dd) a dd/MM/yyyy antes de mostrarla.
        List<String[]> filas = new ArrayList<>();
        for (String[] fila : filasCrudas) {
            filas.add(new String[]{fila[0], fila[1], fila[2], DateUtil.isoToDisplay(fila[3]), fila[4], fila[5]});
        }

        rvHistorial.setAdapter(new HistorialAdapter(filas));
    }
}
