package com.example.arace.menu.reservas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.arace.R;
import com.example.arace.bbdd.DB_Conexion;
import com.example.arace.menu.reservas.zonas.Zonas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Calendario extends AppCompatActivity  {


    Button reservar;

    CalendarView cal;

    Spinner entrada,salida;

    String fechaSeleccionada = null;
    private int selectedDay;
    private int selectedMonth;
    private int selectedYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_reserva);
        reservar=findViewById(R.id.btnreservar);
        cal=findViewById(R.id.calendarView);
        horario_entrada();
        horario_salida();
        DB_Conexion dbC=new DB_Conexion(this);
        SQLiteDatabase db= dbC.getWritableDatabase();
        cal.setDate(0);
        Log.d("Solicitud_Reserva", "Nueva fecha seleccionada: " + fechaSeleccionada);
        String u=getIntent().getStringExtra("Usuario");
        Log.d("Solicitud_Reserva", "Nombre de usuario: " + u);
        String t=getIntent().getStringExtra("Tipo");
        Log.d("Solicitud_Reserva", "Tipo de usuario: " + t);

        //Con esto se crea un dato de una fecha en el calendario
        cal.setOnDateChangeListener(((view, year, month, dayOfMonth) -> {
            selectedDay = dayOfMonth;
            selectedMonth = month + 1;
            selectedYear = year;
            fechaSeleccionada = String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth, selectedDay);
            Log.d("Solicitud_Reserva", "Nueva fecha seleccionada: " + fechaSeleccionada);
        }));


   // Deshabilita fechas anteriores al día actual
        cal.setMinDate(System.currentTimeMillis() - 1000);

       reservar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String fechaInicio = null;
               String fechaFinal = null;
               Cursor cursor = null;
               try {
                   // Obtiene los valores seleccionados de los Spinners
                   //Convertimos a string los datos de los spinners
                   String horaEntradaSeleccionada = entrada.getSelectedItem().toString();
                   Log.d("Fecha inicio entrada", horaEntradaSeleccionada);
                   String horaSalidaSeleccionada = salida.getSelectedItem().toString();

                   //Esto sivrve para poner coniciones en los rangos de hora
                   //lo metemos en una lista
                   List<String> horariosEntrada = Arrays.asList("8:30-9:25", "9:25-10:20", "10:40-11:35", "11:35-12:30", "12:40-13:35", "13:35-14:30", "14:30-15:25");
                   List<String> horariosSalida = Arrays.asList("8:30-9:25", "9:25-10:20", "10:40-11:35", "11:35-12:30", "12:40-13:35", "13:35-14:30", "14:30-15:25");

                   //Creamos una variable para sacar la posicion de la lista
                   int posicionEntrada = horariosEntrada.indexOf(horaEntradaSeleccionada);
                   int posicionSalida = horariosSalida.indexOf(horaSalidaSeleccionada);
                   //Si la posicion de la entrada en superios al de la salida...
                   if (posicionEntrada > posicionSalida) {
                       Toast.makeText(Calendario.this, "Rango de hora no permitido", Toast.LENGTH_SHORT).show();
                       return;
                   }
                   // Crea un formato de fecha y hora completo para la inserción
                   fechaInicio = horaEntradaSeleccionada.split("-")[0];
                   fechaFinal = horaSalidaSeleccionada.split("-")[1];
                   Log.d("Fecha inicio", fechaInicio);
                   Log.d("Fecha final", fechaFinal);
                   // Verificación de la fecha
                   int cod;
                   String fecha = "SELECT * FROM Calendario WHERE Fecha = ?";
                   cursor = db.rawQuery(fecha, new String[]{fechaSeleccionada});
                   Log.d("Solicitud_Reserva", "Fecha seleccionada bbdd: " + cursor);
                   if (cursor != null && cursor.moveToFirst()) {
                       String fechaDB = cursor.getString(0);
                       Log.d("Solicitud_Reserva", "Fecha de la base de datos: " + fechaDB);
                       //Tenemos una tabla calendario con un codigo que es un 0 y un 1, si es 0 se puede reservar en caso contrario no
                       if (fechaDB != null && fechaDB.equals(fechaSeleccionada)) {
                           cod = cursor.getInt(1);
                           if (cod == 0) {
                               Intent i = new Intent(Calendario.this, Zonas.class);
                               //Para volver al menu es necesarior recoger el nombre de usuario y las fechas y horas
                               i.putExtra("Reservausuario", u);
                               i.putExtra("Reservatipo", t);
                               i.putExtra("Fecha",fechaDB);
                               i.putExtra("HoraEntrada",fechaInicio);
                               i.putExtra("HoraSalida",fechaFinal);
                               startActivity(i);
                           } else if (cod == 1) {
                               Toast.makeText(Calendario.this, "No se puede reservar ese día", Toast.LENGTH_SHORT).show();
                           }
                       } else {
                           Toast.makeText(Calendario.this, "No se puede reservar ese día", Toast.LENGTH_SHORT).show();
                       }
                       cursor.close();
                   } else {
                       Toast.makeText(Calendario.this, "No se puede reservar ese día", Toast.LENGTH_SHORT).show();
                   }
               } catch (Exception e) {
                   Toast.makeText(Calendario.this, "Debes ingresar la fecha", Toast.LENGTH_SHORT).show();
               }
           }
       });

    }
    //Estos spiners son arrays con los horarios estos metodos son necesarios porque muestran en pantalla los horarios
    //Si no lo ponemos no aparecerán los horarios
    public Spinner horario_entrada(){
        entrada=findViewById(R.id.id_hora_entrada);
        String [] horarios ={"8:30-9:25","9:25-10:20","10:40-11:35","11:35-12:30","12:40-13:35","13:35-14:30","14:30-15:25"};
        //Añade el array al spinner
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, horarios);

        entrada.setAdapter(adapter);

        return entrada;

    }

    public Spinner horario_salida(){
        salida=findViewById(R.id.id_hora_salida);
        String [] horarios ={"8:30-9:25","9:25-10:20","10:40-11:35","11:35-12:30","12:40-13:35","13:35-14:30","14:30-15:25"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, horarios);

        salida.setAdapter(adapter);

        return salida;
    }

}