package com.example.ecolim;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecolim.model.DetalleItem;
import com.example.ecolim.model.RecoleccionSession;
import com.example.ecolim.util.DateUtil;
import com.example.ecolim.util.WasteVisuals;

import java.util.Locale;

/** Pantalla 8 (nueva, no existía en el código original): revisar todo antes de confirmar. */
public class ResumenRecoleccionActivity extends AppCompatActivity {

    private TextView tvResumenClienteUbicacion, tvResumenFechaHora, tvTituloResiduosAgregados, tvTotalKg, tvObservacionesResumen;
    private LinearLayout contenedorResiduosAgregados;
    private Button btnCancelarResumen, btnFinalizarResumen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_recoleccion);

        tvResumenClienteUbicacion = findViewById(R.id.tvResumenClienteUbicacion);
        tvResumenFechaHora = findViewById(R.id.tvResumenFechaHora);
        tvTituloResiduosAgregados = findViewById(R.id.tvTituloResiduosAgregados);
        tvTotalKg = findViewById(R.id.tvTotalKg);
        tvObservacionesResumen = findViewById(R.id.tvObservacionesResumen);
        contenedorResiduosAgregados = findViewById(R.id.contenedorResiduosAgregados);
        btnCancelarResumen = findViewById(R.id.btnCancelarResumen);
        btnFinalizarResumen = findViewById(R.id.btnFinalizarResumen);

        TextView btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());

        mostrarDatos();

        btnCancelarResumen.setOnClickListener(v -> {
            RecoleccionSession.getInstance().reset();
            Intent intent = new Intent(ResumenRecoleccionActivity.this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        btnFinalizarResumen.setOnClickListener(v ->
                startActivity(new Intent(ResumenRecoleccionActivity.this, ConfirmacionActivity.class)));
    }

    private void mostrarDatos() {
        RecoleccionSession session = RecoleccionSession.getInstance();

        tvResumenClienteUbicacion.setText(session.clientName + "\n" + session.locationName);
        tvResumenFechaHora.setText(DateUtil.isoToDisplay(session.date) + " · " + session.startTime);
        tvTituloResiduosAgregados.setText("Residuos agregados (" + session.items.size() + ")");
        tvTotalKg.setText(String.format(Locale.getDefault(), "%.1f kg", session.getTotalKg()));
        tvObservacionesResumen.setText(
                (session.observations == null || session.observations.isEmpty()) ? "Sin observaciones" : session.observations);

        contenedorResiduosAgregados.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        for (DetalleItem item : session.items) {
            android.view.View fila = inflater.inflate(R.layout.item_residuo_agregado, contenedorResiduosAgregados, false);
            TextView tvIcono = fila.findViewById(R.id.tvIconoItem);
            TextView tvNombre = fila.findViewById(R.id.tvNombreItem);
            TextView tvCantidad = fila.findViewById(R.id.tvCantidadItem);

            tvIcono.setText(WasteVisuals.iconFor(item.categoryName));
            tvIcono.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(WasteVisuals.colorFor(this, item.categoryName)));
            tvNombre.setText(item.wasteTypeName);
            tvCantidad.setText(String.format(Locale.getDefault(), "%.1f kg", item.quantity));

            contenedorResiduosAgregados.addView(fila);
        }
    }
}
