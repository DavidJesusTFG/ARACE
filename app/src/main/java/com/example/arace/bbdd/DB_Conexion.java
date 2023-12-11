package com.example.arace.bbdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB_Conexion extends SQLiteOpenHelper {

    Context mcontext;
    String bbdd;
    private static final String DB_NAME="arace.db";
    private static final int DB_VERSION=1;

    public static final String TABLA_USUARIO="Usuarios";
    public static final String TABLA_Reservas="Reservas";
    public static final String TABLA_Plaza="Plaza";
    public static final String TABLA_Zona="Zona";
    public static final String TABLA_Calendario="Calendario";

    public static final String TABLA_USUARIO_CREATE = "CREATE TABLE " + TABLA_USUARIO + " (" +
            "Id_Usuario INTEGER NOT NULL UNIQUE," +
            "Usuario TEXT NOT NULL UNIQUE," +
            "Contraseña TEXT NOT NULL," +
            "CodigoSecreto INTEGER NOT NULL UNIQUE," +
            "Tipo_usuario TEXT," +
            "PRIMARY KEY(Id_Usuario)" +
            ");";

    public static final String TABLA_Reservas_CREATE = "CREATE TABLE " + TABLA_Reservas + " (" +
            "Id_Reserva INTEGER PRIMARY KEY AUTOINCREMENT," +  // Cambiado a Id_Reserva
            "Id_Usuario INTEGER," +
            "Id_Plaza INTEGER," +  // Agregado campo para la plaza
            "Date_Inicio TEXT NOT NULL," +
            "Date_Final TEXT NOT NULL," +
            "Dia TEXT NOT NULL," +
            "FOREIGN KEY(Id_Usuario) REFERENCES Usuarios(Id_Usuario)," +
            "FOREIGN KEY(Id_Plaza) REFERENCES Plaza(Id_Plaza)" +  // Agregada referencia a la tabla Plaza
            ");";

    public static final String TABLA_Plaza_CREATE = "CREATE TABLE " + TABLA_Plaza + " (" +
            "Id_Plaza INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
            "Id_Zona INTEGER NOT NULL," +
            "Lugar TEXT NOT NULL," +
            "Disponibilidad TEXT NOT NULL," +
            "FOREIGN KEY(Id_Zona) REFERENCES " + TABLA_Zona + "(Id_Zona)" +
            ");";

    public static final String TABLA_Zona_CREATE = "CREATE TABLE " + TABLA_Zona + " (" +
            "Id_Zona INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
            "Disponibilidad INTEGER NOT NULL," +
            "Nombre TEXT" +
            ");";
    public static final String TABLA_Calendario_CREATE = "CREATE TABLE " + TABLA_Calendario + " (" +
            "'fecha' TEXT NOT NULL," +
            "'codigo' INTEGER NOT NULL" +
            ");";

    public DB_Conexion( Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TABLA_USUARIO_CREATE);
        db.execSQL(TABLA_Reservas_CREATE);
        db.execSQL(TABLA_Plaza_CREATE);
        db.execSQL(TABLA_Zona_CREATE);
        db.execSQL(TABLA_Calendario_CREATE);


    }



    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){

        db.execSQL("DROP TABLE IF EXISTS "+TABLA_USUARIO);
        db.execSQL("DROP TABLE IF EXISTS "+TABLA_Reservas);
        db.execSQL("DROP TABLE IF EXISTS "+TABLA_Plaza);
        db.execSQL("DROP TABLE IF EXISTS "+TABLA_Zona);
        db.execSQL("DROP TABLE IF EXISTS "+TABLA_Calendario);
        onCreate(db);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLA_USUARIO);
        db.execSQL("DROP TABLE IF EXISTS "+TABLA_Reservas);
        db.execSQL("DROP TABLE IF EXISTS "+TABLA_Plaza);
        db.execSQL("DROP TABLE IF EXISTS "+TABLA_Zona);
        db.execSQL("DROP TABLE IF EXISTS "+TABLA_Calendario);
        onCreate(db);
    }
    // Método para verificar si hay datos en una tabla específica
    public static boolean hayDatosEnTabla(SQLiteDatabase db, String nombreTabla) {
            String query = "SELECT COUNT(*) FROM " + nombreTabla;
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            return count > 0;
    }
    //Checkea primero las plazas que se encuentran disponibles y lo actualiza al contador
    public void actualizarDisponibilidadZona() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlUpdate =  "UPDATE " + TABLA_Zona + " " +
                "SET Disponibilidad = (" +
                "SELECT COUNT(DISTINCT " + TABLA_Plaza + ".Id_Plaza) " +
                "FROM " + TABLA_Plaza + " " +
                "WHERE " + TABLA_Plaza + ".Id_Zona = " + TABLA_Zona + ".Id_Zona " +
                "AND " + TABLA_Plaza + ".Disponibilidad = 'Si');";

        db.execSQL(sqlUpdate);
    }
    public void Insercion(){
        SQLiteDatabase db = this.getWritableDatabase();

        // Insertar datos en la tabla Usuarios
       String sqlUsuarios="INSERT OR REPLACE INTO Usuarios (Id_Usuario, Usuario, Contraseña, CodigoSecreto, Tipo_usuario) VALUES (1, 'jose.sala', '1234', 111, 'Profesor'),\n" +
               "(2, 'david.ortiz', '1234', 222, 'Alumno'),\n" +
               "(3, 'jesus.pedroza', '1234', 333, 'Alumno')," +
               "(4, 'laura.sanchez', '1234', 444, 'Profesor')," +
               "(5, 'agustin.gonzalez', '1234', 555, 'Profesor');";
       db.execSQL(sqlUsuarios);
        // Insertar datos en la tabla Plaza
        String[] sqlPlaza = {
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (1, 'E-1', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (1, 'E-2', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (1, 'E-3', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (2, 'C-1', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (6, 'A-1', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (6, 'A-2', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (6, 'A-3', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (1, 'E-4', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (1, 'E-5', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (1, 'E-6', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (1, 'E-7', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (1, 'E-8', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (1, 'E-9', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (1, 'E-10', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (2, 'C-2', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (2, 'C-3', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (2, 'C-4', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (2, 'C-5', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (2, 'C-6', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (2, 'C-7', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (2, 'C-8', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (2, 'C-9', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (2, 'C-10', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (3, 'B-1', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (3, 'B-2', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (3, 'B-3', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (3, 'B-4', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (3, 'B-5', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (3, 'B-6', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (3, 'B-', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (3, 'B-8', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (3, 'B-9', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (3, 'B-10', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (6, 'A-4', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (6, 'A-5', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (6, 'A-6', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (6, 'A-7', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (6, 'A-8', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (6, 'A-9', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (6, 'A-10', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (4, 'P-1', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (4, 'P-2', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (4, 'P-3', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (4, 'P-4', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (4, 'P-5', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (4, 'P-6', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (4, 'P-7', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (4, 'P-8', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (4, 'P-9', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (4, 'P-10', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (5, 'J-1', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (5, 'J-2', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (5, 'J-3', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (5, 'J-4', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (5, 'J-5', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (5, 'J-6', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (5, 'J-7', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (5, 'J-8', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (5, 'J-9', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (5, 'J-10', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (7, 'RE-1', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (7, 'RE-2', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (7, 'RE-3', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (7, 'RE-4', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (7, 'RE-5', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (7, 'RE-6', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (7, 'RE-7', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (7, 'RE-8', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (7, 'RE-9', 'Si');",
                "INSERT INTO Plaza (Id_Zona, Lugar, Disponibilidad) VALUES (7, 'RE-10', 'Si');"
        };

        for (String sql : sqlPlaza) {
            db.execSQL(sql);
        }
        // Insertar datos en la tabla Zona
        String sqlZona="INSERT INTO Zona (Disponibilidad, Nombre) VALUES (6, 'Zona Entrada'),\n" +
                "(0, 'Zona Cafetería'),\n" +
                "(0, 'Zona hotel-escuela'),\n" +
                "(0, 'Zona Pabellón'),\n" +
                "(0, 'Zona jardín'),\n" +
                "(1, 'Zona para Alumnos'),\n" +
                "(0, 'Zona residencia de estudiantes');";
        db.execSQL(sqlZona);
        String sqlCalendario="INSERT INTO calendario (fecha, codigo) VALUES \n" +
                "('2024-01-01', 1),\n" +
                "('2024-01-02', 1),\n" +
                "('2024-01-03', 1),\n" +
                "('2024-01-04', 1),\n" +
                "('2024-01-05', 1),\n" +
                "('2024-01-06', 1),\n" +
                "('2024-01-07', 1),\n" +
                "('2024-01-08', 0),\n" +
                "('2024-01-09', 0),\n" +
                "('2024-01-10', 0),\n" +
                "('2024-01-11', 0),\n" +
                "('2024-01-12', 0),\n" +
                "('2024-01-13', 1),\n" +
                "('2024-01-14', 1),\n" +
                "('2024-01-15', 0),\n" +
                "('2024-01-16', 0),\n" +
                "('2024-01-17', 0),\n" +
                "('2024-01-18', 0),\n" +
                "('2024-01-19', 0),\n" +
                "('2024-01-20', 1),\n" +
                "('2024-01-21', 1),\n" +
                "('2024-01-22', 0),\n" +
                "('2024-01-23', 0),\n" +
                "('2024-01-24', 0),\n" +
                "('2024-01-25', 0),\n" +
                "('2024-01-26', 0),\n" +
                "('2024-01-27', 1),\n" +
                "('2024-01-28', 1),\n" +
                "('2024-01-29', 0),\n" +
                "('2024-01-30', 0),\n" +
                "('2024-01-31', 0),\n" +
                "---febrero\n" +
                "('2024-02-01', 0),\n" +
                "('2024-02-02', 0),\n" +
                "('2024-02-03', 1),\n" +
                "('2024-02-04', 1),\n" +
                "('2024-02-05', 0),\n" +
                "('2024-02-06', 0),\n" +
                "('2024-02-07', 0),\n" +
                "('2024-02-08', 0),\n" +
                "('2024-02-09', 0),\n" +
                "('2024-02-10', 1),\n" +
                "('2024-02-11', 1),\n" +
                "('2024-02-12', 0),\n" +
                "('2024-02-13', 0),\n" +
                "('2024-02-14', 0),\n" +
                "('2024-02-15', 0),\n" +
                "('2024-02-16', 0),\n" +
                "('2024-02-17', 1),\n" +
                "('2024-02-18', 1),\n" +
                "('2024-02-19', 0),\n" +
                "('2024-02-20', 0),\n" +
                "('2024-02-21', 0),\n" +
                "('2024-02-22', 0),\n" +
                "('2024-02-23', 0),\n" +
                "('2024-02-24', 1),\n" +
                "('2024-02-25', 1),\n" +
                "('2024-02-26', 0),\n" +
                "('2024-02-27', 0),\n" +
                "('2024-02-28', 0),\n" +
                "('2024-02-29', 0),\n" +
                "\n" +
                "----marzo\n" +
                "('2024-03-01', 0),\n" +
                "('2024-03-02', 1),\n" +
                "('2024-03-03', 1),\n" +
                "('2024-03-04', 0),\n" +
                "('2024-03-05', 0),\n" +
                "('2024-03-06', 0),\n" +
                "('2024-03-07', 0),\n" +
                "('2024-03-08', 0),\n" +
                "('2024-03-09', 1),\n" +
                "('2024-03-10', 1),\n" +
                "('2024-03-11', 0),\n" +
                "('2024-03-12', 0),\n" +
                "('2024-03-13', 0),\n" +
                "('2024-03-14', 0),\n" +
                "('2024-03-15', 0),\n" +
                "('2024-03-16', 1),\n" +
                "('2024-03-17', 1),\n" +
                "('2024-03-18', 0),\n" +
                "('2024-03-19', 0),\n" +
                "('2024-03-20', 0),\n" +
                "('2024-03-21', 0),\n" +
                "('2024-03-22', 1),\n" +
                "('2024-03-23', 1),\n" +
                "('2024-03-24', 1),\n" +
                "('2024-03-25', 1),\n" +
                "('2024-03-26', 1),\n" +
                "('2024-03-27', 1),\n" +
                "('2024-03-28', 1),\n" +
                "('2024-03-29', 1),\n" +
                "('2024-03-30', 1),\n" +
                "('2024-03-31', 1),\n" +
                "--- abril\n" +
                "('2024-04-01', 1),\n" +
                "('2024-04-02', 0),\n" +
                "('2024-04-03', 0),\n" +
                "('2024-04-04', 0),\n" +
                "('2024-04-05', 0),\n" +
                "('2024-04-06', 1),\n" +
                "('2024-04-07', 1),\n" +
                "('2024-04-08', 0),\n" +
                "('2024-04-09', 0),\n" +
                "('2024-04-10', 0),\n" +
                "('2024-04-11', 0),\n" +
                "('2024-04-12', 0),\n" +
                "('2024-04-13', 1),\n" +
                "('2024-04-14', 1),\n" +
                "('2024-04-15', 0),\n" +
                "('2024-04-16', 0),\n" +
                "('2024-04-17', 0),\n" +
                "('2024-04-18', 0),\n" +
                "('2024-04-19', 0),\n" +
                "('2024-04-20', 1),\n" +
                "('2024-04-21', 1),\n" +
                "('2024-04-22', 0),\n" +
                "('2024-04-23', 0),\n" +
                "('2024-04-24', 0),\n" +
                "('2024-04-25', 0),\n" +
                "('2024-04-26', 0),\n" +
                "('2024-04-27', 1),\n" +
                "('2024-04-28', 1),\n" +
                "('2024-04-29', 0),\n" +
                "('2024-04-30', 0),\n" +
                "--- mayo\n" +
                "('2024-05-01', 1),\n" +
                "('2024-05-02', 1),\n" +
                "('2024-05-03', 1),\n" +
                "('2024-05-04', 1),\n" +
                "('2024-05-05', 1),\n" +
                "('2024-05-06', 0),\n" +
                "('2024-05-07', 0),\n" +
                "('2024-05-08', 0),\n" +
                "('2024-05-09', 0),\n" +
                "('2024-05-10', 0),\n" +
                "('2024-05-11', 1),\n" +
                "('2024-05-12', 1),\n" +
                "('2024-05-13', 0),\n" +
                "('2024-05-14', 0),\n" +
                "('2024-05-15', 1),\n" +
                "('2024-05-16', 0),\n" +
                "('2024-05-17', 0),\n" +
                "('2024-05-18', 1),\n" +
                "('2024-05-19', 1),\n" +
                "('2024-05-20', 0),\n" +
                "('2024-05-21', 0),\n" +
                "('2024-05-22', 0),\n" +
                "('2024-05-23', 0),\n" +
                "('2024-05-24', 0),\n" +
                "('2024-05-25', 1),\n" +
                "('2024-05-26', 1),\n" +
                "('2024-05-27', 0),\n" +
                "('2024-05-28', 0),\n" +
                "('2024-05-29', 0),\n" +
                "('2024-05-30', 0),\n" +
                "('2024-05-31', 0),\n" +
                "--- junio\n" +
                "('2024-06-01', 1),\n" +
                "('2024-06-02', 1),\n" +
                "('2024-06-03', 0),\n" +
                "('2024-06-04', 0),\n" +
                "('2024-06-05', 0),\n" +
                "('2024-06-06', 0),\n" +
                "('2024-06-07', 0),\n" +
                "('2024-06-08', 1),\n" +
                "('2024-06-09', 1),\n" +
                "('2024-06-10', 0),\n" +
                "('2024-06-11', 0),\n" +
                "('2024-06-12', 0),\n" +
                "('2024-06-13', 0),\n" +
                "('2024-06-14', 0),\n" +
                "('2024-06-15', 1),\n" +
                "('2024-06-16', 1),\n" +
                "('2024-06-17', 0),\n" +
                "('2024-06-18', 0),\n" +
                "('2024-06-19', 0),\n" +
                "('2024-06-20', 0),\n" +
                "('2024-06-21', 0),\n" +
                "('2024-06-22', 1),\n" +
                "('2024-06-23', 1),\n" +
                "('2024-06-24', 0),\n" +
                "('2024-06-25', 0),\n" +
                "('2024-06-26', 0),\n" +
                "('2024-06-27', 0),\n" +
                "('2024-06-28', 0),\n" +
                "('2024-06-29', 1),\n" +
                "('2024-06-30', 1),\n" +
                "--julio\n" +
                "('2024-07-01', 0),\n" +
                "('2024-07-02', 0),\n" +
                "('2024-07-03', 0),\n" +
                "('2024-07-04', 0),\n" +
                "('2024-07-05', 0),\n" +
                "('2024-07-06', 1),\n" +
                "('2024-07-07', 1),\n" +
                "('2024-07-08', 0),\n" +
                "('2024-07-09', 0),\n" +
                "('2024-07-10', 0),\n" +
                "('2024-07-11', 0),\n" +
                "('2024-07-12', 0),\n" +
                "('2024-07-13', 1),\n" +
                "('2024-07-14', 1),\n" +
                "('2024-07-15', 0),\n" +
                "('2024-07-16', 0),\n" +
                "('2024-07-17', 0),\n" +
                "('2024-07-18', 0),\n" +
                "('2024-07-19', 0),\n" +
                "('2024-07-20', 1),\n" +
                "('2024-07-21', 1),\n" +
                "('2024-07-22', 0),\n" +
                "('2024-07-23', 0),\n" +
                "('2024-07-24', 0),\n" +
                "('2024-07-25', 1),\n" +
                "('2024-07-26', 0),\n" +
                "('2024-07-27', 1),\n" +
                "('2024-07-28', 1),\n" +
                "('2024-07-29', 0),\n" +
                "('2024-07-30', 0),\n" +
                "('2024-07-31', 0),\n" +
                "---agosto\n" +
                "('2024-08-01', 0),\n" +
                "('2024-08-02', 0),\n" +
                "('2024-08-03', 1),\n" +
                "('2024-08-04', 1),\n" +
                "('2024-08-05', 0),\n" +
                "('2024-08-06', 0),\n" +
                "('2024-08-07', 0),\n" +
                "('2024-08-08', 0),\n" +
                "('2024-08-09', 0),\n" +
                "('2024-08-10', 1),\n" +
                "('2024-08-11', 1),\n" +
                "('2024-08-12', 0),\n" +
                "('2024-08-13', 0),\n" +
                "('2024-08-14', 0),\n" +
                "('2024-08-15', 1),\n" +
                "('2024-08-16', 0),\n" +
                "('2024-08-17', 1),\n" +
                "('2024-08-18', 1),\n" +
                "('2024-08-19', 0),\n" +
                "('2024-08-20', 0),\n" +
                "('2024-08-21', 0),\n" +
                "('2024-08-22', 0),\n" +
                "('2024-08-23', 0),\n" +
                "('2024-08-24', 1),\n" +
                "('2024-08-25', 1),\n" +
                "('2024-08-26', 0),\n" +
                "('2024-08-27', 0),\n" +
                "('2024-08-28', 0),\n" +
                "('2024-08-29', 0),\n" +
                "('2024-08-30', 0),\n" +
                "('2024-08-31', 1),\n" +
                "---septiembre \n" +
                "\n" +
                "('2024-09-01', 1),\n" +
                "('2024-09-02', 0),\n" +
                "('2024-09-03', 0),\n" +
                "('2024-09-04', 0),\n" +
                "('2024-09-05', 0),\n" +
                "('2024-09-06', 0),\n" +
                "('2024-09-07', 1),\n" +
                "('2024-09-08', 1),\n" +
                "('2024-09-09', 0),\n" +
                "('2024-09-10', 0),\n" +
                "('2024-09-11', 0),\n" +
                "('2024-09-12', 0),\n" +
                "('2024-09-13', 0),\n" +
                "('2024-09-14', 1),\n" +
                "('2024-09-15', 1),\n" +
                "('2024-09-16', 0),\n" +
                "('2024-09-17', 0),\n" +
                "('2024-09-18', 0),\n" +
                "('2024-09-19', 0),\n" +
                "('2024-09-20', 0),\n" +
                "('2024-09-21', 1),\n" +
                "('2024-09-22', 1),\n" +
                "('2024-09-23', 0),\n" +
                "('2024-09-24', 0),\n" +
                "('2024-09-25', 0),\n" +
                "('2024-09-26', 0),\n" +
                "('2024-09-27', 0),\n" +
                "('2024-09-28', 1),\n" +
                "('2024-09-29', 1),\n" +
                "('2024-09-30', 0),\n" +
                "\n" +
                "--cotubre\n" +
                "\n" +
                "('2024-10-01', 0),\n" +
                "('2024-10-02', 0),\n" +
                "('2024-10-03', 0),\n" +
                "('2024-10-04', 0),\n" +
                "('2024-10-05', 1),\n" +
                "('2024-10-06', 1),\n" +
                "('2024-10-07', 0),\n" +
                "('2024-10-08', 0),\n" +
                "('2024-10-09', 0),\n" +
                "('2024-10-10', 0),\n" +
                "('2024-10-11', 0),\n" +
                "('2024-10-12', 1),\n" +
                "('2024-10-13', 1),\n" +
                "('2024-10-14', 0),\n" +
                "('2024-10-15', 0),\n" +
                "('2024-10-16', 0),\n" +
                "('2024-10-17', 0),\n" +
                "('2024-10-18', 0),\n" +
                "('2024-10-19', 1),\n" +
                "('2024-10-20', 1),\n" +
                "('2024-10-21', 0),\n" +
                "('2024-10-22', 0),\n" +
                "('2024-10-23', 0),\n" +
                "('2024-10-24', 0),\n" +
                "('2024-10-25', 0),\n" +
                "('2024-10-26', 1),\n" +
                "('2024-10-27', 1),\n" +
                "('2024-10-28', 0),\n" +
                "('2024-10-29', 0),\n" +
                "('2024-10-30', 0),\n" +
                "('2024-10-31', 0),\n" +
                "\n" +
                "-- Noviembre\n" +
                "\n" +
                "('2024-11-01', 1),\n" +
                "('2024-11-02', 1),\n" +
                "('2024-11-03', 1),\n" +
                "('2024-11-04', 0),\n" +
                "('2024-11-05', 0),\n" +
                "('2024-11-06', 0),\n" +
                "('2024-11-07', 0),\n" +
                "('2024-11-08', 0),\n" +
                "('2024-11-09', 1),\n" +
                "('2024-11-10', 1),\n" +
                "('2024-11-11', 0),\n" +
                "('2024-11-12', 0),\n" +
                "('2024-11-13', 0),\n" +
                "('2024-11-14', 0),\n" +
                "('2024-11-15', 0),\n" +
                "('2024-11-16', 1),\n" +
                "('2024-11-17', 1),\n" +
                "('2024-11-18', 0),\n" +
                "('2024-11-19', 0),\n" +
                "('2024-11-20', 0),\n" +
                "('2024-11-21', 0),\n" +
                "('2024-11-22', 0),\n" +
                "('2024-11-23', 1),\n" +
                "('2024-11-24', 1),\n" +
                "('2024-11-25', 0),\n" +
                "('2024-11-26', 0),\n" +
                "('2024-11-27', 0),\n" +
                "('2024-11-28', 0),\n" +
                "('2024-11-29', 0),\n" +
                "('2024-11-30', 1),\n" +
                "\n" +
                "-- Diciembre\n" +
                "\n" +
                "('2024-12-01', 1),\n" +
                "('2024-12-02', 0),\n" +
                "('2024-12-03', 0),\n" +
                "('2024-12-04', 0),\n" +
                "('2024-12-05', 0),\n" +
                "('2024-12-06', 1),\n" +
                "('2024-12-07', 1),\n" +
                "('2024-12-08', 1),\n" +
                "('2024-12-09', 0),\n" +
                "('2024-12-10', 0),\n" +
                "('2024-12-11', 0),\n" +
                "('2024-12-12', 0),\n" +
                "('2024-12-13', 0),\n" +
                "('2024-12-14', 1),\n" +
                "('2024-12-15', 1),\n" +
                "('2024-12-16', 0),\n" +
                "('2024-12-17', 0),\n" +
                "('2024-12-18', 0),\n" +
                "('2024-12-19', 0),\n" +
                "('2024-12-20', 0),\n" +
                "('2024-12-21', 1),\n" +
                "('2024-12-22', 1),\n" +
                "('2024-12-23', 0),\n" +
                "('2024-12-24', 0),\n" +
                "('2024-12-25', 1),\n" +
                "('2024-12-26', 0),\n" +
                "('2024-12-27', 0),\n" +
                "('2024-12-28', 1),\n" +
                "('2024-12-29', 1),\n" +
                "('2024-12-30', 0),\n" +
                "('2024-12-31', 0);";
        db.execSQL(sqlCalendario);

    }

}
