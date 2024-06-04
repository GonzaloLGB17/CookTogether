package views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cooktogether.R;

import java.sql.SQLException;
import java.util.ArrayList;

import adapters.PerfilAdapter;
import controllers.RecetaController;
import controllers.UserController;
import interfaces.InterfacePublicacion;
import models.RecetaModel;
import models.UserModel;
import nl.joery.animatedbottombar.AnimatedBottomBar;
import utils.ImageUtil;

public class UserBuscadoPerfilActivity extends AppCompatActivity implements InterfacePublicacion{
    private AnimatedBottomBar bottomBar;
    private TextView tvUserPerfilBuscado, tvPuntuacionPerfilBuscado, tvNumPublicacionesBuscado;
    private ImageView imgUserPerfilBuscado;
    private UserModel user = new UserModel();
    private UserModel userBuscado = new UserModel();
    private ImageUtil imageUtil = new ImageUtil();
    private RecetaController recetaController = new RecetaController();
    private UserController userController = new UserController();
    private ArrayList<RecetaModel> recetas = new ArrayList<>();
    private RecyclerView rvPerfilBuscado;
    private PerfilAdapter perfilAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_buscado_perfil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initComponents();
    }

    private void initComponents() {
        bottomBar = findViewById(R.id.bottomBarPerfilBuscado);
        tvUserPerfilBuscado = findViewById(R.id.tvUserPerfilBuscado);
        imgUserPerfilBuscado = findViewById(R.id.imgUserPerfilBuscado);
        tvNumPublicacionesBuscado = findViewById(R.id.tvNumPublicacionesBuscado);
        tvPuntuacionPerfilBuscado = findViewById(R.id.tvPuntuacionPerfilBuscado);

        Intent intent = getIntent();
        user = (UserModel) intent.getSerializableExtra("user");
        String username = intent.getStringExtra("userPub");
        try {
            userBuscado = userController.buscarUsuario(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        tvUserPerfilBuscado.setText(userBuscado.getUsername());
        Bitmap foto = imageUtil.transformarBytesBitmap(userBuscado.getFotoUsuario());
        imgUserPerfilBuscado.setImageBitmap(imageUtil.redimensionarImagen(foto,1920,1080));

        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {
                if (tab.getTitle().equals("Inicio")) {
                    Intent intent = new Intent(UserBuscadoPerfilActivity.this, InicioActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (tab.getTitle().equals("Buscar")) {
                    Intent intent = new Intent(UserBuscadoPerfilActivity.this, BuscarActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (tab.getTitle().equals("Compartir")) {
                    Intent intent = new Intent(UserBuscadoPerfilActivity.this, PublicarActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("mode","false");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }

            @Override
            public void onTabSelected(int lastIndex, AnimatedBottomBar.Tab lastTab, int newIndex, AnimatedBottomBar.Tab newTab) {
                if (newTab.getTitle().equals("Inicio")) {
                    Intent intent = new Intent(UserBuscadoPerfilActivity.this, InicioActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (newTab.getTitle().equals("Buscar")) {
                    Intent intent = new Intent(UserBuscadoPerfilActivity.this, BuscarActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (newTab.getTitle().equals("Compartir")) {
                    Intent intent = new Intent(UserBuscadoPerfilActivity.this, PublicarActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("mode","false");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }
        });
        try {
            recetas = recetaController.obtenerRecetasUsuario(userBuscado.getUsername());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        rvPerfilBuscado = findViewById(R.id.rvPerfilBuscado);
        perfilAdapter = new PerfilAdapter(this, recetas, UserBuscadoPerfilActivity.this);
        rvPerfilBuscado.setAdapter(perfilAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvPerfilBuscado.setLayoutManager(layoutManager);
        try {
            String puntuacioMedia = userController.obtenerPuntuacionUsuario(userBuscado.getUsername());
            tvPuntuacionPerfilBuscado.setText(puntuacioMedia);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            String publicaciones = userController.obtenerRecetasUsuario(userBuscado.getUsername());
            tvNumPublicacionesBuscado.setText(publicaciones);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void pubCardClick(int position) {
        Intent intent = new Intent(this, PublicacionActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("userPub", recetas.get(position).getUsuario());
        intent.putExtra("receta", recetas.get(position).getTitulo());
        intent.putExtra("mode","false");
        startActivity(intent);
    }
}