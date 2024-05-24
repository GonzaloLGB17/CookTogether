package views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cooktogether.R;

import java.sql.SQLException;
import java.util.ArrayList;

import adapters.InicioAdapter;
import controllers.RecetaController;
import controllers.UserController;
import interfaces.InterfacePublicacion;
import models.RecetaModel;
import models.UserModel;
import nl.joery.animatedbottombar.AnimatedBottomBar;
import utils.ImageUtil;

public class InicioActivity extends AppCompatActivity implements InterfacePublicacion {

    private AnimatedBottomBar bottomBar;
    private UserModel user = new UserModel();
    private UserController userController = new UserController();
    private RecetaController recetaController = new RecetaController();
    private ArrayList<RecetaModel> recetas = new ArrayList<>();
    private ImageUtil imageUtil = new ImageUtil();
    private TextView tvUserInicio;
    private ImageView imgUserInicio, imgFilters;
    private RecyclerView rvInicio;
    private InicioAdapter inicioAdapter;
    private Spinner spFilters;
    private String username = "";
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
        imgFilters = findViewById(R.id.imgFilters);
        Intent intent = getIntent();
        user = (UserModel) intent.getSerializableExtra("user");

        tvUserInicio.setText(user.getUsername());
        imgUserInicio.setImageBitmap(imageUtil.transformarBytesBitmap(user.getFotoUsuario()));

        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {
                if (tab.getTitle().equals("Buscar")) {
                    Intent intent = new Intent(InicioActivity.this, BuscarActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (tab.getTitle().equals("Perfil")) {
                    Intent intent = new Intent(InicioActivity.this, PerfilActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (tab.getTitle().equals("Compartir")) {
                    Intent intent = new Intent(InicioActivity.this, PublicarActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("mode","false");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }

            @Override
            public void onTabSelected(int lastIndex, AnimatedBottomBar.Tab lastTab, int newIndex, AnimatedBottomBar.Tab newTab) {
                if (newTab.getTitle().equals("Buscar")) {
                    Intent intent = new Intent(InicioActivity.this, BuscarActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (newTab.getTitle().equals("Perfil")) {
                    Intent intent = new Intent(InicioActivity.this, PerfilActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (newTab.getTitle().equals("Compartir")) {
                    Intent intent = new Intent(InicioActivity.this, PublicarActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("mode","false");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }
        });

        spFilters = findViewById(R.id.spFilters);
        String[] categorias = {" ","Recientes"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_categorias, categorias);
        adapter.setDropDownViewResource(R.layout.spinner_categorias);
        spFilters.setAdapter(adapter);

        imgFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spFilters.performClick();
            }
        });

        spFilters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String filtro = parent.getItemAtPosition(position).toString();
                try {
                    recetas = recetaController.obtenerRecetasFiltros(filtro);
                    cargarRv(recetas);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(parent.getContext(), "HOLA",Toast.LENGTH_LONG).show();
            }
        });

        try {
            recetas = recetaController.obtenerRecetas();
        } catch (SQLException e) {
            Toast.makeText(this, e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        rvInicio = findViewById(R.id.rvInicio);
        inicioAdapter = new InicioAdapter(this, recetas, this);
        rvInicio.setAdapter(inicioAdapter);
        // GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvInicio.setLayoutManager(new LinearLayoutManager(this));
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

    public void cargarRv(ArrayList<RecetaModel> recetasArray){
        rvInicio = findViewById(R.id.rvInicio);
        inicioAdapter = new InicioAdapter(this, recetasArray, this);
        rvInicio.setAdapter(inicioAdapter);
        rvInicio.setLayoutManager(new LinearLayoutManager(this));
    }
}