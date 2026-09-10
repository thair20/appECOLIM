package com.example.ecolim;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecolim.model.DetalleItem;
import com.example.ecolim.model.RecoleccionSession;
import com.example.ecolim.util.WasteVisuals;

import java.util.Locale;

/** Pantalla 7: cantidad y observaciones de un tipo de residuo específico. */
public class DetalleResiduoActivity extends AppCompatActivity {

    private TextView tvIconoGrande, tvNombreResiduoDetalle, tvCategoriaResiduoDetalle;
    private EditText etCantidadResiduo, etObservacionesResiduo;
    private Button btnMenos, btnMas, btnAgregarResiduoDetalle;

    private long idWasteType;
    private String nombreWasteType;
    private String categoriaWasteType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_residuo);

        tvIconoGrande = findViewById(R.id.tvIconoGrande);
        tvNombreResiduoDetalle = findViewById(R.id.tvNombreResiduoDetalle);
        tvCategoriaResiduoDetalle = findViewById(R.id.tvCategoriaResiduoDetalle);
        etCantidadResiduo = findViewById(R.id.etCantidadResiduo);
        etObservacionesResiduo = findViewById(R.id.etObservacionesResiduo);
        btnMenos = findViewById(R.id.btnMenos);
        btnMas = findViewById(R.id.btnMas);
        btnAgregarResiduoDetalle = findViewById(R.id.btnAgregarResiduoDetalle);

        TextView btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());

        idWasteType = getIntent().getLongExtra("ID_WASTE_TYPE", -1);
        nombreWasteType = getIntent().getStringExtra("NOMBRE_WASTE_TYPE");
        categoriaWasteType = getIntent().getStringExtra("CATEGORIA_WASTE_TYPE");

        tvNombreResiduoDetalle.setText(nombreWasteType);
        tvCategoriaResiduoDetalle.setText(categoriaWasteType);
        tvIconoGrande.setText(WasteVisuals.iconFor(categoriaWasteType));
        tvIconoGrande.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(WasteVisuals.colorFor(this, categoriaWasteType)));

        btnMenos.setOnClickListener(v -> ajustarCantidad(-0.5));
        btnMas.setOnClickListener(v -> ajustarCantidad(0.5));

        btnAgregarResiduoDetalle.setOnClickListener(v -> agregarYVolver());
    }

    private void ajustarCantidad(double delta) {
        double actual = leerCantidad();
        double nueva = Math.max(0.5, actual + delta);
        etCantidadResiduo.setText(String.format(Locale.US, "%.1f", nueva));
    }

    private double leerCantidad() {
        try {
            return Double.parseDouble(etCantidadResiduo.getText().toString().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void agregarYVolver() {
        double cantidad = leerCantidad();
        if (cantidad <= 0) {
            Toast.makeText(this, "Ingresa una cantidad válida", Toast.LENGTH_SHORT).show();
            return;
        }
        if (idWasteType == -1) {
            Toast.makeText(this, "Error: tipo de residuo no reconocido", Toast.LENGTH_SHORT).show();
            return;
        }

        DetalleItem item = new DetalleItem(idWasteType, nombreWasteType, categoriaWasteType, cantidad);
        item.observations = etObservacionesResiduo.getText().toString().trim();
        RecoleccionSession.getInstance().items.add(item);

        Toast.makeText(this, nombreWasteType + " agregado (" + cantidad + " kg)", Toast.LENGTH_SHORT).show();
        finish();
    }
}
