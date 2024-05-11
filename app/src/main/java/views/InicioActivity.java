package views;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cooktogether.R;

import java.sql.SQLException;

import controllers.UserController;
import models.UserModel;
import nl.joery.animatedbottombar.AnimatedBottomBar;

public class InicioActivity extends AppCompatActivity {
    private AnimatedBottomBar bottomBar;
    private UserModel user = new UserModel();
    private UserController userController = new UserController();
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

        Intent login = getIntent();
        String username = login.getStringExtra("username");
        try {
            user = userController.buscarUsuario(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {
                if (tab.getTitle().equals("Buscar")) {
                    Intent intent = new Intent(InicioActivity.this, BuscarActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
                if (tab.getTitle().equals("Perfil")) {
                    Intent intent = new Intent(InicioActivity.this, PerfilActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
                if (tab.getTitle().equals("Compartir")) {
                    Intent intent = new Intent(InicioActivity.this, PublicarActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
            }

            @Override
            public void onTabSelected(int lastIndex, AnimatedBottomBar.Tab lastTab, int newIndex, AnimatedBottomBar.Tab newTab) {
                if (newTab.getTitle().equals("Buscar")) {
                    Intent intent = new Intent(InicioActivity.this, BuscarActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
                if (newTab.getTitle().equals("Perfil")) {
                    Intent intent = new Intent(InicioActivity.this, PerfilActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
                if (newTab.getTitle().equals("Compartir")) {
                    Intent intent = new Intent(InicioActivity.this, PublicarActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
            }
        });

    }
}