package views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

import com.example.cooktogether.R;

import java.sql.SQLException;
import java.sql.Timestamp;

import controllers.RecetaController;
import controllers.UserController;
import models.RecetaModel;
import models.UserModel;
import nl.joery.animatedbottombar.AnimatedBottomBar;
import utils.ImageUtil;

public class PublicarActivity extends AppCompatActivity {
    private AnimatedBottomBar bottomBar;
    private ImageView imgUserPublicar, imgRecetaPublicar;
    private Spinner spCategorias;
    private TextView tvUserPublicar;
    private EditText etDescripcion, etInstrucciones, etIngredientes, etTitulo;
    private UserController userController = new UserController();
    private RecetaController recetaController = new RecetaController();
    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap = null;
    private ImageUtil imageUtil = new ImageUtil();
    private UserModel user = new UserModel();
    private RecetaModel receta = new RecetaModel();
    private boolean isEditMode = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_publicar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initComponents();
    }

    private void initComponents(){
        imgUserPublicar = findViewById(R.id.imgUserPublicar);
        imgRecetaPublicar = findViewById(R.id.imgRecetaPublicar);
        tvUserPublicar = findViewById(R.id.tvUserPublicar);
        etDescripcion = findViewById(R.id.etDescReceta);
        etInstrucciones = findViewById(R.id.etIntrReceta);
        etIngredientes = findViewById(R.id.etIngrReceta);
        etTitulo = findViewById(R.id.etTituloRecetaPublicar);
        bottomBar = findViewById(R.id.bottomBarPublicar);
        spCategorias = findViewById(R.id.spCategoriasPublicar);
        String[] categorias = {"Platos Principales", "Entrantes", "Postres", "Sopas", "Ensaladas"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_categorias, categorias);
        adapter.setDropDownViewResource(R.layout.spinner_categorias);
        spCategorias.setAdapter(adapter);

        Intent intent = getIntent();
        user = (UserModel) intent.getSerializableExtra("user");
        isEditMode = Boolean.parseBoolean(intent.getStringExtra("mode"));

        tvUserPublicar.setText(user.getUsername());
        imgUserPublicar.setImageBitmap(imageUtil.transformarBytesBitmap(user.getFotoUsuario()));
        if(isEditMode){
            receta = (RecetaModel) intent.getSerializableExtra("receta");
            etIngredientes.setText(receta.getIngredientes());
            etInstrucciones.setText(receta.getInstrucciones());
            etDescripcion.setText(receta.getDescripcion());
            etTitulo.setText(receta.getTitulo());
            imgRecetaPublicar.setImageBitmap(imageUtil.transformarBytesBitmap(receta.getFotoReceta()));
        }
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


        imgRecetaPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la galería cuando el ImageView sea clickeado
                Intent intent = new Intent();
                intent.setType("image/*"); // Establece el tipo de datos que se espera seleccionar en la galería como imágenes.
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST); // Inicia una actividad para seleccionar una imagen de la galería y espera el resultado.
            }
        });


        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {
                if (tab.getTitle().equals("Inicio")) {
                    Intent intent = new Intent(PublicarActivity.this, InicioActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (tab.getTitle().equals("Buscar")) {
                    Intent intent = new Intent(PublicarActivity.this, BuscarActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (tab.getTitle().equals("Perfil")) {
                    Intent intent = new Intent(PublicarActivity.this, PerfilActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }

            }

            @Override
            public void onTabSelected(int lastIndex, AnimatedBottomBar.Tab lastTab, int newIndex, AnimatedBottomBar.Tab newTab) {
                if (newTab.getTitle().equals("Inicio")) {
                    Intent intent = new Intent(PublicarActivity.this, InicioActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (newTab.getTitle().equals("Buscar")) {
                    Intent intent = new Intent(PublicarActivity.this, BuscarActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                if (newTab.getTitle().equals("Perfil")) {
                    Intent intent = new Intent(PublicarActivity.this, PerfilActivity.class);
                    intent.putExtra("user", user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Cuando se selecciona una imagen de la galería, obtener la URI de la imagen
            Uri uri = data.getData(); // Obtiene la URI de la imagen seleccionada.
            try {
                // Convertir la URI en un Bitmap
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Establecer el Bitmap en el ImageView
                imgRecetaPublicar.setImageBitmap(imageUtil.redimensionarImagen(bitmap, 1000, 1000));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public void publicar(View view){
        if(bitmap == null){
            Toast.makeText(this, "Selecciona una foto para tu receta.", Toast.LENGTH_SHORT).show();
        }
        else if(etInstrucciones.getText().toString().isEmpty() || etDescripcion.getText().toString().isEmpty() ||
                etIngredientes.getText().toString().isEmpty()){
            Toast.makeText(this, "Ningún campo puede estar vacío.", Toast.LENGTH_SHORT).show();
        }else{
            RecetaModel receta = null;
            receta = new RecetaModel(
                    etTitulo.getText().toString(),
                    etDescripcion.getText().toString(),
                    etIngredientes.getText().toString(),
                    etInstrucciones.getText().toString(),
                    user.getUsername(),
                    0,
                    imageUtil.transformarBitmapBytes(imageUtil.redimensionarImagen(bitmap,800,600)),
                    spCategorias.getSelectedItem().toString(),
                    new Timestamp(System.currentTimeMillis()));
            try {
                recetaController.insertarReceta(receta);
                Toast.makeText(this, "Receta publicada con éxito.", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void ir(){
        Intent intent = new Intent(PublicarActivity.this, PublicacionActivity.class);
        startActivity(intent);
    }


}