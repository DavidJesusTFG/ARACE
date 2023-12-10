package com.example.arace;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.arace.bbdd.DB_Conexion;
import com.example.arace.menu.reservas.Calendario;


public class Bienvenida extends Fragment {

    String u;
    String d;
    String t;
    Button solicitud;
    TextView Usuario;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bienvenida, container, false);
        solicitud = view.findViewById(R.id.btnReservar);
        DB_Conexion dbC = new DB_Conexion(getActivity());
        SQLiteDatabase db = dbC.getReadableDatabase();
        //Agarra el numbre y el tipo de usuario
        if (getActivity() != null) {
            try {
                Intent intent = getActivity().getIntent();
                if (u == null && t==null) {
                    u = intent.getStringExtra("Usuario");
                    t=intent.getStringExtra("Tipo_Usuario");
                }
                //Esto es para recuperar el usuario al cambiar de activity/fragment
                if (getActivity() != null) {
                    try {
                        Intent i = getActivity().getIntent();
                        d = i.getStringExtra("Dev_U");
                    } catch (Exception e) {
                        Log.e("Error", "Error al obtener el extra 'Usuario': " + e.getMessage());
                    }
                }
                Log.d("Bienvenida", "Nombre de usuario : " + u);
                Log.d("Bienvenida", "Nombre de usuario devuelto: " + d);
                //Si el usuario devuelto es nulo...
                if(u==null){
                    u=d;
                }
                try {
                    Cursor rec_t = db.rawQuery("SELECT Tipo_usuario FROM Usuarios WHERE Usuario='" + u + "';", null);
                    if (rec_t.moveToFirst()) {
                        t = rec_t.getString(0);
                        rec_t.close();
                    }
                }catch (Exception e) {
                    Log.e("Error", "Error al obtener el extra 'Usuario': " + e.getMessage());
                }
                Log.d("Bienvenida", "Nombre de usuario : " + u);
            } catch (Exception e) {
                Log.e("Error", "Error al obtener el extra 'Usuario': " + e.getMessage());
            }
        }
        Log.d("Bienvenida", "Tipo de usuario: " + t);
        Log.d("Bienvenida", "Nombre usuario: " + u);
        Usuario=view.findViewById(R.id.txtUsuario);
        Usuario.setText(u);
        solicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), Calendario.class);
                i.putExtra("Usuario",u );
                i.putExtra("Tipo",t);
                startActivity(i);
            }
        });
        return view;
    }
}