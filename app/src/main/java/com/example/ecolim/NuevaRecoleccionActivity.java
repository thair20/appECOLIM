package com.example.ecolim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NuevaRecoleccionActivity extends AppCompatActivity {

    private EditText etClienteRecoleccion, etKilosRecoleccion;
    private Button btnContinuarResiduo;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_recoleccion);

        dbHelper = new DatabaseHelper(this);

        etClienteRecoleccion = findViewById(R.id.etClienteRecoleccion);
        etKilosRecoleccion = findViewById(R.id.etKilosRecoleccion);
        btnContinuarResiduo = findViewById(R.id.btnContinuarResiduo);

        btnContinuarResiduo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cliente = etClienteRecoleccion.getText().toString().trim();
                String kilos = etKilosRecoleccion.getText().toString().trim();

                if (cliente.isEmpty() || kilos.isEmpty()) {
                    Toast.makeText(NuevaRecoleccionActivity.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(NuevaRecoleccionActivity.this, DetalleResiduoActivity.class);
                intent.putExtra("CLIENTE_KEY", cliente);
                intent.putExtra("KILOS_KEY", kilos);
                startActivity(intent);
            }
        });
    }
}