package adapters;

import android.content.Context;
import android.graphics.Bitmap;
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

import controllers.RecetaController;
import controllers.UserController;
import interfaces.InterfacePublicacion;
import models.RecetaModel;
import models.UserModel;
import utils.ImageUtil;

public class InicioAdapter extends RecyclerView.Adapter<InicioAdapter.MyViewHolder> {
    public Context context;
    public ArrayList<RecetaModel> recetas;
    private final InterfacePublicacion interfacePublicacion;
    private RecetaController recetaController = new RecetaController();
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
        return new MyViewHolder(view, interfacePublicacion);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvUserCard.setText(recetas.get(position).getUsuario());
        holder.tvTituloCard.setText(recetas.get(position).getTitulo());
        holder.tvFechaPub.setText(recetas.get(position).getTiempoTranscurrido());
        try {
            holder.tvPuntuacionCard.setText(recetaController.obtenerPuntuacionReceta(recetas.get(position).getIdReceta()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Bitmap fotoR = new ImageUtil().transformarBytesBitmap(recetas.get(position).getFotoReceta());
        holder.imgPublicacionInicio.setImageBitmap(new ImageUtil().redimensionarImagen(fotoR,1920,1080));

        try {
            UserModel user = new UserController().buscarUsuario(recetas.get(position).getUsuario());
            Bitmap foto = new ImageUtil().transformarBytesBitmap(user.getFotoUsuario());
            holder.imgUserPublicacionCard.setImageBitmap(new ImageUtil().redimensionarImagen(foto,1920,1080));
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
        private TextView tvTituloCard, tvUserCard, tvPuntuacionCard, tvFechaPub;
        public MyViewHolder(@NonNull View itemView, InterfacePublicacion interfacePublicacion) {
            super(itemView);
            tvUserCard = itemView.findViewById(R.id.tvUsuarioCard);
            tvTituloCard = itemView.findViewById(R.id.tvTituloCard);
            tvPuntuacionCard = itemView.findViewById(R.id.tvPuntuacioCard);
            tvFechaPub = itemView.findViewById(R.id.tvFechaPub);
            imgPublicacionInicio = itemView.findViewById(R.id.imgPublicacionInicio);
            imgUserPublicacionCard = itemView.findViewById(R.id.imgUserPublicacionCard);

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
