package views;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cooktogether.R;

import java.io.Serializable;
import java.sql.SQLException;

import controllers.RecetaController;
import controllers.UserController;
import models.RecetaModel;
import models.UserModel;
import nl.joery.animatedbottombar.AnimatedBottomBar;
import utils.ImageUtil;

public class PublicacionActivity extends AppCompatActivity {
    private ImageView imgUserPublicacion, imgRecetaPublicacion, imgEditPub, imgTrashPub;
    private TextView tvUserPublicacion, tvTitulo, tvContent, tvCategoria, tvInfoPub;
    private AnimatedBottomBar menuBar;
    private AnimatedBottomBar bottomBar;
    private EditText etDescripcion;
    private UserController userController = new UserController();
    private RecetaController recetaController = new RecetaController();
    private ImageUtil imageUtil = new ImageUtil();
    private UserModel user = new UserModel();
    private RecetaModel receta = new RecetaModel();
    private boolean isEditMode = false;

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
        imgEditPub = findViewById(R.id.imgEditPub);
        imgTrashPub = findViewById(R.id.imgTrashPub);
        tvUserPublicacion = findViewById(R.id.tvUserPublicacion);
        etDescripcion = findViewById(R.id.etDescReceta);
        tvInfoPub = findViewById(R.id.tvInfoPublicacion);
        tvContent = findViewById(R.id.tvContentPublicacion);
        tvCategoria = findViewById(R.id.tvCategoriaRecetaPublicacion);
        tvTitulo = findViewById(R.id.tvTituloRecetaPublicacion);
        menuBar = findViewById(R.id.menuBar);
        bottomBar = findViewById(R.id.bottomBarPublicacion);
        Intent intent = getIntent();
        user = (UserModel) intent.getSerializableExtra("user");
        String usernamePub = intent.getStringExtra("userPub");
        String recetaTitulo = intent.getStringExtra("receta");
        isEditMode = Boolean.parseBoolean(intent.getStringExtra("mode"));
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

        tvTitulo.setText(receta.getTitulo());
        etDescripcion.setText(receta.getDescripcion());
        imgRecetaPublicacion.setImageBitmap(imageUtil.transformarBytesBitmap(receta.getFotoReceta()));
        tvInfoPub.setText("Ingredientes");
        tvContent.setText(receta.getIngredientes());
        tvCategoria.setText("Categoría: " + receta.getCategoria());

        if(isEditMode){
            imgTrashPub.setVisibility(View.VISIBLE);
            imgEditPub.setVisibility(View.VISIBLE);
        }else {
            imgTrashPub.setVisibility(View.GONE);
            imgEditPub.setVisibility(View.GONE);
        }

        imgEditPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PublicacionActivity.this, PublicarActivity.class);
                intent.putExtra("mode","true");
                intent.putExtra("receta", receta);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        imgTrashPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoEliminar(receta.getIdReceta());

            }
        });

        menuBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {
                if (tab.getTitle().equals("Ingredientes")) {
                    tvInfoPub.setText("Ingredientes");
                    tvContent.setText(receta.getIngredientes());
                }
                if (tab.getTitle().equals("Instrucciones")) {
                    tvInfoPub.setText("Instrucciones");
                    tvContent.setText(receta.getInstrucciones());
                }
            }

            @Override
            public void onTabSelected(int lastIndex, AnimatedBottomBar.Tab lastTab, int newIndex, AnimatedBottomBar.Tab newTab) {
                if (newTab.getTitle().equals("Ingredientes")) {
                    tvInfoPub.setText("Ingredientes");
                    tvContent.setText(receta.getIngredientes());
                }
                if (newTab.getTitle().equals("Instrucciones")) {
                    tvInfoPub.setText("Instrucciones");
                    tvContent.setText(receta.getInstrucciones());
                }
            }
        });

        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {
                if (tab.getTitle().equals("Inicio")) {
                    Intent intent = new Intent(PublicacionActivity.this, InicioActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (tab.getTitle().equals("Buscar")) {
                    Intent intent = new Intent(PublicacionActivity.this, BuscarActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (tab.getTitle().equals("Perfil")) {
                    Intent intent = new Intent(PublicacionActivity.this, PerfilActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (tab.getTitle().equals("Compartir")) {
                    Intent intent = new Intent(PublicacionActivity.this, PublicarActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("mode","false");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }

            @Override
            public void onTabSelected(int lastIndex, AnimatedBottomBar.Tab lastTab, int newIndex, AnimatedBottomBar.Tab newTab) {
                if (newTab.getTitle().equals("Inicio")) {
                    Intent intent = new Intent(PublicacionActivity.this, InicioActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (newTab.getTitle().equals("Buscar")) {
                    Intent intent = new Intent(PublicacionActivity.this, BuscarActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (newTab.getTitle().equals("Perfil")) {
                    Intent intent = new Intent(PublicacionActivity.this, PerfilActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (newTab.getTitle().equals("Compartir")) {
                    Intent intent = new Intent(PublicacionActivity.this, PublicarActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("mode","false");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }
        });

    }

    private void mostrarDialogoEliminar(int recetaId){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Receta");
        builder.setMessage("¿Estás seguro de que deseas eliminar esta receta?");
        builder.setIcon(R.drawable.defcookicon);

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    recetaController.eliminarReceta(recetaId);
                    dialog.dismiss();
                    Intent intent = new Intent(PublicacionActivity.this, InicioActivity.class);
                    intent.putExtra("user",user);
                    startActivity(intent);
                    Toast.makeText(PublicacionActivity.this, "Receta eliminada exitosamente.", Toast.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Toast.makeText(PublicacionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}