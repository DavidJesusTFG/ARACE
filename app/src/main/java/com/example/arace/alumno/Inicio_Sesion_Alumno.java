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
import com.example.arace.menu.Menu_Principal;

public class Inicio_Sesion_Alumno extends AppCompatActivity {
    private EditText TXT_USR, TXT_PAS;

    private Button btnIniSes;
    private Button btnOlCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion_alumno);
        btnIniSes=(Button) findViewById(R.id.btnInSesion);
        btnOlCon=(Button) findViewById(R.id.btnOlContraseña);
        TXT_USR=(EditText) findViewById(R.id.txtExpediente);
        TXT_PAS=(EditText) findViewById(R.id.txtContraseñaNueva);

        DB_Conexion dbC=new DB_Conexion(this);
        SQLiteDatabase db= dbC.getWritableDatabase();

        btnIniSes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String V_USR = TXT_USR.getText().toString();
                String V_PAS = TXT_PAS.getText().toString();
                if (!V_USR.isEmpty() && !V_PAS.isEmpty()) {
                    Cursor fila = db.rawQuery("SELECT * FROM Usuarios WHERE Usuario= '" + V_USR + "' AND Contraseña= '" + V_PAS + "' AND Tipo_Usuario= 'Alumno'", null);

                    if (fila.moveToFirst()) {
                        Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Inicio_Sesion_Alumno.this, Menu_Principal.class);
                        i.putExtra("Usuario",V_USR);
                        i.putExtra("Tipo_Usuario","Alumno");
                        startActivity(i);
                        TXT_USR.setText("");
                        TXT_PAS.setText("");
                        fila.close();
                        db.close();
                    } else {
                        Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrecto", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Debes introducir Usuario y contraseña", Toast.LENGTH_LONG).show();
                }

            }


        });

        btnOlCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent e= new Intent(Inicio_Sesion_Alumno.this, Cod_Secreto_Alumno.class);
                startActivity(e);
            }

        });
        }
    }

//Esta condicion es de uso temporal debe ser sustituido con la implementacion de la bbdd
                /*
                if (V_USR.equals("alumno") && V_PAS.equals("1") ) {

                    Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Inicio_Sesion_Alumno.this, Menu_Alumno.class);
                    i.putExtra("Usuario",V_USR);
                    startActivity(i);
                    TXT_USR.setText("");
                    TXT_PAS.setText("");

                } else if (!V_USR.equals("alumno") && !V_PAS.equals("1") ) {
                    Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrecto", Toast.LENGTH_LONG).show();

                }else if(V_USR.isEmpty() && V_PAS.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Debes introducir Usuario y contraseña", Toast.LENGTH_LONG).show();
                }


                 */