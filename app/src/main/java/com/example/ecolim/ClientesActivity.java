package com.example.ecolim;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ClientesActivity extends AppCompatActivity {

    private EditText etNombreCliente, etDireccionCliente;
    private Button btnGuardarCliente;
    private ListView lvClientes;
    private DatabaseHelper dbHelper;
    private ArrayList<String> listaClientes;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        dbHelper = new DatabaseHelper(this);

        etNombreCliente = findViewById(R.id.etNombreCliente);
        etDireccionCliente = findViewById(R.id.etDireccionCliente);
        btnGuardarCliente = findViewById(R.id.btnGuardarCliente);
        lvClientes = findViewById(R.id.lvClientes);

        cargarListaClientes();

        btnGuardarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = etNombreCliente.getText().toString().trim();
                String direccion = etDireccionCliente.getText().toString().trim();

                if (nombre.isEmpty() || direccion.isEmpty()) {
                    Toast.makeText(ClientesActivity.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean insertado = dbHelper.insertarCliente(nombre, direccion);
                if (insertado) {
                    Toast.makeText(ClientesActivity.this, "Cliente guardado exitosamente", Toast.LENGTH_SHORT).show();
                    etNombreCliente.setText("");
                    etDireccionCliente.setText("");
                    cargarListaClientes(); // Refrescar la lista en pantalla
                } else {
                    Toast.makeText(ClientesActivity.this, "Error al registrar cliente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void cargarListaClientes() {
        listaClientes = new ArrayList<>();
        Cursor cursor = dbHelper.obtenerClientes();

        while (cursor.moveToNext()) {
            String nombre = cursor.getString(1);
            String direccion = cursor.getString(2);
            listaClientes.add(nombre + " - " + direccion);
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaClientes);
        lvClientes.setAdapter(adapter);
    }
}