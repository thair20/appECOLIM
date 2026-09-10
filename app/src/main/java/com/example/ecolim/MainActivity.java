package com.example.ecolim;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecolim.model.Employee;

/** Pantalla 1: Inicio de sesión. Valida contra la tabla "employees". */
public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "EcolimSession";

    private EditText etUsuario, etPassword;
    private Button btnIniciarSesion;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);

        // Si ya hay una sesión guardada, saltamos directo al Dashboard.
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        long employeeIdGuardado = prefs.getLong("employee_id", -1);
        if (employeeIdGuardado != -1) {
            irADashboard();
            return;
        }

        btnIniciarSesion.setOnClickListener(v -> intentarLogin());
    }

    private void intentarLogin() {
        String dni = etUsuario.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (dni.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Ingresa tu DNI y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        Employee employee = dbHelper.login(dni, password);
        if (employee == null) {
            Toast.makeText(this, "DNI o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit()
                .putLong("employee_id", employee.idEmployee)
                .putString("employee_name", employee.getFullName())
                .putString("employee_role", employee.role)
                .apply();

        Toast.makeText(this, "¡Bienvenido, " + employee.firstName + "!", Toast.LENGTH_SHORT).show();
        irADashboard();
    }

    private void irADashboard() {
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}
