package com.example.arace.menu;

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
import android.widget.EditText;
import android.widget.Toast;

import com.example.arace.R;
import com.example.arace.bbdd.DB_Conexion;

public class Fragment_Cambio_De_Contrasena extends Fragment {
    private EditText TXT_ANT_CON,TXT_PAS, TXT_REP_CON;
    private Button btnCambioCon;
    String u;



    public Fragment_Cambio_De_Contrasena() {
        // Required empty public constructor
    }

    public static Fragment_Cambio_De_Contrasena newInstance(String param1, String param2) {
        Fragment_Cambio_De_Contrasena fragment = new Fragment_Cambio_De_Contrasena();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DB_Conexion dbC = new DB_Conexion(getActivity());
        SQLiteDatabase db = dbC.getReadableDatabase();
        if (getActivity() != null) {
            try {
                Intent intent = getActivity().getIntent();
                u = intent.getStringExtra("Usuario");
                // Ahora puedes usar la variable 'u'
            } catch (Exception e) {
                // Manejar cualquier excepción que pueda ocurrir al obtener el extra
                Log.e("Error", "Error al obtener el extra 'Usuario': " + e.getMessage());
            }
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__cambio__de__contrasena, container, false);
        btnCambioCon=(Button) view.findViewById(R.id.btnCambioCon);
        TXT_ANT_CON=(EditText) view.findViewById(R.id.txtContraseñaAntigua);
        TXT_PAS=(EditText) view.findViewById(R.id.txtContraseñaNueva);
        TXT_REP_CON=(EditText) view.findViewById(R.id.txtRepCon2);

        btnCambioCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String V_PAS = TXT_PAS.getText().toString();
                String V_REP_CON = TXT_REP_CON.getText().toString();
                String V_ANT_CON = TXT_ANT_CON.getText().toString();
                //Verifica si la contraseña actual es correcta y si la nueva y su repetición son iguales
                Log.d("Fragment_Cambio_De_Contrasena", "Nombre de usuario: " + u);
                if (V_PAS.isEmpty() || V_REP_CON.isEmpty() || V_ANT_CON.isEmpty()) {
                    Toast.makeText(getActivity(), "Hay campos vacios", Toast.LENGTH_LONG).show();
                } else {
                    Cursor cursor = db.rawQuery("SELECT Contraseña FROM Usuarios WHERE Usuario='" + u + "'",  null);
                    if (cursor.moveToFirst()) {

                        if (!V_ANT_CON.equals(cursor.getString(0))) {
                            Toast.makeText(getActivity(), " La no coincide con la actual", Toast.LENGTH_LONG).show();
                        } else {
                            if (!V_REP_CON.equals(V_PAS)) {
                                Toast.makeText(getActivity(), " La contraseña no coincide", Toast.LENGTH_LONG).show();
                            } else {
                                String sql = "UPDATE Usuarios SET Contraseña = ? WHERE Usuario = ?";
                                db.execSQL(sql,new String[]{V_PAS,u});
                                Toast.makeText(getActivity(), " La contraseña se ha modificado", Toast.LENGTH_LONG).show();

                            }
                        }
                    }

                }
            }
        });

        return view;

    }
}