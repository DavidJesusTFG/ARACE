package com.example.arace.menu.reservas.zonas;

import android.content.Intent;
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
import com.example.arace.menu.reservas.plazas.Plazas;

import java.util.List;

public class RecycleViewAdapterZonas extends RecyclerView.Adapter<RecycleViewAdapterZonas.ReservasViewHolder> {
    Zonas context;

    List<ListaZonas> listareservas;
    String nombreUsuario;
    String fecha;
    String inicio;
    String fin;

    public RecycleViewAdapterZonas(Zonas reservas, List<ListaZonas> listareservas, String nombreUsuario, String f, String i, String l) {
        this.context=reservas;
        this.listareservas=listareservas;
        this.nombreUsuario = nombreUsuario;
        this.fecha=f;
        this.inicio=i;
        this.fin=l;
    }
    public void actualizarDatos(List<ListaZonas> nuevasReservas) {
        listareservas.clear();
        listareservas.addAll(nuevasReservas);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecycleViewAdapterZonas.ReservasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_reservas, parent, false);
        return new ReservasViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapterZonas.ReservasViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.tvZona.setText(listareservas.get(position).getZona());
        holder.tvPlaza.setText(String.valueOf(listareservas.get(position).getPlaza()));
        //Segun la zona se carga una imagen distinta
        switch (holder.tvZona.getText().toString()) {
            case "Zona Entrada":
                holder.tvImagen.setImageResource(R.drawable.entrada);
                break;
            case "Zona Cafetería":
                holder.tvImagen.setImageResource(R.drawable.cafeteria);
                break;
            case "Zona hotel-escuela":
                holder.tvImagen.setImageResource(R.drawable.hotelescuela);
                break;
            case "Zona Pabellón":
                holder.tvImagen.setImageResource(R.drawable.pabellon);
                break;
            case "Zona jardín":
                holder.tvImagen.setImageResource(R.drawable.jardineria);
                break;
            case "Zona para Alumnos":
                holder.tvImagen.setImageResource(R.drawable.alumnos);
                break;
            case "Zona residencia de estudiantes":
                holder.tvImagen.setImageResource(R.drawable.residencia);
                break;
        }
        //Este código srive para cambiar el color del borde dependiendo de una condicion
        ConstraintLayout layout= holder.itemView.findViewById(R.id.idBordeZona);

        if(listareservas.get(position).getPlaza()!=0){
            layout.setBackgroundResource(R.drawable.border_verde);
        }else{
            layout.setBackgroundResource(R.drawable.border_rojo);
        }
        final int finalPosition = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listareservas.get(position).getPlaza()!=0){
                    Intent i = new Intent(view.getContext(), Plazas.class);
                    i.putExtra("Zona", listareservas.get(finalPosition).getZona());
                    i.putExtra("Plaza", listareservas.get(finalPosition).getPlaza());
                    //Para que todo funcione debemos pasar el nombre de usuario
                    i.putExtra("NUsuario", nombreUsuario);
                    i.putExtra("Fecha", fecha);
                    i.putExtra("Inicio", inicio);
                    i.putExtra("Final", fin);
                    context.startActivity(i);
                }else{
                    Toast.makeText(context,"La zona ya está ocupada",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
    @Override
    public int getItemCount() {
        return listareservas.size();
    }

    public class ReservasViewHolder extends RecyclerView.ViewHolder{

        TextView tvZona;
        TextView tvPlaza;
        ImageView tvImagen;
        public ReservasViewHolder(@NonNull View itemView){
            super(itemView);

            tvZona=itemView.findViewById(R.id.tvZona);
            tvPlaza=itemView.findViewById(R.id.tvPlaza);
            tvImagen=itemView.findViewById(R.id.idImagen);
        }
    }
}
