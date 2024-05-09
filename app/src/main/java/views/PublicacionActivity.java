package views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
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
import utils.ImageUtil;

public class PublicacionActivity extends AppCompatActivity {
    private ImageView imgUserPublicacion, imgRecetaPublicacion;
    private TextView tvUserPublicacion, etInstrucciones, etIngredientes;
    private EditText etDescripcion,  etTitulo;
    private UserController userController = new UserController();
    private RecetaController recetaController = new RecetaController();
    private ImageUtil imageUtil = new ImageUtil();
    UserModel user = new UserModel();
    RecetaModel receta = new RecetaModel();
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

    private void initComponents(){
        imgUserPublicacion = findViewById(R.id.imgUserPublicacion);
        imgRecetaPublicacion = findViewById(R.id.imgRecetaPublicacion);
        tvUserPublicacion = findViewById(R.id.tvUserPublicacion);
        etDescripcion = findViewById(R.id.etDescReceta);
        etInstrucciones = findViewById(R.id.tvIntrRecetaContent);
        etIngredientes = findViewById(R.id.tvIngrRecetaContent);
        etTitulo = findViewById(R.id.etTituloReceta);


        etDescripcion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (view.getId() == R.id.etDescReceta) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });

        

        try {
            receta = recetaController.buscarReceta("Empanadas criollas", 22);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        etTitulo.setText(receta.getTitulo());
        etDescripcion.setText(receta.getDescripcion());
        etIngredientes.setText(receta.getIngredientes());
        etInstrucciones.setText(receta.getInstrucciones());
        imgRecetaPublicacion.setImageBitmap(imageUtil.transformarBytesBitmap(receta.getFotoReceta()));

    }
}