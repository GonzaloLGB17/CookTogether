package views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
    private TextView tvUserPublicacion;
    private EditText etDescripcion, etInstrucciones, etIngredientes, etTitulo;
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
        etInstrucciones = findViewById(R.id.etIntrReceta);
        etIngredientes = findViewById(R.id.etIngrReceta);
        etTitulo = findViewById(R.id.etTituloReceta);
        etIngredientes.setOnTouchListener(new View.OnTouchListener() {
            // Este metodo lo utilizo para poder scrollear en el edit text dentro de un scroll view.
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (view.getId() == R.id.etIngrReceta) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    /**
                     * Evita que el padre del EditText intercepte eventos táctiles.
                     * Esto significa que los eventos táctiles no se propagarán a los View padres,
                     * permitiendo que el EditText maneje los eventos táctiles de manera independiente.
                     **/
                    switch (event.getAction()&MotionEvent.ACTION_MASK){
                        /**
                         * Comienza un switch basado en el tipo de acción del evento táctil.
                         * MotionEvent.ACTION_MASK es una constante que se utiliza para extraer el tipo de acción del evento.
                         **/
                        case MotionEvent.ACTION_UP:
                            /**
                             * Cuando el usuario toca la pantalla y luego levanta el dedo, se genera un evento de tipo ACTION_UP.
                             * Esto suele utilizarse para acciones como hacer clic en un botón o soltar un objeto arrastrado.
                             **/
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            // Permite que el padre del EditText vuelva a interceptar eventos táctiles.
                            break;
                    }
                }
                return false;
                /**
                 * Devuelve false para indicar que el evento táctil no ha sido consumido y que otros listeners pueden seguir respondiendo a él.
                 * Esto significa que este listener no consume el evento táctil y lo pasa a otros listeners en caso de que existan.
                 **/
            }
        });

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

        etInstrucciones.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (view.getId() == R.id.etIntrReceta) {
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
            receta = recetaController.buscarReceta("arrozsad", 22);
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