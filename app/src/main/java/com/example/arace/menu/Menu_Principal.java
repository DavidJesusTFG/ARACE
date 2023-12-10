package com.example.arace.menu;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.example.arace.MainActivity;
import com.example.arace.R;
import com.example.arace.databinding.ActivityMenuAlumnoBinding;
import com.example.arace.bbdd.DB_Conexion;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Menu_Principal extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuAlumnoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuAlumnoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMenuAlumno.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        DB_Conexion dbC=new DB_Conexion(this);
        SQLiteDatabase db= dbC.getWritableDatabase();

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        //Una vez que la reserva se realiza recogemos el usuario que recogimos de la solicitud
        //Se debe hacer esto ya que hay un problema y es que al cambiar de acvtivity/fragment los datos pueden desaparecer
        String d=getIntent().getStringExtra("Dev_U");
        Log.d("Usuario", "Nombre de usuario devuelto: " + d);
        // Muestra el nombre del usuario que inicio sesión
        String u=getIntent().getStringExtra("Usuario");
        //Si el usuario es nulo, es porque se perdio la informacion y se debe recuperar del usuario correcto
        if(u==null){
            u=d;
        }
        Log.d("Usuario", "Nombre de usuario: " + u);
        //Recogemos el tipo de usuario
        String t=getIntent().getStringExtra("Tipo_Usuario");
        //En caso de que el tipo de usuario sea nulo
        //Se debe reoger de esta manera ya que al navegar por la app la informacion se pierde y la debemos recuperar
        if(t==null){
            try {
                Cursor rec_t = db.rawQuery("SELECT Tipo_usuario FROM Usuarios WHERE Usuario='" + u + "';", null);
                if (rec_t.moveToFirst()) {
                    t = rec_t.getString(0);
                    rec_t.close();
                }
            }catch (Exception e) {
                // Manejar cualquier excepción que pueda ocurrir al obtener el extra
                Log.e("Error", "Error al obtener el extra 'Usuario': " + e.getMessage());
            }

        }
        Log.d("Usuario", "Tipo de usuario: " + t);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.id_nombreUsuario);
        TextView navTipo = (TextView) headerView.findViewById(R.id.id_tipo_usuario);
        //Mostramos el nombre y el tipo de usuario en el navigation drawer
        navUsername.setText(u);
        navTipo.setText(t);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.bienvenida, R.id.fragment_Historial, R.id.fragment_Cambio_De_Contrasena, R.id.nav_exit)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_alumno);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu__alumno, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_alumno);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //Opciones de la barra del menú
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        //Opción de salida
        if (item.getItemId() == R.id.nav_exit) {
            AlertDialog.Builder aviso= new AlertDialog.Builder(Menu_Principal.this);
            aviso.setMessage("Estas seguro que deseas cerrar sesión")
                    .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent e=new Intent(Menu_Principal.this, MainActivity.class);
                            startActivity(e);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            AlertDialog titulo= aviso.create();
            titulo.setTitle("Salida");
            titulo.show();
        }
        return true;
    }

    //Si pulsa el botón de atras...
    public void onBackPressed(){
        AlertDialog.Builder aviso= new AlertDialog.Builder(Menu_Principal.this);
        aviso.setMessage("Estas seguro que deseas cerrar sesión")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent e=new Intent(Menu_Principal.this, MainActivity.class);
                        startActivity(e);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        aviso.create();
        aviso.show();

    }


}