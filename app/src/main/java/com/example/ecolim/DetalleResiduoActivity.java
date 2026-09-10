package com.example.ecolim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class DetalleResiduoActivity extends AppCompatActivity {

    private EditText etTipoResiduo, etCantidadResiduo, etEstadoContencion;
    private Button btnGuardarContinuarResiduo;
    private String clienteRecibido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_residuo); // Asegúrate de que el XML coincida

        clienteRecibido = getIntent().getStringExtra("CLIENTE_KEY");
        if (clienteRecibido == null) {
            clienteRecibido = "Cliente General";
        }

        etTipoResiduo = findViewById(R.id.etTipoResiduo);         // Ajusta el ID según tu XML si es diferente
        etCantidadResiduo = findViewById(R.id.etCantidadResiduo);     // Ajusta el ID según tu XML
        etEstadoContencion = findViewById(R.id.etEstadoContencion);   // Ajusta el ID según tu XML
        btnGuardarContinuarResiduo = findViewById(R.id.btnGuardarContinuarResiduo); // El botón verde de abajo

        btnGuardarContinuarResiduo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tipoResiduo = etTipoResiduo != null ? etTipoResiduo.getText().toString().trim() : "Plástico PET";
                String cantidad = etCantidadResiduo != null ? etCantidadResiduo.getText().toString().trim() : "0";
                String estado = etEstadoContencion != null ? etEstadoContencion.getText().toString().trim() : "Sellado";

                Intent intent = new Intent(DetalleResiduoActivity.this, ConfirmacionActivity.class);
                intent.putExtra("CLIENTE_KEY", clienteRecibido);
                intent.putExtra("TIPO_RESIDUO_KEY", tipoResiduo);
                intent.putExtra("CANTIDAD_KEY", cantidad);
                intent.putExtra("ESTADO_KEY", estado);
                startActivity(intent);
            }
        });
    }
}