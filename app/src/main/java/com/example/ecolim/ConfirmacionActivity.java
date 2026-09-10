package com.example.ecolim;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecolim.model.RecoleccionSession;

import java.util.Calendar;
import java.util.Locale;

/** Pantalla 9: guarda la recolección en SQLite (transacción) y muestra el código generado. */
public class ConfirmacionActivity extends AppCompatActivity {

    private TextView tvCodigoRecoleccion, tvTotalKgConfirmacion;
    private Button btnVerDetalle, btnFinalizarRegistrar;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacion);

        dbHelper = new DatabaseHelper(this);
        tvCodigoRecoleccion = findViewById(R.id.tvCodigoRecoleccion);
        tvTotalKgConfirmacion = findViewById(R.id.tvTotalKgConfirmacion);
        btnVerDetalle = findViewById(R.id.btnVerDetalle);
        btnFinalizarRegistrar = findViewById(R.id.btnFinalizarRegistrar);

        guardarYMostrar();

        btnVerDetalle.setOnClickListener(v -> {
            startActivity(new Intent(ConfirmacionActivity.this, HistorialActivity.class));
        });

        btnFinalizarRegistrar.setOnClickListener(v -> {
            Intent intent = new Intent(ConfirmacionActivity.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void guardarYMostrar() {
        RecoleccionSession session = RecoleccionSession.getInstance();

        long idCollection = dbHelper.guardarRecoleccion(
                session.idEmployee, session.idLocation, session.date,
                session.startTime, session.observations, session.items);

        double total = session.getTotalKg();

        if (idCollection == -1) {
            Toast.makeText(this, "Error al registrar la recolección en la base de datos", Toast.LENGTH_LONG).show();
            tvCodigoRecoleccion.setText("REC-ERROR");
        } else {
            int anio = Calendar.getInstance().get(Calendar.YEAR);
            String codigo = String.format(Locale.getDefault(), "REC-%d-%06d", anio, idCollection);
            tvCodigoRecoleccion.setText(codigo);
            Toast.makeText(this, "¡Recolección enviada y registrada con éxito!", Toast.LENGTH_SHORT).show();
        }

        tvTotalKgConfirmacion.setText(String.format(Locale.getDefault(), "%.1f kg", total));

        // Ya se guardó: limpiamos la sesión en memoria para la siguiente recolección.
        session.reset();
    }
}
