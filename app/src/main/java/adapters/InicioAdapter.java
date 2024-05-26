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
import interfaces.InterfacePublicacion;
import models.RecetaModel;
import models.UserModel;
import utils.ImageUtil;

public class InicioAdapter extends RecyclerView.Adapter<InicioAdapter.MyViewHolder> {
    public Context context;
    public ArrayList<RecetaModel> recetas;
    private final InterfacePublicacion interfacePublicacion;
    public InicioAdapter(Context context, ArrayList<RecetaModel> recetas, InterfacePublicacion interfacePublicacion){
        this.context = context;
        this.recetas = recetas;
        this.interfacePublicacion = interfacePublicacion;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.publicacion_layout, parent, false);
        return new InicioAdapter.MyViewHolder(view, interfacePublicacion);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvUserCard.setText(recetas.get(position).getUsuario());
        holder.tvTituloCard.setText(recetas.get(position).getTitulo());
        holder.tvPuntuacionCard.setText(String.valueOf(String.format("%.1f", recetas.get(position).getPuntuacionMedia())));
        holder.imgPublicacionInicio.setScaleType(ImageView.ScaleType.FIT_CENTER);
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
        public MyViewHolder(@NonNull View itemView, InterfacePublicacion interfacePublicacion) {
            super(itemView);
            tvUserCard = itemView.findViewById(R.id.tvUsuarioCard);
            tvTituloCard = itemView.findViewById(R.id.tvTituloCard);
            tvPuntuacionCard = itemView.findViewById(R.id.tvPuntuacioCard);
            imgPublicacionInicio = itemView.findViewById(R.id.imgPublicacionInicio);
            imgPublicacionInicio.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imgUserPublicacionCard = itemView.findViewById(R.id.imgUserPublicacionCard);
            imgUserPublicacionCard.setScaleType(ImageView.ScaleType.FIT_CENTER);

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
