package com.example.arace;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.arace.alumno.Inicio_Sesion_Alumno;
import com.example.arace.bbdd.DB_Conexion;
import com.example.arace.menu.Menu_Principal;
import com.example.arace.profesor.Inicio_Sesion_Profesor;

public class MainActivity extends AppCompatActivity {


    Button btnAlumno;
    ;
    Button btnProfesor;
    int a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Realiza conexion con la base de datos
        DB_Conexion dbC=new DB_Conexion(this);
        SQLiteDatabase db= dbC.getWritableDatabase();
        //Verifica si ya existian las tablas en caso contrario las tablas se crean
        if (!DB_Conexion.hayDatosEnTabla(db, "Usuarios")) {
            dbC.Insercion();
        }
        if (!DB_Conexion.hayDatosEnTabla(db, "Zona")) {
            dbC.Insercion();
        }
        if (!DB_Conexion.hayDatosEnTabla(db, "Plaza")) {
            dbC.Insercion();
        }
        if (!DB_Conexion.hayDatosEnTabla(db, "Calendario")) {
            dbC.Insercion();
        }
        //Inicia el conteo de las plazas disponibles por zona
        dbC.actualizarDisponibilidadZona();

        btnAlumno=(Button) findViewById(R.id.btnAlumno);
        btnProfesor=(Button) findViewById(R.id.btnProfesor);
        //Selecciona el tipo de usuario que va a iniciar sesion
        btnProfesor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(MainActivity.this, Inicio_Sesion_Profesor.class);
                startActivity(i);
            }
        });

        btnAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent e=new Intent(MainActivity.this, Inicio_Sesion_Alumno.class);
                startActivity(e);
            }
        });
    }
    //Si pulsa el botón de atras...
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }

}