package com.example.arace.profesor;

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
import com.example.arace.menu.Menu_Principal;
import com.example.arace.bbdd.DB_Conexion;

public class Inicio_Sesion_Profesor extends AppCompatActivity {
    private EditText TXT_USR, TXT_PAS;

    private Button btnIniSes;
    private Button btnOlCon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion_profesor);
        btnIniSes=(Button) findViewById(R.id.btnInSesion2);
        btnOlCon=(Button) findViewById(R.id.btnOlContraseña2);
        TXT_USR=(EditText) findViewById(R.id.txtExpediente2);
        TXT_PAS=(EditText) findViewById(R.id.txtContraseñaNueva);

        DB_Conexion dbC=new DB_Conexion(this);
        SQLiteDatabase db= dbC.getWritableDatabase();
        btnIniSes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String V_USR = TXT_USR.getText().toString();
                String V_PAS = TXT_PAS.getText().toString();
                //Verifica si el usuario y coontraseña es correcto
                if (!V_USR.isEmpty() && !V_PAS.isEmpty()) {
                    Cursor fila = db.rawQuery("SELECT * FROM Usuarios WHERE Usuario= '" + V_USR + "' AND Contraseña= '" + V_PAS + "' AND tipo_usuario= 'Profesor'", null);

                    if (fila.moveToFirst()) {
                        Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Inicio_Sesion_Profesor.this, Menu_Principal.class);
                        //Al ir al menu principal cogemos el nombre del usuario y el tipo de usuario
                        //Es importante hacer esto o en caso contrario no podremos acceder a la informacion
                        //Y alguas funcionalidades no servirán
                        i.putExtra("Usuario",V_USR);
                        i.putExtra("Tipo_Usuario","Profesor");
                        startActivity(i);
                        TXT_USR.setText("");
                        TXT_PAS.setText("");
                        fila.close();

                    } else {
                        Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrecto", Toast.LENGTH_LONG).show();
                    }
                    //Si los campos se encuentran vacios...
                }else{
                    Toast.makeText(getApplicationContext(), "Debes introducir Usuario y contraseña", Toast.LENGTH_LONG).show();
                }
            }
        });
        //En caso de que el usuario se olvide la contraseña accede al siguiente boton
        btnOlCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent e= new Intent(Inicio_Sesion_Profesor.this, Cod_Secreto_Profesor.class);
                startActivity(e);
            }

        });
    }
}