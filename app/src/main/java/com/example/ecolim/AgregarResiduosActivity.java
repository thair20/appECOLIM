package com.example.ecolim;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecolim.adapter.ResiduoAdapter;
import com.example.ecolim.model.DetalleItem;
import com.example.ecolim.model.RecoleccionSession;
import com.example.ecolim.model.WasteType;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Pantalla 6 (nueva, no existía en el código original): elegir tipos de residuo y sus cantidades. */
public class AgregarResiduosActivity extends AppCompatActivity {

    private EditText etBuscarResiduo;
    private RecyclerView rvTiposResiduo;
    private TextView tvResumenAgregados;
    private Button btnContinuarResumen;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_residuos);

        dbHelper = new DatabaseHelper(this);
        etBuscarResiduo = findViewById(R.id.etBuscarResiduo);
        rvTiposResiduo = findViewById(R.id.rvTiposResiduo);
        rvTiposResiduo.setLayoutManager(new LinearLayoutManager(this));
        tvResumenAgregados = findViewById(R.id.tvResumenAgregados);
        btnContinuarResumen = findViewById(R.id.btnContinuarResumen);

        TextView btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());

        btnContinuarResumen.setOnClickListener(v ->
                startActivity(new Intent(AgregarResiduosActivity.this, ResumenRecoleccionActivity.class)));

        etBuscarResiduo.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { cargarTipos(s.toString()); }
            @Override public void afterTextChanged(Editable s) { }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarTipos(etBuscarResiduo.getText().toString());
        actualizarResumen();
    }

    private void cargarTipos(String filtro) {
        List<WasteType> tipos = dbHelper.obtenerTiposResiduo(filtro);
        rvTiposResiduo.setAdapter(new ResiduoAdapter(tipos, mapaAgregados(), this::abrirDetalle));
    }

    private Map<Long, Double> mapaAgregados() {
        Map<Long, Double> mapa = new HashMap<>();
        for (DetalleItem item : RecoleccionSession.getInstance().items) {
            Double actual = mapa.get(item.idWasteType);
            mapa.put(item.idWasteType, (actual == null ? 0 : actual) + item.quantity);
        }
        return mapa;
    }

    private void abrirDetalle(WasteType tipo) {
        Intent intent = new Intent(AgregarResiduosActivity.this, DetalleResiduoActivity.class);
        intent.putExtra("ID_WASTE_TYPE", tipo.idWasteType);
        intent.putExtra("NOMBRE_WASTE_TYPE", tipo.name);
        intent.putExtra("CATEGORIA_WASTE_TYPE", tipo.categoryName);
        startActivity(intent);
    }

    private void actualizarResumen() {
        RecoleccionSession session = RecoleccionSession.getInstance();
        int cantidad = session.items.size();
        double total = session.getTotalKg();
        tvResumenAgregados.setText(String.format(Locale.getDefault(), "%d residuo%s agregado%s · Total: %.1f kg",
                cantidad, cantidad == 1 ? "" : "s", cantidad == 1 ? "" : "s", total));
        btnContinuarResumen.setEnabled(cantidad > 0);
    }
}
