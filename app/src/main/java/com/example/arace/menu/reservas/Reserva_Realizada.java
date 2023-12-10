package com.example.arace.menu.reservas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.arace.R;
import com.example.arace.menu.Menu_Principal;

public class Reserva_Realizada extends AppCompatActivity {
    Button ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva_realizada);
        ok=findViewById(R.id.id_okrealizdo);
        String u=getIntent().getStringExtra("Nombre_Usuario");
        Log.d("Usuario", "Nombre de usuario: " + u);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Reserva_Realizada.this, Menu_Principal.class);
                i.putExtra("Dev_U",u);
                startActivity(i);
            }
        });
    }
}