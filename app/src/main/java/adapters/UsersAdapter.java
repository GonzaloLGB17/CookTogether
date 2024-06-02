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

import controllers.RecetaController;
import controllers.UserController;
import interfaces.InterfacePublicacion;
import models.RecetaModel;
import models.UserModel;
import utils.ImageUtil;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {
    public Context context;
    public ArrayList<UserModel> usuarios;
    private final InterfacePublicacion interfacePublicacion;
    private UserController userController = new UserController();
    public UsersAdapter(Context context, ArrayList<UserModel> usuarios, InterfacePublicacion interfacePublicacion){
        this.context = context;
        this.usuarios = usuarios;
        this.interfacePublicacion = interfacePublicacion;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.user_layout, parent, false);
        return new UsersAdapter.MyViewHolder(view, interfacePublicacion);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvUsuarioCardBuscar.setText(usuarios.get(position).getUsername());
        holder.tvNombreCardBuscar.setText(usuarios.get(position).getNombre());
        try {
            holder.tvPuntuacionCardBuscar.setText(userController.obtenerPuntuacionUsuario(usuarios.get(position).getUsername()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        holder.imgUserCardBuscar.setImageBitmap(new ImageUtil().transformarBytesBitmap(usuarios.get(position).getFotoUsuario()));
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgUserCardBuscar;
        private TextView tvUsuarioCardBuscar, tvPuntuacionCardBuscar, tvNombreCardBuscar;
        public MyViewHolder(@NonNull View itemView, InterfacePublicacion interfacePublicacion) {
            super(itemView);
            tvUsuarioCardBuscar = itemView.findViewById(R.id.tvUsuarioCardBuscar);
            tvPuntuacionCardBuscar = itemView.findViewById(R.id.tvPuntuacionCardBuscar);
            tvNombreCardBuscar = itemView.findViewById(R.id.tvNombreCardBuscar);
            imgUserCardBuscar = itemView.findViewById(R.id.imgUserCardBuscar);
            imgUserCardBuscar.setScaleType(ImageView.ScaleType.FIT_CENTER);

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
