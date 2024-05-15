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

import java.sql.SQLException;
import java.util.ArrayList;

import controllers.UserController;
import interfaces.InterfaceInicioPublicacion;
import models.RecetaModel;
import models.UserModel;
import utils.ImageUtil;

public class InicioAdapter extends RecyclerView.Adapter<InicioAdapter.MyViewHolder> {
    public Context context;
    public ArrayList<RecetaModel> recetas;
    private final InterfaceInicioPublicacion interfaceInicioPublicacion;
    public InicioAdapter(Context context, ArrayList<RecetaModel> recetas, InterfaceInicioPublicacion interfaceInicioPublicacion){
        this.context = context;
        this.recetas = recetas;
        this.interfaceInicioPublicacion = interfaceInicioPublicacion;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.publicacion_layout, parent, false);
        return new InicioAdapter.MyViewHolder(view, interfaceInicioPublicacion);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvUserCard.setText(recetas.get(position).getUsuario());
        holder.tvTituloCard.setText(recetas.get(position).getTitulo());
        holder.tvPuntuacionCard.setText(String.valueOf(recetas.get(position).getPuntuacionMedia()));
        holder.imgPublicacionInicio.setImageBitmap(new ImageUtil().transformarBytesBitmap(recetas.get(position).getFotoReceta()));
        try {
            UserModel user = new UserController().buscarUsuario(recetas.get(position).getUsuario());
            holder.imgUserPublicacionCard.setImageBitmap(new ImageUtil().transformarBytesBitmap(user.getFotoUsuario()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return recetas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgPublicacionInicio, imgUserPublicacionCard;
        private TextView tvTituloCard, tvUserCard, tvPuntuacionCard;
        public MyViewHolder(@NonNull View itemView, InterfaceInicioPublicacion interfaceInicioPublicacion) {
            super(itemView);
            tvUserCard = itemView.findViewById(R.id.tvUsuarioCard);
            tvTituloCard = itemView.findViewById(R.id.tvTituloCard);
            tvPuntuacionCard = itemView.findViewById(R.id.tvPuntuacioCard);
            imgPublicacionInicio = itemView.findViewById(R.id.imgPublicacionInicio);
            imgUserPublicacionCard = itemView.findViewById(R.id.imgUserPublicacionCard);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(interfaceInicioPublicacion != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            interfaceInicioPublicacion.pubCardClick(position);
                        }
                    }
                }
            });
        }
    }
}
