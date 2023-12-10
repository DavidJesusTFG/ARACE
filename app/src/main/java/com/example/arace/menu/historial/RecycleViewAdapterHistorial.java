package com.example.arace.menu.historial;

import static android.app.PendingIntent.getActivity;

import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arace.R;
import com.example.arace.bbdd.DB_Conexion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecycleViewAdapterHistorial extends RecyclerView.Adapter<RecycleViewAdapterHistorial.HistorialViewHolder> {

    Fragment_Historial context;

    List<ListaHistorial> listahistorial;

    public RecycleViewAdapterHistorial(Fragment_Historial historial, List<ListaHistorial> listahistorial) {
        this.context=historial;
        this.listahistorial=listahistorial;
    }

    @NonNull
    @Override
    public RecycleViewAdapterHistorial.HistorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_historial, parent, false);
        return new RecycleViewAdapterHistorial.HistorialViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapterHistorial.HistorialViewHolder holder, int position) {
        holder.tvZona.setText(listahistorial.get(position).getNombre());
        holder.tvFecha_Inicio.setText(listahistorial.get(position).getFecha_Inicio());
        holder.tvFecha_Fin.setText(listahistorial.get(position).getFecha_Fin());
        holder.tvDia.setText(listahistorial.get(position).getDia());

        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaAlmacenada = LocalDate.parse(listahistorial.get(position).getDia());
        if (fechaAlmacenada.isAfter(fechaActual)) {
            holder.cancelar.setEnabled(true);
        } else {
            holder.cancelar.setEnabled(false);
        }

        holder.cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB_Conexion dbC = new DB_Conexion(context.getActivity());
                SQLiteDatabase db = dbC.getWritableDatabase();

                String deleteQuery = "DELETE FROM Reservas WHERE Id_Plaza=? AND Dia=?";
                String updateQuery = "UPDATE Plaza SET Disponibilidad='Si' WHERE Id_Plaza=?";
                String updateZonaQuery = "UPDATE Zona SET Disponibilidad = (" +
                        "SELECT COUNT(*) FROM Plaza " +
                        "WHERE Plaza.Id_Zona = Zona.Id_Zona AND Plaza.Disponibilidad = 'Si'" +
                        ") WHERE Id_Zona = (SELECT Id_Zona FROM Plaza WHERE Id_Plaza = ?)";
                try {
                    db.beginTransaction();

                    // Eliminar reserva
                    db.execSQL(deleteQuery, new String[]{listahistorial.get(position).getZona(), listahistorial.get(position).getDia()});

                    // Actualizar disponibilidad
                    db.execSQL(updateQuery, new String[]{listahistorial.get(position).getZona()});

                    db.execSQL(updateZonaQuery,  new String[]{listahistorial.get(position).getZona()});
                    db.setTransactionSuccessful();
                    listahistorial.remove(position); // Eliminar elemento de la lista();
                    notifyItemRemoved(position);
                    Toast.makeText(context.getActivity(), "Reserva cancelada", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                    dbC.close();
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return listahistorial.size();
    }

    public class HistorialViewHolder extends RecyclerView.ViewHolder{

        TextView tvZona, tvFecha_Inicio, tvFecha_Fin, tvDia;
        Button cancelar;
        public HistorialViewHolder(@NonNull View itemView){
            super(itemView);

            tvZona=itemView.findViewById(R.id.tvZona);
            tvFecha_Inicio =itemView.findViewById(R.id.tvFecha);
            tvFecha_Fin =itemView.findViewById(R.id.tvFecha2);
            tvDia=itemView.findViewById(R.id.tvDia);
            cancelar=itemView.findViewById(R.id.btnCancelar);

        }
    }
    public void filtrar(ArrayList<ListaHistorial> ListaHistorial){

        this.listahistorial=ListaHistorial;
        notifyDataSetChanged();
    }
}
