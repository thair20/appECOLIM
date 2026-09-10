package com.example.ecolim;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class HistorialActivity extends AppCompatActivity {

    private ListView lvHistorial;
    private DatabaseHelper dbHelper;
    private ArrayList<String> listaHistorial;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial); // Asegúrate de que el XML tenga un ListView con id @+id/lvHistorial

        lvHistorial = findViewById(R.id.lvHistorial);
        dbHelper = new DatabaseHelper(this);
        listaHistorial = new ArrayList<>();

        cargarDatosHistorial();
    }

    private void cargarDatosHistorial() {
        Cursor cursor = dbHelper.obtenerHistorial();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No hay recolecciones registradas aún", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                String cliente = cursor.getString(1); // Columna cliente
                String kilos = cursor.getString(2);    // Columna kilos
                String fecha = cursor.getString(3);    // Columna fecha

                String registro = "Cliente: " + cliente + "\nKilos: " + kilos + " kg\nFecha: " + fecha;
                listaHistorial.add(registro);
            }

            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaHistorial);
            lvHistorial.setAdapter(adapter);
        }
    }
}