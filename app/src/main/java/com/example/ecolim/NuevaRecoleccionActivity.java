package com.example.ecolim;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecolim.model.RecoleccionSession;
import com.example.ecolim.util.DateUtil;

import java.util.Calendar;
import java.util.Locale;

/** Pantalla 5: datos generales de la recolección (cliente/ubicación ya vienen de las pantallas 3 y 4). */
public class NuevaRecoleccionActivity extends AppCompatActivity {

    private TextView tvClienteElegido, tvUbicacionElegida, tvFecha, tvHoraInicio;
    private EditText etObservaciones;
    private Button btnAgregarResiduos;

    private final Calendar calendarioFecha = Calendar.getInstance();
    private final Calendar calendarioHora = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_recoleccion);

        tvClienteElegido = findViewById(R.id.tvClienteElegido);
        tvUbicacionElegida = findViewById(R.id.tvUbicacionElegida);
        tvFecha = findViewById(R.id.tvFecha);
        tvHoraInicio = findViewById(R.id.tvHoraInicio);
        etObservaciones = findViewById(R.id.etObservaciones);
        btnAgregarResiduos = findViewById(R.id.btnAgregarResiduos);

        TextView btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());

        LinearLayout rowCliente = findViewById(R.id.rowCliente);
        LinearLayout rowUbicacion = findViewById(R.id.rowUbicacion);
        rowCliente.setOnClickListener(v -> {
            startActivity(new Intent(this, ClientesActivity.class));
            finish();
        });
        rowUbicacion.setOnClickListener(v -> {
            startActivity(new Intent(this, UbicacionesActivity.class));
            finish();
        });

        actualizarTextoFecha();
        actualizarTextoHora();

        tvFecha.setOnClickListener(v -> new DatePickerDialog(this, (view, year, month, day) -> {
            calendarioFecha.set(year, month, day);
            actualizarTextoFecha();
        }, calendarioFecha.get(Calendar.YEAR), calendarioFecha.get(Calendar.MONTH), calendarioFecha.get(Calendar.DAY_OF_MONTH)).show());

        tvHoraInicio.setOnClickListener(v -> new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            calendarioHora.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendarioHora.set(Calendar.MINUTE, minute);
            actualizarTextoHora();
        }, calendarioHora.get(Calendar.HOUR_OF_DAY), calendarioHora.get(Calendar.MINUTE), false).show());

        btnAgregarResiduos.setOnClickListener(v -> continuar());
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecoleccionSession session = RecoleccionSession.getInstance();
        tvClienteElegido.setText(session.clientName != null ? session.clientName : "Seleccionar cliente");
        tvUbicacionElegida.setText(session.locationName != null ? session.locationName : "Selecciona un cliente primero");
    }

    private void actualizarTextoFecha() {
        tvFecha.setText(String.format(Locale.getDefault(), "%02d/%02d/%04d",
                calendarioFecha.get(Calendar.DAY_OF_MONTH), calendarioFecha.get(Calendar.MONTH) + 1, calendarioFecha.get(Calendar.YEAR)));
    }

    private void actualizarTextoHora() {
        tvHoraInicio.setText(String.format(Locale.getDefault(), "%02d:%02d",
                calendarioHora.get(Calendar.HOUR_OF_DAY), calendarioHora.get(Calendar.MINUTE)));
    }

    private void continuar() {
        RecoleccionSession session = RecoleccionSession.getInstance();
        if (session.idLocation == 0) {
            Toast.makeText(this, "Selecciona cliente y ubicación", Toast.LENGTH_SHORT).show();
            return;
        }
        session.date = DateUtil.isoFromDate(calendarioFecha.getTime());
        session.startTime = tvHoraInicio.getText().toString();
        session.observations = etObservaciones.getText().toString().trim();

        startActivity(new Intent(NuevaRecoleccionActivity.this, AgregarResiduosActivity.class));
    }
}
