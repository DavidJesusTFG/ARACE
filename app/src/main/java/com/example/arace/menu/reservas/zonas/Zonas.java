package com.example.arace.menu.reservas.zonas;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.widget.Button;

import com.example.arace.R;
import com.example.arace.bbdd.DB_Conexion;

import java.util.ArrayList;
import java.util.List;


public class Zonas extends AppCompatActivity {

    RecyclerView recyclerView;
    Parcelable recyclerViewState;
    RecycleViewAdapterZonas adaptador;
    List<ListaZonas> listareservas = new ArrayList<>();
    String u;
    String t;
    String d;
    String f;
    String i;
    String l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment__reservas);

        DB_Conexion dbC = new DB_Conexion(this);
        SQLiteDatabase db = dbC.getReadableDatabase();
        verificarDia();

        Intent intent = getIntent();
        u = intent.getStringExtra("Reservausuario");
        t = intent.getStringExtra("Reservatipo");
        f= intent.getStringExtra("Fecha");
        i= intent.getStringExtra("HoraEntrada");
        l= intent.getStringExtra("HoraSalida");
        if (u == null) {
            d = intent.getStringExtra("Dev_U");
            u = (d != null) ? d : "";
        }

        try {
            Cursor rec_t = db.rawQuery("SELECT Tipo_usuario FROM Usuarios WHERE Usuario='" + u + "';", null);
            if (rec_t.moveToFirst()) {
                t = rec_t.getString(0);
                rec_t.close();
            }
        } catch (Exception e) {
            Log.e("Error", "Error al obtener el extra 'Usuario': " + e.getMessage());
        }

        Cursor cursor = null;

        try {
            listareservas.clear();

            if (t.equals("Alumno")) {
                cursor = db.rawQuery("SELECT Nombre, Disponibilidad FROM Zona WHERE Id_Zona=6", null);
            } else if (t.equals("Profesor")) {
                cursor = db.rawQuery("SELECT Nombre, Disponibilidad FROM Zona WHERE NOT Id_Zona=6", null);
            }

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ListaZonas lr = new ListaZonas();
                    lr.setZona(cursor.getString(0));
                    lr.setPlaza(cursor.getInt(1));
                    listareservas.add(lr);

                } while (cursor.moveToNext());
                Log.d("Fragment_Reserva", "Nombre usuario array: " + u);
                Log.d("Reservas", "Tipo"+ t);
                Log.d("Reservas", "Fecha"+ f);
                Log.d("Reservas", "Inicio"+ i);
                Log.d("Reservas", "Final"+ l);
            } else {
                Log.e("Framengo vacio", "null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        recyclerView =  findViewById(R.id.idReservas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adaptador = new RecycleViewAdapterZonas(Zonas.this, listareservas, u,f,i,l);
        recyclerView.setAdapter(adaptador);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("recycler_state", recyclerView.getLayoutManager().onSaveInstanceState());
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (recyclerView != null && recyclerViewState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }
    }
    //Esto lo que hace es verificar si la fecha actual se despues al de una reserva
    private void verificarDia() {
        DB_Conexion dbC = new DB_Conexion(this);
        SQLiteDatabase db = dbC.getWritableDatabase();
        String updatePastReservationsQuery = "UPDATE Plaza SET Disponibilidad='Si' WHERE Disponibilidad='No' AND Id_Plaza IN " +
                "(SELECT Id_Plaza FROM Reservas WHERE Dia <= date('now'));";

        Cursor verfecha=db.rawQuery("SELECT Dia, Date_Inicio, Date_Final FROM Reservas;", null);
        if (verfecha.moveToFirst()) {
            do {
                // Obtener los valores de las columnas para cada fila
                String dia = verfecha.getString(0);
                String dateInicio = verfecha.getString(1);
                String dateFinal = verfecha.getString(2);

                // Realizar operaciones con los datos obtenidos
                // Por ejemplo, imprimir en el registro
                Log.d("Consulta Reservas", "Dia: " + dia + ", Date_Inicio: " + dateInicio + ", Date_Final: " + dateFinal);

                // Aquí puedes realizar más operaciones con los datos según tus necesidades
                if(!dia.equals(f) || (!dateInicio.equals(i) || !dateFinal.equals(l))){

                }
            } while (verfecha.moveToNext());
        } else {
            // Manejar el caso donde no hay reservas en la tabla
            Log.d("Consulta Reservas", "No hay reservas en la tabla");
        }

        // Regla 2: Actualiza la disponibilidad si la fecha y el horario son diferentes de cualquier reserva existente
        String updatePlazaQuery ="UPDATE Plaza SET Disponibilidad = 'Si' WHERE Disponibilidad = 'No' AND Id_Plaza IN " +
                "(SELECT Id_Plaza FROM Reservas WHERE NOT Dia = ?  AND NOT " +
                "((Date_Inicio = ? AND Date_Final = ?) OR NOT (Date_Inicio = ? AND NOT Date_Final = ?)));";

        String updateZonaQuery = "UPDATE Zona SET Disponibilidad = (" +
                "SELECT COUNT(*) FROM Plaza " +
                "WHERE Plaza.Id_Zona = Zona.Id_Zona AND Plaza.Disponibilidad = 'Si'" +
                ") WHERE Id_Zona = (SELECT Id_Zona FROM Plaza)";
        try {
            db.execSQL(updatePastReservationsQuery);
            db.execSQL(updatePlazaQuery, new String[]{f, i, l, i, l});
            db.execSQL(updateZonaQuery);
            Log.d("Reservas", "Actualizacion de datos correcto");

        } catch (SQLException e) {
            Log.e("Reservas", "Error de actualizxacion"+ e.getMessage());
        } finally {
            dbC.close();
        }
    }
}