package com.example.arace.menu.reservas.plazas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arace.R;
import com.example.arace.bbdd.DB_Conexion;

import java.util.ArrayList;
import java.util.List;

public class Plazas extends AppCompatActivity {

    RecycleViewAdapterPlazas adaptador;
    List<ListaPlazas> listaplazas=new ArrayList<>();
    String u;
    String d;
    String i;
    String f;
    TextView txtZona;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plazas);

        ImageView imagen=(ImageView) findViewById(R.id.id_imagen);

        DB_Conexion dbC=new DB_Conexion(this);
        SQLiteDatabase db= dbC.getWritableDatabase();
        String z=getIntent().getStringExtra("Zona");
        Log.d("Plazas", "Zona: " + z);
        RecyclerView vp=(RecyclerView) findViewById(R.id.idPlazas);
         u=getIntent().getStringExtra("NUsuario");
        Log.d("Solicitud_Reserva", "Nombre de usuario: " + u);
        d=getIntent().getStringExtra("Fecha");
        i=getIntent().getStringExtra("Inicio");
        f=getIntent().getStringExtra("Final");
        Log.d("Plazas", "Fecha "+ d);
        Log.d("Plazas", "Inicio "+ i);
        Log.d("Plazas", "Fin "+ f);
        vp.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        txtZona=findViewById(R.id.txtZona);
        Cursor fila = db.rawQuery("SELECT Plaza.Id_Plaza, Lugar, Disponibilidad FROM Plaza " +
                "WHERE Id_Zona= (SELECT Id_Zona FROM Zona WHERE Nombre='"+z+"');", null);

        txtZona.setText(z);
        //Esto sirve que dependiendo del tipo de la zona se cambie la imagen
        if (z != null) {
            switch (z) {
                case "Zona Entrada":
                    imagen.setImageResource(R.drawable.entrada);
                    break;
                case "Zona Cafetería":
                    imagen.setImageResource(R.drawable.cafeteria);
                    break;
                case "Zona hotel-escuela":
                    imagen.setImageResource(R.drawable.hotelescuela);
                    break;
                case "Zona Pabellón":
                    imagen.setImageResource(R.drawable.pabellon);
                    break;
                case "Zona jardín":
                    imagen.setImageResource(R.drawable.jardineria);
                    break;
                case "Zona para Alumnos":
                    imagen.setImageResource(R.drawable.alumnos);
                    break;
                case "Zona residencia de estudiantes":
                    imagen.setImageResource(R.drawable.residencia);
                    break;
            }
        }
        if(fila!=null ){

            while(fila.moveToNext()){
                ListaPlazas lp=new ListaPlazas();
                lp.setPlaza(fila.getInt(0));
                lp.setLugar(fila.getString(1));
                lp.setDisponibilidad(fila.getString(2));
                listaplazas.add(lp);
                Log.d("Plazas", "Plaza: " + lp.getPlaza());
            }
        }
        fila.close();
        adaptador=new RecycleViewAdapterPlazas(Plazas.this, listaplazas,u,d,i,f);
        vp.setAdapter(adaptador);
    }

}