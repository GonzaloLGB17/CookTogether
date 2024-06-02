package views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
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
import adapters.UsersAdapter;
import controllers.UserController;
import interfaces.InterfacePublicacion;
import models.RecetaModel;
import models.UserModel;
import nl.joery.animatedbottombar.AnimatedBottomBar;
import utils.ImageUtil;

public class BuscarActivity extends AppCompatActivity implements InterfacePublicacion {
    private AnimatedBottomBar bottomBar;
    private UserModel user = new UserModel();
    private UserController userController = new UserController();
    private ImageUtil imageUtil = new ImageUtil();
    private TextView tvUserBuscar;
    private RecyclerView rvBuscar;
    private ImageView imgUserBuscar, imgSearchBuscar;
    private UsersAdapter usersAdapter;
    private ArrayList<UserModel> usuarios = new ArrayList<>();
    private EditText etSearchUsuarios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buscar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.consEdit), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initComponents();
    }

    private void initComponents() {
        bottomBar = findViewById(R.id.bottomBarBuscar);
        tvUserBuscar = findViewById(R.id.tvUserBuscar);
        imgSearchBuscar = findViewById(R.id.imgSearchBuscar);
        rvBuscar = findViewById(R.id.rvBuscar);
        imgUserBuscar = findViewById(R.id.imgUserBuscar);
        Intent intent = getIntent();
        user = (UserModel) intent.getSerializableExtra("user");
        tvUserBuscar.setText(user.getUsername());
        imgUserBuscar.setImageBitmap(imageUtil.transformarBytesBitmap(user.getFotoUsuario()));
        etSearchUsuarios = findViewById(R.id.etSearchUsers);

        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {
                if (tab.getTitle().equals("Inicio")) {
                    Intent intent = new Intent(BuscarActivity.this, InicioActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (tab.getTitle().equals("Perfil")) {
                    Intent intent = new Intent(BuscarActivity.this, PerfilActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (tab.getTitle().equals("Compartir")) {
                    Intent intent = new Intent(BuscarActivity.this, PublicarActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("mode","false");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }

            @Override
            public void onTabSelected(int lastIndex, AnimatedBottomBar.Tab lastTab, int newIndex, AnimatedBottomBar.Tab newTab) {
                if (newTab.getTitle().equals("Inicio")) {
                    Intent intent = new Intent(BuscarActivity.this, InicioActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (newTab.getTitle().equals("Perfil")) {
                    Intent intent = new Intent(BuscarActivity.this, PerfilActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (newTab.getTitle().equals("Compartir")) {
                    Intent intent = new Intent(BuscarActivity.this, PublicarActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("mode","false");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }
        });

        imgSearchBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    usuarios = userController.obtenerUsuariosBuscados(etSearchUsuarios.getText().toString().toLowerCase());
                    if(usuarios.size() == 0){
                        Toast.makeText(BuscarActivity.this,"No existen usuarios.", Toast.LENGTH_SHORT).show();
                    }else if(etSearchUsuarios.getText().toString().isEmpty()){
                        //Para que no cargue todos los usuarios si busca con valores vacios
                    }else {
                        cargarRv(usuarios);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }

    public void cargarRv(ArrayList<UserModel> usuariosArray){
        rvBuscar = findViewById(R.id.rvBuscar);
        usersAdapter = new UsersAdapter(this, usuariosArray, this);
        rvBuscar.setAdapter(usersAdapter);
        rvBuscar.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void pubCardClick(int position) {

    }
}