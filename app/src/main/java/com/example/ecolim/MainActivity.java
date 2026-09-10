package com.example.ecolim;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etUsuario, etPassword;
    private Button btnIniciarSesion, btnRegistrar;
    private static final String PREFS_NAME = "EcolimPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = etUsuario.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (usuario.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Ingrese usuario y contraseña para registrar", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("saved_user", usuario);
                    editor.putString("saved_password", password);
                    editor.apply();

                    Toast.makeText(MainActivity.this, "¡Usuario registrado con éxito!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuarioIngresado = etUsuario.getText().toString().trim();
                String passwordIngresado = etPassword.getText().toString().trim();

                SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                String usuarioGuardado = preferences.getString("saved_user", "");
                String passwordGuardado = preferences.getString("saved_password", "");

                if (usuarioIngresado.isEmpty() || passwordIngresado.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Complete los campos", Toast.LENGTH_SHORT).show();
                } else if (usuarioIngresado.equals(usuarioGuardado) && passwordIngresado.equals(passwordGuardado)) {
                    Toast.makeText(MainActivity.this, "¡Inicio de sesión exitoso!", Toast.LENGTH_SHORT).show();

                    // ¡ AQUÍ PASAMOS A LA PANTALLA 2 (DASHBOARD) !
                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                    intent.putExtra("usuario_key", usuarioIngresado);
                    startActivity(intent);
                    finish(); // Cierra el login para que al dar atrás no regrese
                } else {
                    Toast.makeText(MainActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}