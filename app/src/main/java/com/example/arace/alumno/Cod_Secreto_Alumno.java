package com.example.arace.alumno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.arace.R;
import com.example.arace.bbdd.DB_Conexion;

public class Cod_Secreto_Alumno extends AppCompatActivity {
    Button btnCS;
    EditText TXT_USR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cod_secreto_alumno);
        TXT_USR=(EditText) findViewById(R.id.txtCodseg);
        btnCS=(Button) findViewById(R.id.btnEnviar);
        DB_Conexion dbC=new DB_Conexion(this);
        SQLiteDatabase db= dbC.getWritableDatabase();
        btnCS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String V_USR = TXT_USR.getText().toString();

                if (!V_USR.isEmpty()) {

                    Cursor fila = db.rawQuery("SELECT CodigoSecreto FROM Usuarios WHERE CodigoSecreto= '" + V_USR + "'", null);

                    if (fila.moveToFirst()) {
                        Intent i = new Intent(Cod_Secreto_Alumno.this, Cambio_Contrasena_Alumno.class);
                        i.putExtra("Usuario",V_USR);
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), "Codigo secreto incorrecto ", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Campo vacio ", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
    public void onClick(View view) {

        Intent e=new Intent(Cod_Secreto_Alumno.this, Inicio_Sesion_Alumno.class);
        startActivity(e);
    }
}
