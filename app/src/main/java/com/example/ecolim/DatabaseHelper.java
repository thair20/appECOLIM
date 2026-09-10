package com.example.ecolim;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.ecolim.model.Client;
import com.example.ecolim.model.DetalleItem;
import com.example.ecolim.model.Employee;
import com.example.ecolim.model.Location;
import com.example.ecolim.model.WasteType;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper SQLite con las 9 tablas del modelo de datos ECOLIM S.A.C.
 * (employees, clients, locations, collections, collection_details,
 *  waste_categories, waste_types, disposal_methods, treatment_plants,
 *  disposals).
 * Tipos SQLite usados: BIGSERIAL -> INTEGER PRIMARY KEY AUTOINCREMENT,
 * BOOLEAN -> INTEGER (0/1), NUMERIC(10,2) -> REAL, TIMESTAMP/DATE/TIME -> TEXT.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ecolim.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON");

        db.execSQL("CREATE TABLE employees (" +
                "id_employee INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "dni TEXT UNIQUE, " +
                "first_name TEXT NOT NULL, " +
                "last_name TEXT NOT NULL, " +
                "phone TEXT, " +
                "email TEXT UNIQUE, " +
                "password TEXT NOT NULL, " +
                "role TEXT NOT NULL, " +
                "is_active INTEGER DEFAULT 1, " +
                "created_at TEXT DEFAULT CURRENT_TIMESTAMP)");

        db.execSQL("CREATE TABLE clients (" +
                "id_client INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ruc TEXT UNIQUE, " +
                "name TEXT NOT NULL, " +
                "address TEXT, " +
                "phone TEXT, " +
                "email TEXT, " +
                "contact_person TEXT, " +
                "is_active INTEGER DEFAULT 1, " +
                "created_at TEXT DEFAULT CURRENT_TIMESTAMP)");

        db.execSQL("CREATE TABLE locations (" +
                "id_location INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_client INTEGER NOT NULL, " +
                "name TEXT NOT NULL, " +
                "address TEXT, " +
                "reference TEXT, " +
                "latitude REAL, " +
                "longitude REAL, " +
                "is_active INTEGER DEFAULT 1, " +
                "created_at TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (id_client) REFERENCES clients(id_client))");

        db.execSQL("CREATE TABLE waste_categories (" +
                "id_category INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "description TEXT, " +
                "is_active INTEGER DEFAULT 1)");

        db.execSQL("CREATE TABLE waste_types (" +
                "id_waste_type INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_category INTEGER NOT NULL, " +
                "name TEXT NOT NULL, " +
                "description TEXT, " +
                "is_active INTEGER DEFAULT 1, " +
                "FOREIGN KEY (id_category) REFERENCES waste_categories(id_category))");

        db.execSQL("CREATE TABLE collections (" +
                "id_collection INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_employee INTEGER NOT NULL, " +
                "id_location INTEGER NOT NULL, " +
                "collection_date TEXT NOT NULL, " +
                "start_time TEXT, " +
                "end_time TEXT, " +
                "observations TEXT, " +
                "status TEXT DEFAULT 'COMPLETADA', " +
                "created_at TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (id_employee) REFERENCES employees(id_employee), " +
                "FOREIGN KEY (id_location) REFERENCES locations(id_location))");

        db.execSQL("CREATE TABLE collection_details (" +
                "id_collection_detail INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_collection INTEGER NOT NULL, " +
                "id_waste_type INTEGER NOT NULL, " +
                "quantity REAL NOT NULL, " +
                "unit TEXT DEFAULT 'kg', " +
                "observations TEXT, " +
                "FOREIGN KEY (id_collection) REFERENCES collections(id_collection), " +
                "FOREIGN KEY (id_waste_type) REFERENCES waste_types(id_waste_type))");

        db.execSQL("CREATE TABLE disposal_methods (" +
                "id_disposal_method INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "description TEXT, " +
                "is_active INTEGER DEFAULT 1)");

        db.execSQL("CREATE TABLE treatment_plants (" +
                "id_treatment_plant INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "address TEXT, " +
                "type TEXT, " +
                "phone TEXT, " +
                "is_active INTEGER DEFAULT 1)");

        db.execSQL("CREATE TABLE disposals (" +
                "id_disposal INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_collection_detail INTEGER NOT NULL, " +
                "disposal_date TEXT NOT NULL, " +
                "id_disposal_method INTEGER, " +
                "id_treatment_plant INTEGER, " +
                "quantity REAL NOT NULL, " +
                "unit TEXT DEFAULT 'kg', " +
                "observations TEXT, " +
                "created_at TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (id_collection_detail) REFERENCES collection_details(id_collection_detail), " +
                "FOREIGN KEY (id_disposal_method) REFERENCES disposal_methods(id_disposal_method), " +
                "FOREIGN KEY (id_treatment_plant) REFERENCES treatment_plants(id_treatment_plant))");

        seedData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS disposals");
        db.execSQL("DROP TABLE IF EXISTS disposal_methods");
        db.execSQL("DROP TABLE IF EXISTS treatment_plants");
        db.execSQL("DROP TABLE IF EXISTS collection_details");
        db.execSQL("DROP TABLE IF EXISTS collections");
        db.execSQL("DROP TABLE IF EXISTS waste_types");
        db.execSQL("DROP TABLE IF EXISTS waste_categories");
        db.execSQL("DROP TABLE IF EXISTS locations");
        db.execSQL("DROP TABLE IF EXISTS clients");
        db.execSQL("DROP TABLE IF EXISTS employees");
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // ------------------------------------------------------------------
    // Datos semilla (para que la app se pueda probar y sustentar sin
    // depender de que el usuario cargue todo manualmente)
    // ------------------------------------------------------------------
    private void seedData(SQLiteDatabase db) {
        // Empleado de prueba (login: DNI 12345678 / clave 1234)
        db.execSQL("INSERT INTO employees (dni, first_name, last_name, phone, email, password, role) VALUES " +
                "('12345678','Juan','Pérez','987654321','juan.perez@ecolim.com','1234','Operador de recolección')");

        // Clientes (coinciden con la lista del mockup)
        db.execSQL("INSERT INTO clients (ruc, name, address) VALUES ('20100000001','Planta Industrial ABC','Av. Los Industriales 123')");
        db.execSQL("INSERT INTO clients (ruc, name, address) VALUES ('20100000002','Oficinas Corporativas XYZ','Jr. Lima 456')");
        db.execSQL("INSERT INTO clients (ruc, name, address) VALUES ('20100000003','Almacenes del Sur','Av. El Sol 789')");
        db.execSQL("INSERT INTO clients (ruc, name, address) VALUES ('20100000004','Centro Comercial Plaza','Av. Principal 321')");
        db.execSQL("INSERT INTO clients (ruc, name, address) VALUES ('20100000005','Fábrica Textil San Juan','Carretera Central Km 15')");

        // Ubicaciones de "Planta Industrial ABC" (id_client = 1), como en el mockup
        db.execSQL("INSERT INTO locations (id_client, name, address) VALUES (1,'Área de Producción - Nave 1','Planta Industrial ABC')");
        db.execSQL("INSERT INTO locations (id_client, name, address) VALUES (1,'Almacén General - Nave 2','Planta Industrial ABC')");
        db.execSQL("INSERT INTO locations (id_client, name, address) VALUES (1,'Taller de Mantenimiento - Nave 3','Planta Industrial ABC')");
        db.execSQL("INSERT INTO locations (id_client, name, address) VALUES (1,'Patio de Maniobras - Área Exterior','Planta Industrial ABC')");
        db.execSQL("INSERT INTO locations (id_client, name, address) VALUES (1,'Oficinas Administrativas - Edificio Principal','Planta Industrial ABC')");
        // Al menos una ubicación para cada otro cliente
        db.execSQL("INSERT INTO locations (id_client, name, address) VALUES (2,'Piso 1 - Recepción','Oficinas Corporativas XYZ')");
        db.execSQL("INSERT INTO locations (id_client, name, address) VALUES (3,'Nave de Almacenamiento','Almacenes del Sur')");
        db.execSQL("INSERT INTO locations (id_client, name, address) VALUES (4,'Zona de Foodcourt','Centro Comercial Plaza')");
        db.execSQL("INSERT INTO locations (id_client, name, address) VALUES (5,'Área de Corte y Confección','Fábrica Textil San Juan')");

        // Categorías de residuos
        db.execSQL("INSERT INTO waste_categories (name) VALUES ('Reciclable')");
        db.execSQL("INSERT INTO waste_categories (name) VALUES ('Orgánico')");
        db.execSQL("INSERT INTO waste_categories (name) VALUES ('Peligroso')");
        db.execSQL("INSERT INTO waste_categories (name) VALUES ('Electrónico')");
        db.execSQL("INSERT INTO waste_categories (name) VALUES ('No aprovechable')");

        // Tipos de residuo (coinciden con la lista del mockup "Agregar Residuos")
        db.execSQL("INSERT INTO waste_types (id_category, name) VALUES (1,'Plástico PET')");
        db.execSQL("INSERT INTO waste_types (id_category, name) VALUES (1,'Cartón')");
        db.execSQL("INSERT INTO waste_types (id_category, name) VALUES (1,'Vidrio')");
        db.execSQL("INSERT INTO waste_types (id_category, name) VALUES (2,'Restos de comida')");
        db.execSQL("INSERT INTO waste_types (id_category, name) VALUES (3,'Aceite usado')");
        db.execSQL("INSERT INTO waste_types (id_category, name) VALUES (4,'Residuos electrónicos')");
        db.execSQL("INSERT INTO waste_types (id_category, name) VALUES (1,'Papel')");
        db.execSQL("INSERT INTO waste_types (id_category, name) VALUES (5,'Madera')");

        // Métodos de disposición final
        db.execSQL("INSERT INTO disposal_methods (name) VALUES ('Reciclaje')");
        db.execSQL("INSERT INTO disposal_methods (name) VALUES ('Relleno sanitario')");
        db.execSQL("INSERT INTO disposal_methods (name) VALUES ('Incineración controlada')");
        db.execSQL("INSERT INTO disposal_methods (name) VALUES ('Compostaje')");
        db.execSQL("INSERT INTO disposal_methods (name) VALUES ('Tratamiento especializado')");

        // Plantas de tratamiento
        db.execSQL("INSERT INTO treatment_plants (name, type) VALUES ('Planta de Reciclaje Lima Norte','Reciclaje')");
        db.execSQL("INSERT INTO treatment_plants (name, type) VALUES ('Relleno Sanitario Portillo Grande','Relleno sanitario')");
        db.execSQL("INSERT INTO treatment_plants (name, type) VALUES ('Centro de Tratamiento de Residuos Peligrosos','Peligrosos')");
    }

    // ==================================================================
    // EMPLOYEES
    // ==================================================================
    @Nullable
    public Employee login(String dni, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id_employee, dni, first_name, last_name, phone, email, role, is_active " +
                "FROM employees WHERE dni = ? AND password = ? AND is_active = 1", new String[]{dni, password});
        Employee employee = null;
        if (c.moveToFirst()) {
            employee = new Employee();
            employee.idEmployee = c.getLong(0);
            employee.dni = c.getString(1);
            employee.firstName = c.getString(2);
            employee.lastName = c.getString(3);
            employee.phone = c.getString(4);
            employee.email = c.getString(5);
            employee.role = c.getString(6);
            employee.isActive = c.getInt(7) == 1;
        }
        c.close();
        return employee;
    }

    // ==================================================================
    // CLIENTS
    // ==================================================================
    public List<Client> obtenerClientes(String filtroNombre) {
        List<Client> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT id_client, name, address FROM clients WHERE is_active = 1";
        String[] args = null;
        if (filtroNombre != null && !filtroNombre.trim().isEmpty()) {
            sql += " AND name LIKE ?";
            args = new String[]{"%" + filtroNombre.trim() + "%"};
        }
        sql += " ORDER BY name";
        Cursor c = db.rawQuery(sql, args);
        while (c.moveToNext()) {
            Client cli = new Client();
            cli.idClient = c.getLong(0);
            cli.name = c.getString(1);
            cli.address = c.getString(2);
            lista.add(cli);
        }
        c.close();
        return lista;
    }

    // ==================================================================
    // LOCATIONS
    // ==================================================================
    public List<Location> obtenerUbicaciones(long idClient) {
        List<Location> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id_location, id_client, name, address FROM locations " +
                "WHERE id_client = ? AND is_active = 1 ORDER BY name", new String[]{String.valueOf(idClient)});
        while (c.moveToNext()) {
            Location loc = new Location();
            loc.idLocation = c.getLong(0);
            loc.idClient = c.getLong(1);
            loc.name = c.getString(2);
            loc.address = c.getString(3);
            lista.add(loc);
        }
        c.close();
        return lista;
    }

    // ==================================================================
    // WASTE TYPES (JOIN con waste_categories)
    // ==================================================================
    public List<WasteType> obtenerTiposResiduo(String filtroNombre) {
        List<WasteType> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT wt.id_waste_type, wt.id_category, wt.name, wc.name " +
                "FROM waste_types wt JOIN waste_categories wc ON wt.id_category = wc.id_category " +
                "WHERE wt.is_active = 1";
        String[] args = null;
        if (filtroNombre != null && !filtroNombre.trim().isEmpty()) {
            sql += " AND wt.name LIKE ?";
            args = new String[]{"%" + filtroNombre.trim() + "%"};
        }
        sql += " ORDER BY wc.name, wt.name";
        Cursor c = db.rawQuery(sql, args);
        while (c.moveToNext()) {
            WasteType wt = new WasteType();
            wt.idWasteType = c.getLong(0);
            wt.idCategory = c.getLong(1);
            wt.name = c.getString(2);
            wt.categoryName = c.getString(3);
            lista.add(wt);
        }
        c.close();
        return lista;
    }

    // ==================================================================
    // COLLECTIONS + COLLECTION_DETAILS (guardado transaccional)
    // ==================================================================

    /** Guarda la recolección y todos sus detalles en una sola transacción. Devuelve el id_collection generado, o -1 si falló. */
    public long guardarRecoleccion(long idEmployee, long idLocation, String collectionDate,
                                    String startTime, String observations, List<DetalleItem> items) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        long idCollection = -1;
        try {
            ContentValues cv = new ContentValues();
            cv.put("id_employee", idEmployee);
            cv.put("id_location", idLocation);
            cv.put("collection_date", collectionDate);
            cv.put("start_time", startTime);
            cv.put("observations", observations);
            cv.put("status", "COMPLETADA");
            idCollection = db.insert("collections", null, cv);

            if (idCollection == -1) {
                return -1;
            }

            for (DetalleItem item : items) {
                ContentValues detailCv = new ContentValues();
                detailCv.put("id_collection", idCollection);
                detailCv.put("id_waste_type", item.idWasteType);
                detailCv.put("quantity", item.quantity);
                detailCv.put("unit", item.unit);
                detailCv.put("observations", item.observations);
                long result = db.insert("collection_details", null, detailCv);
                if (result == -1) {
                    return -1;
                }
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return idCollection;
    }

    // ==================================================================
    // DASHBOARD (resumen del día)
    // ==================================================================
    public int contarRecoleccionesDelDia(String fecha) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM collections WHERE collection_date = ? AND status = 'COMPLETADA'", new String[]{fecha});
        int count = 0;
        if (c.moveToFirst()) count = c.getInt(0);
        c.close();
        return count;
    }

    public int contarTiposResiduoDelDia(String fecha) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(DISTINCT cd.id_waste_type) FROM collection_details cd " +
                "JOIN collections co ON cd.id_collection = co.id_collection " +
                "WHERE co.collection_date = ?", new String[]{fecha});
        int count = 0;
        if (c.moveToFirst()) count = c.getInt(0);
        c.close();
        return count;
    }

    public double sumarKilosDelDia(String fecha) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COALESCE(SUM(cd.quantity),0) FROM collection_details cd " +
                "JOIN collections co ON cd.id_collection = co.id_collection " +
                "WHERE co.collection_date = ?", new String[]{fecha});
        double total = 0;
        if (c.moveToFirst()) total = c.getDouble(0);
        c.close();
        return total;
    }

    /** Devuelve un arreglo {clientName, locationName, fecha, hora, status, idCollection} de la última recolección, o null si no hay ninguna. */
    @Nullable
    public String[] obtenerUltimaRecoleccion() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT cl.name, l.name, co.collection_date, co.start_time, co.status, co.id_collection " +
                "FROM collections co " +
                "JOIN locations l ON co.id_location = l.id_location " +
                "JOIN clients cl ON l.id_client = cl.id_client " +
                "ORDER BY co.id_collection DESC LIMIT 1", null);
        String[] result = null;
        if (c.moveToFirst()) {
            result = new String[]{c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5)};
        }
        c.close();
        return result;
    }

    // ==================================================================
    // REPORTES (filtro por fecha, cliente y tipo de residuo)
    // ==================================================================
    public List<String[]> generarReporte(String fechaInicio, String fechaFin, Long idClient, Long idWasteType) {
        List<String[]> resultado = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        StringBuilder sql = new StringBuilder(
                "SELECT co.collection_date, cl.name, l.name, wt.name, cd.quantity, cd.unit " +
                        "FROM collection_details cd " +
                        "JOIN collections co ON cd.id_collection = co.id_collection " +
                        "JOIN locations l ON co.id_location = l.id_location " +
                        "JOIN clients cl ON l.id_client = cl.id_client " +
                        "JOIN waste_types wt ON cd.id_waste_type = wt.id_waste_type " +
                        "WHERE co.collection_date BETWEEN ? AND ?");
        List<String> args = new ArrayList<>();
        args.add(fechaInicio);
        args.add(fechaFin);
        if (idClient != null) {
            sql.append(" AND cl.id_client = ?");
            args.add(String.valueOf(idClient));
        }
        if (idWasteType != null) {
            sql.append(" AND wt.id_waste_type = ?");
            args.add(String.valueOf(idWasteType));
        }
        sql.append(" ORDER BY co.collection_date DESC");

        Cursor c = db.rawQuery(sql.toString(), args.toArray(new String[0]));
        while (c.moveToNext()) {
            resultado.add(new String[]{c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5)});
        }
        c.close();
        return resultado;
    }

    // ==================================================================
    // HISTORIAL (pantalla extra/bonus)
    // ==================================================================
    public List<String[]> obtenerHistorial() {
        List<String[]> resultado = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT co.id_collection, cl.name, l.name, co.collection_date, co.status, " +
                "(SELECT COALESCE(SUM(cd.quantity),0) FROM collection_details cd WHERE cd.id_collection = co.id_collection) " +
                "FROM collections co " +
                "JOIN locations l ON co.id_location = l.id_location " +
                "JOIN clients cl ON l.id_client = cl.id_client " +
                "ORDER BY co.id_collection DESC", null);
        while (c.moveToNext()) {
            resultado.add(new String[]{c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5)});
        }
        c.close();
        return resultado;
    }
}
