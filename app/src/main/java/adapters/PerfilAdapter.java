package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cooktogether.R;

import java.util.ArrayList;

import interfaces.InterfacePublicacion;
import models.RecetaModel;
import utils.ImageUtil;

public class PerfilAdapter extends RecyclerView.Adapter<PerfilAdapter.MyViewHolder> {
    public Context context;
    public ArrayList<RecetaModel> recetas;
    private final InterfacePublicacion interfacePublicacion;
    public PerfilAdapter(Context context, ArrayList<RecetaModel> recetas, InterfacePublicacion interfacePublicacion){
        this.context = context;
        this.recetas = recetas;
        this.interfacePublicacion = interfacePublicacion;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.publicacion_perfil_layout, parent, false);
        return new PerfilAdapter.MyViewHolder(view, interfacePublicacion);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvTituloCard.setText(recetas.get(position).getTitulo());
        holder.tvPuntuacionCard.setText(String.valueOf(recetas.get(position).getPuntuacionMedia()));
        holder.imgPublicacionPerfil.setImageBitmap(new ImageUtil().transformarBytesBitmap(recetas.get(position).getFotoReceta()));

    }

    @Override
    public int getItemCount() {
        return recetas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgPublicacionPerfil;
        private TextView tvTituloCard, tvPuntuacionCard;
        public MyViewHolder(@NonNull View itemView, InterfacePublicacion interfacePublicacion) {
            super(itemView);
            tvTituloCard = itemView.findViewById(R.id.tvTituloCardPerfil);
            tvPuntuacionCard = itemView.findViewById(R.id.tvPuntuacionCardPerfil);
            imgPublicacionPerfil = itemView.findViewById(R.id.imgPublicacionPerfil);
            imgPublicacionPerfil.setScaleType(ImageView.ScaleType.FIT_CENTER);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        if(interfacePublicacion != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            interfacePublicacion.pubCardClick(position);
                        }
                    }
                }
            });
        }
    }
}
