package views;

import android.content.Intent;
import android.os.Bundle;
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

public class PerfilActivity extends AppCompatActivity implements InterfacePublicacion {
    private AnimatedBottomBar bottomBar;
    private TextView tvUserPerfil;
    private ImageView imgUserPerfil;
    private UserModel user = new UserModel();
    private ImageUtil imageUtil = new ImageUtil();
    private RecetaController recetaController = new RecetaController();
    private UserController userController = new UserController();
    private ArrayList<RecetaModel> recetas = new ArrayList<>();
    private RecyclerView rvPerfil;
    private PerfilAdapter perfilAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initComponents();
    }

    private void initComponents() {
        bottomBar = findViewById(R.id.bottomBarPerfil);
        tvUserPerfil = findViewById(R.id.tvUserPerfil);
        imgUserPerfil = findViewById(R.id.imgUserPerfil);

        Intent intent = getIntent();
        user = (UserModel) intent.getSerializableExtra("user");

        tvUserPerfil.setText(user.getUsername());
        imgUserPerfil.setImageBitmap(imageUtil.transformarBytesBitmap(user.getFotoUsuario()));

        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {
                if (tab.getTitle().equals("Inicio")) {
                    Intent intent = new Intent(PerfilActivity.this, InicioActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (tab.getTitle().equals("Buscar")) {
                    Intent intent = new Intent(PerfilActivity.this, BuscarActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (tab.getTitle().equals("Compartir")) {
                    Intent intent = new Intent(PerfilActivity.this, PublicarActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }

            @Override
            public void onTabSelected(int lastIndex, AnimatedBottomBar.Tab lastTab, int newIndex, AnimatedBottomBar.Tab newTab) {
                if (newTab.getTitle().equals("Inicio")) {
                    Intent intent = new Intent(PerfilActivity.this, InicioActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (newTab.getTitle().equals("Buscar")) {
                    Intent intent = new Intent(PerfilActivity.this, BuscarActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (newTab.getTitle().equals("Compartir")) {
                    Intent intent = new Intent(PerfilActivity.this, PublicarActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }
        });
        try {
            recetas = recetaController.obtenerRecetasUsuario(user.getUsername());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        rvPerfil = findViewById(R.id.rvPerfil);
        perfilAdapter = new PerfilAdapter(this, recetas, this);
        rvPerfil.setAdapter(perfilAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvPerfil.setLayoutManager(layoutManager);
    }

    @Override
    public void pubCardClick(int position) {
        Intent intent = new Intent(this, PublicacionActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("userPub", recetas.get(position).getUsuario());
        intent.putExtra("receta", recetas.get(position).getTitulo());
        startActivity(intent);
    }
}