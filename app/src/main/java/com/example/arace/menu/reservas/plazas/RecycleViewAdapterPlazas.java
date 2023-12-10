package com.example.arace.menu.reservas.plazas;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arace.R;
import com.example.arace.bbdd.DB_Conexion;
import com.example.arace.menu.reservas.Reserva_Realizada;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewAdapterPlazas extends RecyclerView.Adapter<RecycleViewAdapterPlazas.PlazaViewHolder> {

    Plazas context;
    List<ListaPlazas> listaplazas=new ArrayList<>();
    String nombreUsuario;
    String fecha;
    String inicio;
    String fin;
    String sql=null;
    String up=null;
    public RecycleViewAdapterPlazas(Plazas adaptador, List<ListaPlazas> listareservas, String u, String d, String i, String f) {
        this.context =adaptador;
        this.listaplazas=listareservas;
        this.nombreUsuario = u;
        this.fecha=d;
        this.inicio=i;
        this.fin=f;
    }


    //Visualiza el item
    @NonNull
    @Override
    public PlazaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plazas, parent, false);
        return new PlazaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlazaViewHolder holder, int position) {
        //Esto visualiza los datos
        holder.tvlugar.setText(listaplazas.get(position).getLugar());
        holder.tvdisponibilidad.setText(listaplazas.get(position).getDisponibilidad());
        DB_Conexion dbC=new DB_Conexion(context);
        SQLiteDatabase db= dbC.getWritableDatabase();
        //Este código srive para cambiar el color del borde dependiendo de una condicion
        ConstraintLayout layout= holder.itemView.findViewById(R.id.idBordeZona);

        if(listaplazas.get(position).getDisponibilidad().equals("Si")){
            layout.setBackgroundResource(R.drawable.border_verde);
        }else if (listaplazas.get(position).getDisponibilidad().equals("No")){
            layout.setBackgroundResource(R.drawable.border_rojo);
        }

        final int pos = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sql = "INSERT INTO Reservas (Id_Usuario, Id_Plaza, Date_Inicio, Date_Final, Dia) VALUES (?,?, ?, ?, ?);";
                up = "UPDATE Plaza SET Disponibilidad='No' WHERE Lugar=? ;";

                String[] bindArgs = {nombreUsuario,String.valueOf(listaplazas.get(pos).getPlaza()) ,inicio, fin, fecha};
                String[] Args = {listaplazas.get(pos).getLugar()};
                try {

                    db.execSQL(sql, bindArgs);
                    db.execSQL(up, Args);
                    //Actualizamos la disponibilidad de las zonas restando la plaza
                    String updateZonaQuery = "UPDATE Zona SET Disponibilidad = (" +
                            "SELECT COUNT(*) FROM Plaza " +
                            "WHERE Plaza.Id_Zona = Zona.Id_Zona AND Plaza.Disponibilidad = 'Si'" +
                            ") WHERE Id_Zona = (SELECT Id_Zona FROM Plaza WHERE Lugar = ?)";
                    db.execSQL(updateZonaQuery, Args);




                    Log.d("Solicitud_Reserva", "Inserción exitosa en Reservas");
                    Log.d("Cambio disponilbilidad plaza", "Se ha actualizado el estado de la plaza");


                } catch (SQLException e) {

                    Log.e("Solicitud_Reserva", "Error al insertar en Reservas: " + e.getMessage());
                    Log.e("Cambio disponibilidad plaza", "Error al actualizar la plaza" + e.getMessage());


                }
                //Al seleccionar se realiza el intent
                if(listaplazas.get(position).getDisponibilidad().equals("Si")){
                    Intent i = new Intent(view.getContext(), Reserva_Realizada.class);
                    i.putExtra("Id_Plaza", String.valueOf(listaplazas.get(pos).getPlaza()));
                    Log.d("Plaza", String.valueOf(listaplazas.get(pos).getPlaza()));
                    i.putExtra("Plaza", listaplazas.get(pos).getLugar());
                    i.putExtra("Disponibilidad", listaplazas.get(pos).getDisponibilidad());
                    i.putExtra("Nombre_Usuario", nombreUsuario);

                    context.startActivity(i);
                }else if(listaplazas.get(position).getDisponibilidad().equals("No")){
                    Toast.makeText(view.getContext(), "La plaza se encuentra ocupada", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public int getItemCount() {
        return listaplazas.size();
    }


    public class PlazaViewHolder  extends RecyclerView.ViewHolder {
        //Recoge los valores del item
        TextView tvlugar, tvdisponibilidad;
        ImageView tvImagen;
        public PlazaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvlugar=itemView.findViewById(R.id.tvlugar);
            tvdisponibilidad=itemView.findViewById(R.id.tvdisponibilidad);
            tvImagen = itemView.findViewById(R.id.id_imagen);
        }
    }
}
