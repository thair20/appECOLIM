package com.example.ecolim;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecolim.model.Client;
import com.example.ecolim.model.WasteType;
import com.example.ecolim.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/** Pantalla 10: reportes automáticos filtrados por fecha, cliente y tipo de residuo. */
public class ReportesActivity extends AppCompatActivity {

    private TextView tvFechaInicioReporte, tvFechaFinReporte, tvTotalReporte;
    private Spinner spinnerClienteReporte, spinnerTipoResiduoReporte;
    private Button btnGenerarReporte;
    private LinearLayout contenedorResultadosReporte;
    private DatabaseHelper dbHelper;

    private final Calendar calInicio = Calendar.getInstance();
    private final Calendar calFin = Calendar.getInstance();

    private List<Client> clientes;
    private List<WasteType> tiposResiduo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);

        dbHelper = new DatabaseHelper(this);
        tvFechaInicioReporte = findViewById(R.id.tvFechaInicioReporte);
        tvFechaFinReporte = findViewById(R.id.tvFechaFinReporte);
        tvTotalReporte = findViewById(R.id.tvTotalReporte);
        spinnerClienteReporte = findViewById(R.id.spinnerClienteReporte);
        spinnerTipoResiduoReporte = findViewById(R.id.spinnerTipoResiduoReporte);
        btnGenerarReporte = findViewById(R.id.btnGenerarReporte);
        contenedorResultadosReporte = findViewById(R.id.contenedorResultadosReporte);

        TextView btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());

        // Rango por defecto: del primer día del mes actual a hoy (como en el mockup).
        calInicio.set(Calendar.DAY_OF_MONTH, 1);
        actualizarTextoFecha(tvFechaInicioReporte, calInicio);
        actualizarTextoFecha(tvFechaFinReporte, calFin);

        tvFechaInicioReporte.setOnClickListener(v -> mostrarSelectorFecha(calInicio, tvFechaInicioReporte));
        tvFechaFinReporte.setOnClickListener(v -> mostrarSelectorFecha(calFin, tvFechaFinReporte));

        cargarSpinners();

        btnGenerarReporte.setOnClickListener(v -> generarReporte());

        LinearLayout navInicio = findViewById(R.id.navInicio);
        LinearLayout navRecolecciones = findViewById(R.id.navRecolecciones);
        LinearLayout navMas = findViewById(R.id.navMas);
        navInicio.setOnClickListener(v -> { startActivity(new Intent(this, DashboardActivity.class)); finish(); });
        navRecolecciones.setOnClickListener(v -> startActivity(new Intent(this, HistorialActivity.class)));
        navMas.setOnClickListener(v -> startActivity(new Intent(this, ConfiguracionActivity.class)));

        generarReporte();
    }

    private void mostrarSelectorFecha(Calendar calendario, TextView destino) {
        new DatePickerDialog(this, (view, year, month, day) -> {
            calendario.set(year, month, day);
            actualizarTextoFecha(destino, calendario);
        }, calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void actualizarTextoFecha(TextView destino, Calendar calendario) {
        destino.setText(DateUtil.displayFromDate(calendario.getTime()));
    }

    private void cargarSpinners() {
        clientes = dbHelper.obtenerClientes(null);
        List<String> nombresClientes = new ArrayList<>();
        nombresClientes.add("Todos");
        for (Client c : clientes) nombresClientes.add(c.name);
        spinnerClienteReporte.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, nombresClientes));

        tiposResiduo = dbHelper.obtenerTiposResiduo(null);
        List<String> nombresTipos = new ArrayList<>();
        nombresTipos.add("Todos");
        for (WasteType wt : tiposResiduo) nombresTipos.add(wt.name);
        spinnerTipoResiduoReporte.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, nombresTipos));
    }

    private void generarReporte() {
        String fechaInicioIso = DateUtil.isoFromDate(calInicio.getTime());
        String fechaFinIso = DateUtil.isoFromDate(calFin.getTime());

        Long idClienteFiltro = null;
        int posCliente = spinnerClienteReporte.getSelectedItemPosition();
        if (posCliente > 0 && clientes != null && posCliente - 1 < clientes.size()) {
            idClienteFiltro = clientes.get(posCliente - 1).idClient;
        }

        Long idTipoFiltro = null;
        int posTipo = spinnerTipoResiduoReporte.getSelectedItemPosition();
        if (posTipo > 0 && tiposResiduo != null && posTipo - 1 < tiposResiduo.size()) {
            idTipoFiltro = tiposResiduo.get(posTipo - 1).idWasteType;
        }

        List<String[]> resultados = dbHelper.generarReporte(fechaInicioIso, fechaFinIso, idClienteFiltro, idTipoFiltro);

        contenedorResultadosReporte.removeAllViews();
        double total = 0;
        LayoutInflater inflater = LayoutInflater.from(this);
        for (String[] fila : resultados) {
            // fila = {collection_date, clientName, locationName, wasteTypeName, quantity, unit}
            View item = inflater.inflate(R.layout.item_reporte, contenedorResultadosReporte, false);
            TextView tvCliente = item.findViewById(R.id.tvReporteCliente);
            TextView tvFecha = item.findViewById(R.id.tvReporteFecha);
            TextView tvUbicacion = item.findViewById(R.id.tvReporteUbicacion);
            TextView tvResiduo = item.findViewById(R.id.tvReporteResiduo);

            tvCliente.setText(fila[1]);
            tvFecha.setText(DateUtil.isoToDisplay(fila[0]));
            tvUbicacion.setText(fila[2]);
            double cantidad = Double.parseDouble(fila[4]);
            total += cantidad;
            tvResiduo.setText(String.format(Locale.getDefault(), "%s · %.1f %s", fila[3], cantidad, fila[5]));

            contenedorResultadosReporte.addView(item);
        }

        tvTotalReporte.setText(String.format(Locale.getDefault(), "Total: %.1f kg en %d registro%s",
                total, resultados.size(), resultados.size() == 1 ? "" : "s"));
    }
}
