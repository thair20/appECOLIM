package com.example.ecolim;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ecolim.db";
    private static final int DATABASE_VERSION = 1;

    // Tabla de Clientes
    public static final String TABLE_CLIENTES = "clientes";
    public static final String COLUMN_CLIENTE_ID = "id";
    public static final String COLUMN_CLIENTE_NOMBRE = "nombre";
    public static final String COLUMN_CLIENTE_DIRECCION = "direccion";

    // Tabla de Recolecciones / Historial
    public static final String TABLE_RECOLECCIONES = "recolecciones";
    public static final String COLUMN_RECOLECCION_ID = "id";
    public static final String COLUMN_RECOLECCION_CLIENTE = "cliente";
    public static final String COLUMN_RECOLECCION_KILOS = "kilos";
    public static final String COLUMN_RECOLECCION_FECHA = "fecha";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla Clientes
        String CREATE_CLIENTES_TABLE = "CREATE TABLE " + TABLE_CLIENTES + "("
                + COLUMN_CLIENTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CLIENTE_NOMBRE + " TEXT, "
                + COLUMN_CLIENTE_DIRECCION + " TEXT)";
        db.execSQL(CREATE_CLIENTES_TABLE);

        // Crear tabla Recolecciones
        String CREATE_RECOLECCIONES_TABLE = "CREATE TABLE " + TABLE_RECOLECCIONES + "("
                + COLUMN_RECOLECCION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_RECOLECCION_CLIENTE + " TEXT, "
                + COLUMN_RECOLECCION_KILOS + " TEXT, "
                + COLUMN_RECOLECCION_FECHA + " TEXT)";
        db.execSQL(CREATE_RECOLECCIONES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECOLECCIONES);
        onCreate(db);
    }

    // Método para agregar un cliente
    boolean insertarCliente(String nombre, String direccion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CLIENTE_NOMBRE, nombre);
        values.put(COLUMN_CLIENTE_DIRECCION, direccion);
        long result = db.insert(TABLE_CLIENTES, null, values);
        return result != -1;
    }

    // Método para obtener todos los clientes
    public Cursor obtenerClientes() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_CLIENTES, null);
    }

    // Método para agregar una recolección
    boolean insertarRecoleccion(String cliente, String kilos, String fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECOLECCION_CLIENTE, cliente);
        values.put(COLUMN_RECOLECCION_KILOS, kilos);
        values.put(COLUMN_RECOLECCION_FECHA, fecha);
        long result = db.insert(TABLE_RECOLECCIONES, null, values);
        return result != -1;
    }

    // Método para obtener el historial de recolecciones
    public Cursor obtenerHistorial() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_RECOLECCIONES + " ORDER BY " + COLUMN_RECOLECCION_ID + " DESC", null);
    }
}