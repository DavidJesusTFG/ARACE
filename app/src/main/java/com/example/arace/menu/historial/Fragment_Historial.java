package com.example.arace.menu.historial;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.arace.R;
import com.example.arace.bbdd.DB_Conexion;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Historial extends Fragment {

    RecycleViewAdapterHistorial adaptador;
    List<ListaHistorial> listahistorial=new ArrayList<>();

    String u;
    String t;
    String d;
    public Fragment_Historial() {
        // Required empty public constructor
    }

    public static Fragment_Historial newInstance(String param1, String param2) {
        Fragment_Historial fragment = new Fragment_Historial();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__historial, container, false);
        EditText Buscar = (EditText) view.findViewById(R.id.Buscar);

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
        if (getActivity() != null) {
            try {
                Intent intent = getActivity().getIntent();
                d = intent.getStringExtra("Dev_U");
                // Ahora puedes usar la variable 'u'
            } catch (Exception e) {
                // Manejar cualquier excepción que pueda ocurrir al obtener el extra
                Log.e("Error", "Error al obtener el extra 'Usuario': " + e.getMessage());
            }
        }
        Log.d("Fragment_Historial", "Nombre de usuario: " + d);
        if(u==null){
            u=d;
        }

        Log.d("Fragment_Historial", "Nombre de usuario: " + u);

        DB_Conexion dbC = new DB_Conexion(getActivity());
        SQLiteDatabase db = dbC.getReadableDatabase();
        listahistorial.clear();
        //Esta consulta solo muestra las reservas que están antes del dia de la reservA
        Cursor cursor = db.rawQuery("SELECT Plaza.Id_Plaza, Date_Inicio, Date_Final ,Dia,Nombre FROM Reservas INNER JOIN Plaza ON(Reservas.Id_Plaza = Plaza.Id_Plaza)" +
                "INNER JOIN Zona ON(Plaza.Id_Zona = Zona.Id_Zona)" +
                "WHERE Id_Usuario ='" + u + "' AND Dia>=CURRENT_DATE;", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ListaHistorial lh = new ListaHistorial();
                lh.setZona(cursor.getString(0));
                lh.setFecha_Inicio(cursor.getString(1));
                lh.setFecha_Fin(cursor.getString(2));
                lh.setDia(cursor.getString(3));
                lh.setNombre(cursor.getString(4));
                listahistorial.add(lh);

            }
        }
        cursor.close();
        dbC.close();
        //El buscador
        Buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filtrar(editable.toString());
            }
        });

        RecyclerView vl = (RecyclerView) view.findViewById(R.id.idHistorial);
        vl.setLayoutManager(new LinearLayoutManager(getContext()));

        adaptador = new RecycleViewAdapterHistorial(Fragment_Historial.this, listahistorial);
        vl.setAdapter(adaptador);
        return view;
    }
    //Los parametros del buscador
    public void filtrar(String texto){
        ArrayList<ListaHistorial>filis=new ArrayList<>();
        for(ListaHistorial vl: listahistorial){
            if(vl.getDia().toLowerCase().contains(texto.toLowerCase())){
                filis.add(vl);
            }
        }
        adaptador.filtrar(filis);
    }
}