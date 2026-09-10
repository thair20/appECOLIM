package com.example.ecolim;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecolim.model.RecoleccionSession;
import com.example.ecolim.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** Pantalla 2: Dashboard con el resumen del día. */
public class DashboardActivity extends AppCompatActivity {

    private TextView tvUsuarioBienvenida, tvRolUsuario, tvFechaHoy;
    private TextView tvRecoleccionesHoy, tvTiposResiduoHoy, tvKilosHoy;
    private TextView tvUltimaCliente, tvUltimaUbicacion, tvUltimaFecha;
    private Button btnNuevaRecoleccion, btnCerrarSesion, btnHistorial, btnConfiguracion;
    private DatabaseHelper dbHelper;

    private long idEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dbHelper = new DatabaseHelper(this);

        tvUsuarioBienvenida = findViewById(R.id.tvUsuarioBienvenida);
        tvRolUsuario = findViewById(R.id.tvRolUsuario);
        tvFechaHoy = findViewById(R.id.tvFechaHoy);
        tvRecoleccionesHoy = findViewById(R.id.tvRecoleccionesHoy);
        tvTiposResiduoHoy = findViewById(R.id.tvTiposResiduoHoy);
        tvKilosHoy = findViewById(R.id.tvKilosHoy);
        tvUltimaCliente = findViewById(R.id.tvUltimaCliente);
        tvUltimaUbicacion = findViewById(R.id.tvUltimaUbicacion);
        tvUltimaFecha = findViewById(R.id.tvUltimaFecha);
        btnNuevaRecoleccion = findViewById(R.id.btnNuevaRecoleccion);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnHistorial = findViewById(R.id.btnHistorial);
        btnConfiguracion = findViewById(R.id.btnConfiguracion);

        SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        idEmployee = prefs.getLong("employee_id", -1);
        if (idEmployee == -1) {
            // No hay sesión activa, regresamos al login.
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        tvUsuarioBienvenida.setText("Hola, " + prefs.getString("employee_name", ""));
        tvRolUsuario.setText(prefs.getString("employee_role", ""));

        btnNuevaRecoleccion.setOnClickListener(v -> {
            RecoleccionSession.getInstance().reset();
            RecoleccionSession.getInstance().idEmployee = idEmployee;
            startActivity(new Intent(DashboardActivity.this, ClientesActivity.class));
        });

        btnCerrarSesion.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            startActivity(new Intent(DashboardActivity.this, MainActivity.class));
            finish();
        });

        btnHistorial.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, HistorialActivity.class)));
        btnConfiguracion.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, ConfiguracionActivity.class)));

        // Barra inferior
        LinearLayout navRecolecciones = findViewById(R.id.navRecolecciones);
        LinearLayout navReportes = findViewById(R.id.navReportes);
        LinearLayout navMas = findViewById(R.id.navMas);
        navRecolecciones.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, HistorialActivity.class)));
        navReportes.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, ReportesActivity.class)));
        navMas.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, ConfiguracionActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarResumenDelDia();
    }

    private void cargarResumenDelDia() {
        String hoyIso = DateUtil.todayIso();
        tvFechaHoy.setText(new SimpleDateFormat("d 'de' MMMM, yyyy", new Locale("es", "PE")).format(new Date()));

        tvRecoleccionesHoy.setText(String.valueOf(dbHelper.contarRecoleccionesDelDia(hoyIso)));
        tvTiposResiduoHoy.setText(String.valueOf(dbHelper.contarTiposResiduoDelDia(hoyIso)));
        tvKilosHoy.setText(String.format(Locale.getDefault(), "%.1f kg", dbHelper.sumarKilosDelDia(hoyIso)));

        String[] ultima = dbHelper.obtenerUltimaRecoleccion();
        if (ultima != null) {
            tvUltimaCliente.setText(ultima[0]);
            tvUltimaUbicacion.setText(ultima[1]);
            tvUltimaFecha.setText(DateUtil.isoToDisplay(ultima[2]) + " · " + ultima[3] + " · " + ultima[4]);
        }
    }
}
