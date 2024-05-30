package views;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cooktogether.R;
import com.saadahmedev.popupdialog.PopupDialog;
import com.saadahmedev.popupdialog.listener.StandardDialogActionListener;

import java.sql.SQLException;
import java.sql.Timestamp;
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
    private TextView tvUserPerfil, tvPuntuacionPerfil, tvNumPublicaciones;
    private ImageView imgUserPerfil, imgLogout;
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
        imgUserPerfil.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imgLogout = findViewById(R.id.imgLogout);
        tvNumPublicaciones = findViewById(R.id.tvNumPublicaciones);
        tvPuntuacionPerfil = findViewById(R.id.tvPuntuacionPerfil);

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
                    intent.putExtra("mode","false");
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
                    intent.putExtra("mode","false");
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
        try {
            String puntuacioMedia = userController.obtenerPuntuacionUsuario(user.getUsername());
            tvPuntuacionPerfil.setText(puntuacioMedia);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            String publicaciones = userController.obtenerRecetasUsuario(user.getUsername());
            tvNumPublicaciones.setText(publicaciones);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mostrarAlertaLogout();
            }
        });
    }

    @Override
    public void pubCardClick(int position) {
        Intent intent = new Intent(this, PublicacionActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("userPub", recetas.get(position).getUsuario());
        intent.putExtra("receta", recetas.get(position).getTitulo());
        intent.putExtra("mode","true");
        startActivity(intent);
    }

    private void logout() {
        // Aquí puedes limpiar cualquier dato almacenado localmente, si tienes datos de usuario almacenados
        // Por ejemplo, limpiar SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Limpia todos los datos en SharedPreferences
        editor.apply();

        // Redirigir a la pantalla de inicio de sesión
        Intent intent = new Intent(PerfilActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Cerrar la actividad actual para que el usuario no pueda volver atrás
    }

    private void mostrarAlertaLogout(){
        PopupDialog.getInstance(PerfilActivity.this)
                .standardDialogBuilder()
                .createStandardDialog()
                .setCancelable(false)
                .setHeading("Cerrar sesión")
                .setDescription("¿Estás seguro de que deseas cerrar sesión?")
                .setIcon(R.drawable.logout)
                .setBackgroundColor(R.color.black)
                .setPositiveButtonBackgroundColor(R.color.dorado)
                .setPositiveButtonText("Si")
                .setNegativeButtonBackground(R.color.rojo)
                .setNegativeButtonText("No")
                .setHeadingFontSize(Float.valueOf(20))
                .setHeadingTextColor(R.color.dorado)
                .setDescriptionTextColor(R.color.plateado)
                .setFontFamily(R.font.amaranth)
                .build(new StandardDialogActionListener() {
                    @Override
                    public void onPositiveButtonClicked(Dialog dialog) {
                        logout();
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegativeButtonClicked(Dialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}