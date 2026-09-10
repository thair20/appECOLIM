package com.example.ecolim;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/** Pantalla bonus (no pedida por el mockup de 10 pantallas): configuración básica. */
public class ConfiguracionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        TextView btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());

        Button btnCerrarSesionConfig = findViewById(R.id.btnCerrarSesionConfig);
        btnCerrarSesionConfig.setOnClickListener(v -> {
            getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE).edit().clear().apply();
            Intent intent = new Intent(ConfiguracionActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
