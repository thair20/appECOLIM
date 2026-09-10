package com.example.ecolim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvUsuarioBienvenida;
    private Button btnVerClientes, btnNuevaRecoleccion, btnDetalleResiduo, btnConfirmacion, btnHistorial, btnReportes, btnConfiguracion, btnCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvUsuarioBienvenida = findViewById(R.id.tvUsuarioBienvenida);
        btnVerClientes = findViewById(R.id.btnVerClientes);
        btnNuevaRecoleccion = findViewById(R.id.btnNuevaRecoleccion);
        btnDetalleResiduo = findViewById(R.id.btnDetalleResiduo);
        btnConfirmacion = findViewById(R.id.btnConfirmacion);
        btnHistorial = findViewById(R.id.btnHistorial);
        btnReportes = findViewById(R.id.btnReportes);
        btnConfiguracion = findViewById(R.id.btnConfiguracion);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        btnNuevaRecoleccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, NuevaRecoleccionActivity.class);
                startActivity(intent);
            }
        });

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}