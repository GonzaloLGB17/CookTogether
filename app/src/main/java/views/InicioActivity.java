package views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cooktogether.R;

import java.sql.SQLException;
import java.util.ArrayList;

import controllers.RecetaController;
import controllers.UserController;
import models.RecetaModel;
import models.UserModel;
import nl.joery.animatedbottombar.AnimatedBottomBar;
import utils.ImageUtil;

public class InicioActivity extends AppCompatActivity{
    private AnimatedBottomBar bottomBar;
    private UserModel user = new UserModel();
    private UserController userController = new UserController();
    private RecetaController recetaController = new RecetaController();
    private ArrayList<RecetaModel> recetas = new ArrayList<>();
    private ImageUtil imageUtil = new ImageUtil();
    private TextView tvUserInicio;
    private ImageView imgUserInicio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initComponents();
    }

    private void initComponents() {
        bottomBar = findViewById(R.id.bottomBarInicio);
        tvUserInicio = findViewById(R.id.tvUserInicio);
        imgUserInicio = findViewById(R.id.imgUserInicio);

        Intent login = getIntent();
        String username = login.getStringExtra("username");
        try {
            user = userController.buscarUsuario(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        tvUserInicio.setText(username);
        imgUserInicio.setImageBitmap(imageUtil.transformarBytesBitmap(user.getFotoUsuario()));

        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {
                if (tab.getTitle().equals("Buscar")) {
                    Intent intent = new Intent(InicioActivity.this, BuscarActivity.class);
                    intent.putExtra("username", username);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (tab.getTitle().equals("Perfil")) {
                    Intent intent = new Intent(InicioActivity.this, PerfilActivity.class);
                    intent.putExtra("username", username);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (tab.getTitle().equals("Compartir")) {
                    Intent intent = new Intent(InicioActivity.this, PublicarActivity.class);
                    intent.putExtra("username", username);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }

            @Override
            public void onTabSelected(int lastIndex, AnimatedBottomBar.Tab lastTab, int newIndex, AnimatedBottomBar.Tab newTab) {
                if (newTab.getTitle().equals("Buscar")) {
                    Intent intent = new Intent(InicioActivity.this, BuscarActivity.class);
                    intent.putExtra("username", username);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (newTab.getTitle().equals("Perfil")) {
                    Intent intent = new Intent(InicioActivity.this, PerfilActivity.class);
                    intent.putExtra("username", username);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (newTab.getTitle().equals("Compartir")) {
                    Intent intent = new Intent(InicioActivity.this, PublicarActivity.class);
                    intent.putExtra("username", username);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }
        });

        try {
            recetas = recetaController.obtenerRecetas();
        } catch (SQLException e) {
            Toast.makeText(this, e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
