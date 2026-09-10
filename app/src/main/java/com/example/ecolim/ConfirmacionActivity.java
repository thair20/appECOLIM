package com.example.ecolim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ConfirmacionActivity extends AppCompatActivity {

    private TextView tvResumenCliente, tvResumenFecha, tvResumenResiduo, tvResumenCantidad;
    private Button btnFinalizarRegistrar;
    private DatabaseHelper dbHelper;

    private String cliente, tipoResiduo, cantidad, estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacion);

        dbHelper = new DatabaseHelper(this);

        tvResumenCliente = findViewById(R.id.tvResumenCliente);
        tvResumenFecha = findViewById(R.id.tvResumenFecha);
        tvResumenResiduo = findViewById(R.id.tvResumenResiduo);
        tvResumenCantidad = findViewById(R.id.tvResumenCantidad);
        btnFinalizarRegistrar = findViewById(R.id.btnFinalizarRegistrar);

        cliente = getIntent().getStringExtra("CLIENTE_KEY");
        tipoResiduo = getIntent().getStringExtra("TIPO_RESIDUO_KEY");
        cantidad = getIntent().getStringExtra("CANTIDAD_KEY");
        estado = getIntent().getStringExtra("ESTADO_KEY");

        if (cliente == null) cliente = "Planta Industrial ABC";
        if (tipoResiduo == null) tipoResiduo = "Plástico PET / Industrial";
        if (cantidad == null) cantidad = "150.5";

        String fechaActual = new SimpleDateFormat("dd/MM/yyyy - hh:mm a", Locale.getDefault()).format(new Date());

        if (tvResumenCliente != null) tvResumenCliente.setText("Cliente: " + cliente);
        if (tvResumenFecha != null) tvResumenFecha.setText("Fecha: " + fechaActual);
        if (tvResumenResiduo != null) tvResumenResiduo.setText("Residuo: " + tipoResiduo);
        if (tvResumenCantidad != null) tvResumenCantidad.setText("Cantidad Total: " + cantidad + " kg");

        btnFinalizarRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean guardado = dbHelper.insertarRecoleccion(cliente, cantidad, fechaActual);

                if (guardado) {
                    Toast.makeText(ConfirmacionActivity.this, "¡Recolección enviada y registrada con éxito!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ConfirmacionActivity.this, NuevaRecoleccionActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ConfirmacionActivity.this, "Error al registrar en la base de datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}