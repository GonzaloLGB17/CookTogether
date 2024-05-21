package views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cooktogether.R;

import java.sql.SQLException;

import controllers.RecetaController;
import controllers.UserController;
import models.RecetaModel;
import models.UserModel;
import nl.joery.animatedbottombar.AnimatedBottomBar;
import utils.ImageUtil;

public class PublicacionActivity extends AppCompatActivity {
    private ImageView imgUserPublicacion, imgRecetaPublicacion;
    private TextView tvUserPublicacion, tvTitulo, tvContent, tvCategoria;
    private AnimatedBottomBar menuBar;
    private AnimatedBottomBar bottomBar;
    private EditText etDescripcion,  etTitulo;
    private UserController userController = new UserController();
    private RecetaController recetaController = new RecetaController();
    private ImageUtil imageUtil = new ImageUtil();
    private UserModel user = new UserModel();
    private RecetaModel receta = new RecetaModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_publicacion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initComponents();
    }

    private void initComponents() {
        imgUserPublicacion = findViewById(R.id.imgUserPublicacion);
        imgRecetaPublicacion = findViewById(R.id.imgRecetaPublicacion);
        tvUserPublicacion = findViewById(R.id.tvUserPublicacion);
        etDescripcion = findViewById(R.id.etDescReceta);
        tvTitulo = findViewById(R.id.tvTituloPublicacion);
        tvContent = findViewById(R.id.tvContentPublicacion);
        tvCategoria = findViewById(R.id.tvCategoriaRecetaPublicacion);
        etTitulo = findViewById(R.id.etTituloRecetaPublicacion);
        menuBar = findViewById(R.id.menuBar);
        bottomBar = findViewById(R.id.bottomBarPublicacion);
        Intent intent = getIntent();
        String username = intent.getStringExtra("user");
        String usernamePub = intent.getStringExtra("userPub");
        String recetaTitulo = intent.getStringExtra("receta");
        try {
            user = userController.buscarUsuario(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            receta = recetaController.buscarReceta(recetaTitulo, usernamePub);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        tvUserPublicacion.setText(receta.getUsuario());
        try {
            imgUserPublicacion.setImageBitmap(imageUtil.transformarBytesBitmap(userController.buscarUsuario(receta.getUsuario()).getFotoUsuario()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        etTitulo.setText(receta.getTitulo());
        etDescripcion.setText(receta.getDescripcion());
        imgRecetaPublicacion.setImageBitmap(imageUtil.transformarBytesBitmap(receta.getFotoReceta()));
        tvTitulo.setText("Ingredientes");
        tvContent.setText(receta.getIngredientes());
        tvCategoria.setText("Categoría: " + receta.getCategoria());

        menuBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {
                if (tab.getTitle().equals("Ingredientes")) {
                    tvTitulo.setText("Ingredientes");
                    tvContent.setText(receta.getIngredientes());
                }
                if (tab.getTitle().equals("Instrucciones")) {
                    tvTitulo.setText("Instrucciones");
                    tvContent.setText(receta.getInstrucciones());
                }
            }

            @Override
            public void onTabSelected(int lastIndex, AnimatedBottomBar.Tab lastTab, int newIndex, AnimatedBottomBar.Tab newTab) {
                if (newTab.getTitle().equals("Ingredientes")) {
                    tvTitulo.setText("Ingredientes");
                    tvContent.setText(receta.getIngredientes());
                }
                if (newTab.getTitle().equals("Instrucciones")) {
                    tvTitulo.setText("Instrucciones");
                    tvContent.setText(receta.getInstrucciones());
                }
            }
        });

        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {
                if (tab.getTitle().equals("Inicio")) {
                    Intent intent = new Intent(PublicacionActivity.this, InicioActivity.class);
                    intent.putExtra("username", username);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (tab.getTitle().equals("Buscar")) {
                    Intent intent = new Intent(PublicacionActivity.this, BuscarActivity.class);
                    intent.putExtra("username", username);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (tab.getTitle().equals("Perfil")) {
                    Intent intent = new Intent(PublicacionActivity.this, PerfilActivity.class);
                    intent.putExtra("username", username);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (tab.getTitle().equals("Compartir")) {
                    Intent intent = new Intent(PublicacionActivity.this, PublicarActivity.class);
                    intent.putExtra("username", username);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }

            @Override
            public void onTabSelected(int lastIndex, AnimatedBottomBar.Tab lastTab, int newIndex, AnimatedBottomBar.Tab newTab) {
                if (newTab.getTitle().equals("Inicio")) {
                    Intent intent = new Intent(PublicacionActivity.this, InicioActivity.class);
                    intent.putExtra("username", username);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (newTab.getTitle().equals("Buscar")) {
                    Intent intent = new Intent(PublicacionActivity.this, BuscarActivity.class);
                    intent.putExtra("username", username);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (newTab.getTitle().equals("Perfil")) {
                    Intent intent = new Intent(PublicacionActivity.this, PerfilActivity.class);
                    intent.putExtra("username", username);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (newTab.getTitle().equals("Compartir")) {
                    Intent intent = new Intent(PublicacionActivity.this, PublicarActivity.class);
                    intent.putExtra("username", username);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }
        });


    }

}